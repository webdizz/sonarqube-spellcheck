package name.webdizz.sonar.grammar.spellcheck;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.File;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.sonar.api.batch.SensorContext;
import org.sonar.api.config.Settings;
import org.sonar.api.resources.JavaFile;
import org.sonar.api.rules.Rule;
import org.sonar.api.rules.RuleFinder;
import org.sonar.api.rules.RuleQuery;
import org.sonar.api.rules.Violation;
import name.webdizz.sonar.grammar.rule.GrammarRuleRepository;

@RunWith(MockitoJUnitRunner.class)
public class SourceGrammarAnalyserTest {

    @Mock
    private RuleFinder ruleFinder;

    @Mock
    private SensorContext sensorContext;

    @Mock
    private File file;

    @Mock
    private Settings settings;

    private SourceGrammarAnalyser testingInstance;

    @Before
    public void setup() {
        when(file.exists()).thenReturn(Boolean.TRUE);
        when(file.getName()).thenReturn("GrammarSensor.java");
        when(file.getParent()).thenReturn("/some_folder/src/main/java/name/webdizz/sonar/grammar/sensor");
        when(file.getAbsolutePath()).thenReturn("/some_folder/src/main/java/name/webdizz/sonar/grammar/sensor/GrammarSensor.java");
        when(settings.getString("sonar.sources")).thenReturn(",/some_folder/src/main/java");
        testingInstance = new SourceGrammarAnalyser(ruleFinder, settings);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldNotAcceptIncorrectSourceFil() {
        testingInstance.analyseSource(null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldNotProcessWithWrongContext() {
        testingInstance.analyseSource(file, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldAcceptOnlyExistingFiles() {
        when(file.exists()).thenReturn(Boolean.FALSE);
        testingInstance.analyseSource(file, sensorContext);
    }

    @Test
    public void shouldCreateGrammarViolation() {
        assertNotNull("Grammar violation is null", testingInstance.analyseSource(file, sensorContext));
    }

    @Test
    public void shouldFindGrammarRule() {
        testingInstance.analyseSource(file, sensorContext);
        ArgumentCaptor<RuleQuery> argument = ArgumentCaptor.forClass(RuleQuery.class);
        verify(ruleFinder).find(argument.capture());
        assertEquals("Rule config key is incorrect", "sonar_grammar_rule", argument.getValue().getConfigKey());
        assertEquals("Rule repository key is incorrect", GrammarRuleRepository.REPOSITORY_KEY, argument.getValue().getRepositoryKey());
    }

    @Test
    public void shouldIndexSourceCodeResource() {
        testingInstance.analyseSource(file, sensorContext);
        ArgumentCaptor<JavaFile> argument = ArgumentCaptor.forClass(JavaFile.class);
        verify(sensorContext).index(argument.capture());
        JavaFile resource = argument.getValue();
        assertEquals("Resource name is incorrect", "GrammarSensor", resource.getName());
        assertEquals("Package is incorrect", "name.webdizz.sonar.grammar.sensor", resource.getParent().getName());
    }

    @Test
    public void shouldIndexSourceCodeResourceMultiModuleProjectCase() {
        when(settings.getString("sonar.sources")).thenReturn("src/main/java");
        testingInstance.analyseSource(file, sensorContext);
        ArgumentCaptor<JavaFile> argument = ArgumentCaptor.forClass(JavaFile.class);
        verify(sensorContext).index(argument.capture());
        JavaFile resource = argument.getValue();
        assertEquals("Resource name is incorrect", "GrammarSensor", resource.getName());
        assertEquals("Package is incorrect", "name.webdizz.sonar.grammar.sensor", resource.getParent().getName());
    }

    @Test
    public void shouldIndexSourceCodeResourceMultiModuleProjectCase2() {
        when(file.getParent()).thenReturn("/some_folder/src/web");
        when(settings.getString("sonar.sources")).thenReturn("src/web");
        testingInstance.analyseSource(file, sensorContext);
        ArgumentCaptor<JavaFile> argument = ArgumentCaptor.forClass(JavaFile.class);
        verify(sensorContext).index(argument.capture());
        JavaFile resource = argument.getValue();
        assertEquals("Resource name is incorrect", "GrammarSensor", resource.getName());
        assertEquals("Package is incorrect", "[default]", resource.getParent().getName());
    }

    @Test
    public void shouldCreateGrammarViolations() {
        //TODO: need to clean things up
        when(ruleFinder.find(any(RuleQuery.class))).thenReturn(Rule.create());
        testingInstance = new SourceGrammarAnalyser(ruleFinder, settings);
        testingInstance.initialize();
        String filePath = getClass().getClassLoader().getResource("./").getPath() + "../../src/main/java" + "/" + SourceGrammarAnalyser.class.getPackage().getName().replaceAll("\\.", "\\/") + "/" + SourceGrammarAnalyser.class.getSimpleName() + ".java";
        File sourceFile = new File(filePath).getAbsoluteFile();
        List<Violation> violations = testingInstance.analyseSource(sourceFile, sensorContext);
        Violation violation = violations.get(0);
        assertNotNull("Resource was not set", violation.getResource());
        assertNotNull("Rule was not set", violation.getRule());
        assertNotNull("Violation line number was not set", violation.getLineId());
    }
}
