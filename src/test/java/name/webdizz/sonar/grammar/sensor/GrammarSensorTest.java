package name.webdizz.sonar.grammar.sensor;

import name.webdizz.sonar.grammar.spellcheck.GrammarChecker;
import name.webdizz.sonar.grammar.spellcheck.SourceGrammarAnalyser;

import java.io.File;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.sonar.api.batch.SensorContext;
import org.sonar.api.config.Settings;
import org.sonar.api.resources.Project;
import org.sonar.api.rules.RuleFinder;
import org.sonar.api.scan.filesystem.FileQuery;
import org.sonar.api.scan.filesystem.ModuleFileSystem;

import static com.google.common.collect.Lists.newArrayList;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class GrammarSensorTest {

    @Mock
    private Settings settings;

    @Mock
    private ModuleFileSystem fileSystem;

    @Mock
    private Project project;

    @Mock
    private SensorContext sensorContext;

    @Mock
    private SourceGrammarAnalyser grammarAnalyser;

    @Mock
    private RuleFinder ruleFinder;

    @Mock
    private GrammarChecker grammarChecker;

    @InjectMocks
    private GrammarSensor testingInstance;

    @Before
    public void setUp() {
        testingInstance = new GrammarSensor(settings, fileSystem, ruleFinder);
        testingInstance.setGrammarAnalyser(grammarAnalyser);
    }

    @Test
    public void shouldExecuteOnAnyProject() throws Exception {
        assertTrue(testingInstance.shouldExecuteOnProject(null));
    }

    @Test
    public void shouldQueryForFilesForAnalyse() {
        testingInstance.analyse(project, sensorContext);
        verify(fileSystem).files(any(FileQuery.class));
    }

    @Test
    public void shouldAnalyseEachSourceFile() {
        File file = mock(File.class);
        when(fileSystem.files(any(FileQuery.class))).thenReturn(newArrayList(file));
        testingInstance.analyse(project, sensorContext);
        verify(grammarAnalyser).analyseSource(any(File.class), eq(sensorContext));
    }

}
