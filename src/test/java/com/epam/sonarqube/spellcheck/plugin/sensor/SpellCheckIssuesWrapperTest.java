package com.epam.sonarqube.spellcheck.plugin.sensor;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.mockito.Mockito.anyDouble;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.component.ResourcePerspectives;
import org.sonar.api.issue.Issuable;
import org.sonar.api.issue.Issue;
import org.sonar.api.rule.RuleKey;

/**
 * Unit test for GrammarIssuesWrapper
 */
public class SpellCheckIssuesWrapperTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(SpellCheckIssuesWrapperTest.class);

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

    public SpellCheckIssuesWrapperTest() {

    }

    @Test
    public void testGetLine() {
        String expResult = "ABCDEF";
        SpellCheckIssuesWrapper instance
                = SpellCheckIssuesWrapper.builder(wrapper).setLine(expResult).build();
        String result = instance.getLine();
        assertEquals(expResult, result);
    }

    @Test
    public void testGetLineNumber() {
        int expResult = 100;
        SpellCheckIssuesWrapper instance
                = SpellCheckIssuesWrapper.builder(wrapper).setLineNumber(expResult).build();
        int result = instance.getLineNumber();
        assertEquals(expResult, result);
    }

    @Test
    public void testIncident_String_int() {
        String message = "ABCD";
        int column = 10;
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
    }

    @Test
    public void testBuilder_0args() {
        SpellCheckIssuesWrapper.Builder expResult = SpellCheckIssuesWrapper.builder();
        SpellCheckIssuesWrapper.Builder result = SpellCheckIssuesWrapper.builder();
        assertNotEquals(expResult, result);
    }

    @Test
    public void testBuilder_GrammarIssuesWrapper() {
        SpellCheckIssuesWrapper.Builder expResult = SpellCheckIssuesWrapper.builder(wrapper);
        SpellCheckIssuesWrapper.Builder result = SpellCheckIssuesWrapper.builder(wrapper);
        assertNotEquals(expResult, result);
    }

}
