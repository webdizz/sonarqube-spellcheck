package com.epam.sonarqube.spellcheck.plugin.sensor;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.LoggingEvent;
import ch.qos.logback.core.Appender;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.runners.MockitoJUnitRunner;
import org.slf4j.LoggerFactory;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.component.ResourcePerspectives;
import org.sonar.api.issue.Issuable;
import org.sonar.api.issue.Issue;
import org.sonar.api.rule.RuleKey;

/**
 * Unit test for GrammarIssuesWrapper
 */
@RunWith(MockitoJUnitRunner.class)
public class SpellCheckIssuesWrapperTest {

    private Logger logger;
    private Appender mockAppender = mock(Appender.class);
    @Captor
    private ArgumentCaptor<LoggingEvent> captorLoggingEvent;
    private final InputFile inputFile = mock(InputFile.class);
    private final ResourcePerspectives perspectives = mock(ResourcePerspectives.class);
    private final RuleKey ruleKey = mock(RuleKey.class);

    private final SpellCheckIssuesWrapper wrapper = SpellCheckIssuesWrapper.builder()
            .setInputFile(inputFile)
            .setLine("Test line")
            .setLineNumber(1)
            .setPerspectives(perspectives)
            .setRuleKey(ruleKey)
            .build();

    @Before
    public void setup() {
        logger = (Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
        logger.addAppender(mockAppender);
    }

    @After
    public void teardown() {
        logger = (Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
        logger.detachAppender(mockAppender);
    }

    @Test(expected = IllegalStateException.class)
    public void shouldThrowExceptionWhenBuildWrapperWithNullInputFile() {
        SpellCheckIssuesWrapper testWrapper = SpellCheckIssuesWrapper.builder()
                .setInputFile(null)
                .setLine("Test line")
                .setLineNumber(1)
                .setPerspectives(perspectives)
                .setRuleKey(ruleKey)
                .build();
    }

    @Test(expected = IllegalStateException.class)
    public void shouldThrowExceptionWhenBuildWrapperOnEmptyLine() {
        SpellCheckIssuesWrapper testWrapper = SpellCheckIssuesWrapper.builder()
                .setInputFile(inputFile)
                .setLine("")
                .setLineNumber(1)
                .setPerspectives(perspectives)
                .setRuleKey(ruleKey)
                .build();
    }

    @Test
    public void shouldProperlyBuildWrapperFromSourceWrapperAndReturnLine() {
        String expResult = "ABCDEF";
        SpellCheckIssuesWrapper instance
                = SpellCheckIssuesWrapper.builder(wrapper).setLine(expResult).build();
        String result = instance.getLine();
        assertEquals(expResult, result);
    }

    @Test
    public void shouldProperlyConstructWrapperWithSourceWrapperAndNewLineNumber() {
        int expResult = 100;
        SpellCheckIssuesWrapper instance
                = SpellCheckIssuesWrapper.builder(wrapper).setLineNumber(expResult).build();
        int result = instance.getLineNumber();
        assertEquals(expResult, result);
    }

    @Test
    public void shouldProperlyConstructWrapperAndIncidentAndWriteDebugInfo() {
        String message = "ABCD";
        int column = 10;
        logger.setLevel(Level.DEBUG);
        ResourcePerspectives mockPerspectives = mock(ResourcePerspectives.class);
        Issuable issuable = mock(Issuable.class);
        Issuable.IssueBuilder builder = mock(Issuable.IssueBuilder.class);
        Issue issue = mock(Issue.class);

        when(issuable.newIssueBuilder()).thenReturn(builder);
        when(builder.attribute(anyString(), anyString())).thenReturn(builder);
        when(builder.effortToFix(anyDouble())).thenReturn(builder);
        when(builder.line(wrapper.getLineNumber())).thenReturn(builder);
        when(builder.message(message)).thenReturn(builder);
        when(builder.reporter(anyString())).thenReturn(builder);
        when(builder.ruleKey(ruleKey)).thenReturn(builder);
        when(builder.severity(anyString())).thenReturn(builder);
        when(builder.build()).thenReturn(issue);
        when(mockPerspectives.as(Issuable.class, inputFile)).thenReturn(issuable);
        SpellCheckIssuesWrapper instance
                = SpellCheckIssuesWrapper.builder(wrapper)
                .setPerspectives(mockPerspectives)
                .build();
        instance.incident(message, message, column);

        // check behavior
        verify(mockPerspectives).as(Issuable.class, inputFile);
        verify(builder).build();
        verify(builder).ruleKey(ruleKey);
        verify(builder).message(message);
        verify(builder).attribute(org.mockito.Matchers.anyString(), eq(Integer.toString(column)));
        verify(issuable).addIssue(issue);

        verify(mockAppender, atLeastOnce()).doAppend(captorLoggingEvent.capture());
        final LoggingEvent loggingEvent = captorLoggingEvent.getValue();
        assertThat(loggingEvent.getLevel(), is(Level.DEBUG));
    }

    @Test
    public void shouldConstructDifferentBuildersFromEmptyWrapper() {
        SpellCheckIssuesWrapper.Builder expResult = SpellCheckIssuesWrapper.builder();
        SpellCheckIssuesWrapper.Builder result = SpellCheckIssuesWrapper.builder();
        assertNotEquals(expResult, result);
    }

    @Test
    public void shouldConstructDifferentBuilderFromSameWrappersAndWithoutLogginInfo() {
        logger.setLevel(Level.OFF);
        SpellCheckIssuesWrapper.Builder expResult = SpellCheckIssuesWrapper.builder(wrapper);
        SpellCheckIssuesWrapper.Builder result = SpellCheckIssuesWrapper.builder(wrapper);
        assertNotEquals(expResult, result);
        verify(mockAppender, never()).doAppend(captorLoggingEvent.capture());
    }
}
