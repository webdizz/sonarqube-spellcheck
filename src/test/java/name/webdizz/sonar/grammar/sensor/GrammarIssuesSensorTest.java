package name.webdizz.sonar.grammar.sensor;

import name.webdizz.sonar.grammar.PluginParameter;
import org.junit.Test;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.batch.SensorContext;
import org.sonar.api.batch.fs.FilePredicate;
import org.sonar.api.batch.fs.FilePredicates;
import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.component.ResourcePerspectives;
import org.sonar.api.issue.Issuable;
import org.sonar.api.issue.Issue;
import org.sonar.api.resources.Project;
import org.sonar.api.rule.RuleKey;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

public class GrammarIssuesSensorTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(GrammarIssuesSensorTest.class);

    private final FileSystem fs = mock(FileSystem.class);

    private final ResourcePerspectives perspectives = mock(ResourcePerspectives.class);

    private final GrammarIssuesSensor instance = new GrammarIssuesSensor(fs, perspectives);

    /**
     * Test of analyse method, of class GrammarIssuesSensor.
     */
    @Test
    public void testAnalyse() {
        LOGGER.info("Testing analyse");
        Project module = mock(Project.class);
        SensorContext context = mock(SensorContext.class);
        // fs.inputFiles(fs.predicates().hasLanguage(PluginParameter.PROFILE_LANGUAGE))
        FilePredicate predicate = mock(FilePredicate.class);
        FilePredicates predicates = mock(FilePredicates.class);
        when(fs.predicates()).thenReturn(predicates);
        when(predicates.hasLanguage(PluginParameter.PROFILE_LANGUAGE)).thenReturn(predicate);
        List<InputFile> inputFiles = new ArrayList<>();
        when(fs.inputFiles(predicate)).thenReturn(inputFiles);
        InputFile inputFile = mock(InputFile.class);
        File sourceFile = new File("src/main/java/name/webdizz/sonar/grammar/GrammarPlugin.java");
        assertTrue("Not accessible file for tests " + sourceFile.getAbsolutePath(), sourceFile.exists());
        when(inputFile.file()).thenReturn(sourceFile);
        when(inputFile.absolutePath()).thenReturn(sourceFile.getAbsolutePath());
        inputFiles.add(inputFile);

        Issuable issuable = mock(Issuable.class);
        Issuable.IssueBuilder builder = mock(Issuable.IssueBuilder.class);
        Issue issue = mock(Issue.class);
        when(issuable.newIssueBuilder()).thenReturn(builder);
        when(builder.attribute(anyString(), anyString())).thenReturn(builder);
        when(builder.effortToFix(anyDouble())).thenReturn(builder);
        when(builder.line(anyInt())).thenReturn(builder);
        when(builder.message(anyString())).thenReturn(builder);
        when(builder.reporter(anyString())).thenReturn(builder);
        when(builder.ruleKey(Mockito.<RuleKey>anyObject())).thenReturn(builder);
        when(builder.severity(anyString())).thenReturn(builder);
        when(builder.build()).thenReturn(issue);

        when(perspectives.as(Issuable.class, inputFile)).thenReturn(issuable);

        instance.analyse(module, context);

        // check the behavior
        verify(issuable, atLeastOnce()).addIssue(issue);
        verify(builder, atLeastOnce()).build();
        verify(inputFile).file();
        verify(fs, atLeastOnce()).inputFiles(predicate);
        verify(predicates, atLeastOnce()).hasLanguage(PluginParameter.PROFILE_LANGUAGE);

        LOGGER.info("Done.");
    }

    /**
     * Test of shouldExecuteOnProject method, of class GrammarIssuesSensor.
     */
    @Test
    public void testShouldExecuteOnProject() {
        LOGGER.info("Testing shouldExecuteOnProject");
        Project project = mock(Project.class);
        // fs.hasFiles(fs.predicates().hasLanguage(PluginParameter.PROFILE_LANGUAGE));
        FilePredicate predicate = mock(FilePredicate.class);
        FilePredicates predicates = mock(FilePredicates.class);
        when(fs.predicates()).thenReturn(predicates);
        when(predicates.hasLanguage(PluginParameter.PROFILE_LANGUAGE)).thenReturn(predicate);
        when(fs.hasFiles(predicate)).thenReturn(Boolean.TRUE);

        boolean result;
        result = instance.shouldExecuteOnProject(project);
        assertEquals(true, result);

        when(fs.hasFiles(predicate)).thenReturn(Boolean.FALSE);
        result = instance.shouldExecuteOnProject(project);
        assertEquals(false, result);

        // check the behavior
        verify(fs, atLeast(2)).hasFiles(predicate);
        verify(predicates, atLeast(2)).hasLanguage(PluginParameter.PROFILE_LANGUAGE);

        LOGGER.info("Done.");
    }

}
