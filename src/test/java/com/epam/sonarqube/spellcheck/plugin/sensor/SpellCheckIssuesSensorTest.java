package com.epam.sonarqube.spellcheck.plugin.sensor;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.slf4j.LoggerFactory;
import org.sonar.api.batch.SensorContext;
import org.sonar.api.batch.fs.FilePredicate;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.fs.internal.DefaultFileSystem;
import org.sonar.api.batch.fs.internal.DefaultInputFile;
import org.sonar.api.batch.rule.ActiveRule;
import org.sonar.api.batch.rule.ActiveRules;
import org.sonar.api.component.ResourcePerspectives;
import org.sonar.api.resources.Project;
import org.sonar.api.rule.RuleKey;

import com.epam.sonarqube.spellcheck.plugin.spellcheck.SpellChecker;
import com.google.common.collect.Lists;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.LoggingEvent;
import ch.qos.logback.core.Appender;

@RunWith(MockitoJUnitRunner.class)
public class SpellCheckIssuesSensorTest {

    private final DefaultFileSystem fileSystem = createFileSystem();

    private final ResourcePerspectives perspectives = mock(ResourcePerspectives.class);

    private final SensorContext context = mock(SensorContext.class);

    private final SpellChecker spellChecker = mock(SpellChecker.class);

    private final Project module = new Project("sonarqube-spellcheck");

    private SpellCheckIssuesSensor testingInstance = null;

    @Mock
    private Appender<ILoggingEvent> mockAppender;
    // Captor is generic with ch.qos.logback.classic.spi.LoggingEvent
    @Captor
    private ArgumentCaptor<LoggingEvent> captorLoggingEvent;
    private Logger logger;

    @Before
    public void init() {
        testingInstance = new SpellCheckIssuesSensor(fileSystem, perspectives, spellChecker);
        ActiveRules activeRules = Mockito.mock(ActiveRules.class);
        ActiveRule activeRule = Mockito.mock(ActiveRule.class);
        when(activeRules.find(any(RuleKey.class))).thenReturn(activeRule);
        when(context.activeRules()).thenReturn(activeRules);

        logger = (Logger) LoggerFactory.getLogger(SpellCheckIssuesSensor.class);
        logger.addAppender(mockAppender);
    }

    @Test
    public void shouldListInputFilesDuringAnalysis() throws Exception {
        testingInstance.analyse(module, context);
        verify(fileSystem, atLeastOnce()).inputFiles(any(FilePredicate.class));
    }

    @Test
    public void shouldProcessInputFileDuringAnalysis() throws Exception {
        InputFile inputFile = createInputFile();
        List<InputFile> inputFiles = Lists.newArrayList(inputFile);
        when(fileSystem.inputFiles(any(FilePredicate.class))).thenReturn(inputFiles);

        testingInstance.analyse(module, context);

        verify(inputFile, atLeastOnce()).file();
    }

    @Test
    public void shouldReturnCorrectToStringOfSpellCheckIssuesSensorClass() throws Exception {
        String expectedToStringValue = "SpellCheck Issues";

        assertEquals(expectedToStringValue, testingInstance.toString());
    }

    @Test
    public void shouldCallHasFilesDuringDecidingWhichFilesToAlalyse() {
        when(fileSystem.hasFiles(any(FilePredicate.class))).thenReturn(true);

        testingInstance.shouldExecuteOnProject(module);

        verify(fileSystem, atLeastOnce()).hasFiles(any(FilePredicate.class));
    }

    @Test
    public void shouldWriteToLogDuringConstructingObjectIfLevelIsDebug() {
        logger.setLevel(Level.DEBUG);

        testingInstance = new SpellCheckIssuesSensor(fileSystem, perspectives, spellChecker);

        // Now verify our logging interactions
        verify(mockAppender, atLeastOnce()).doAppend(captorLoggingEvent.capture());
        // Having a generic captor means we don't need to cast
        final LoggingEvent loggingEvent = captorLoggingEvent.getValue();
        // Check log level is correct
        assertThat(loggingEvent.getLevel(), is(Level.DEBUG));
    }

    @Test
    public void shouldNotWriteToLogDuringConstructingObjectIfLoggerIsOff() {
        logger.setLevel(Level.OFF);

        testingInstance = new SpellCheckIssuesSensor(fileSystem, perspectives, spellChecker);

        // Now verify our logging interactions
        verify(mockAppender, times(0)).doAppend(any(LoggingEvent.class));
    }

    @Test
    public void shouldWriteToLogDuringAnalysisIfLevelIsDebug() {
        logger.setLevel(Level.DEBUG);

        testingInstance.analyse(module, context);

        // Now verify our logging interactions
        verify(mockAppender, atLeastOnce()).doAppend(captorLoggingEvent.capture());
        // Having a generic captor means we don't need to cast
        final LoggingEvent loggingEvent = captorLoggingEvent.getValue();
        // Check log level is correct
        assertThat(loggingEvent.getLevel(), is(Level.DEBUG));
    }

    @Test
    public void shouldNotWriteToLogDuringAnalysisIfLoggerIsOff() {
        logger.setLevel(Level.OFF);

        InputFile inputFile = createInputFile();
        List<InputFile> inputFiles = Lists.newArrayList(inputFile);
        when(fileSystem.inputFiles(any(FilePredicate.class))).thenReturn(inputFiles);

        testingInstance.analyse(module, context);

        // Now verify our logging interactions
        verify(mockAppender, times(0)).doAppend(any(LoggingEvent.class));
    }

    @Test
    public void shouldWriteToLogIfCantReadFileDuringAnalysis() {

        InputFile inputFile = mock(InputFile.class);
        doThrow(IOException.class).when(inputFile).file();
        when(inputFile.absolutePath()).thenReturn("mockedInputFile");

        List<InputFile> inputFiles = Lists.newArrayList(inputFile);
        when(fileSystem.inputFiles(any(FilePredicate.class))).thenReturn(inputFiles);

        testingInstance.analyse(module, context);

        // Now verify our logging interactions
        verify(mockAppender, atLeastOnce()).doAppend(captorLoggingEvent.capture());
        // Having a generic captor means we don't need to cast
        final LoggingEvent loggingEvent = captorLoggingEvent.getValue();
        // Check log level is correct
        assertThat(loggingEvent.getLevel(), is(Level.ERROR));
    }

    @Test
    public void shouldWriteToLogIfSpellCheckRuleIsNullDuringAnalysis() {
        logger.setLevel(Level.DEBUG);

        InputFile inputFile = createInputFile();
        List<InputFile> inputFiles = Lists.newArrayList(inputFile);
        when(fileSystem.inputFiles(any(FilePredicate.class))).thenReturn(inputFiles);

        when(context.activeRules().find(any(RuleKey.class))).thenReturn(null);

        testingInstance.analyse(module, context);

        // Now verify our logging interactions
        verify(mockAppender, atLeastOnce()).doAppend(captorLoggingEvent.capture());
        // Having a generic captor means we don't need to cast
        final LoggingEvent loggingEvent = captorLoggingEvent.getValue();
        // Check log level is correct
        assertThat(loggingEvent.getLevel(), is(Level.DEBUG));
    }

    private InputFile createInputFile() {
        InputFile inputFile = spy(new DefaultInputFile(module.key(), "SpellCheckPlugin.java"));
        ((DefaultInputFile) inputFile).setModuleBaseDir(fileSystem.baseDirPath());
        fileSystem.add((DefaultInputFile) inputFile);
        return inputFile;
    }

    private DefaultFileSystem createFileSystem() {
        File sourceFile = new File("src/main/java/com/epam/sonarqube/spellcheck/plugin");
        return spy(new DefaultFileSystem(sourceFile));
    }

}
