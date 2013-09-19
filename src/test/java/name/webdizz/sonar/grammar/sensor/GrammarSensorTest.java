package name.webdizz.sonar.grammar.sensor;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.sonar.api.batch.SensorContext;
import org.sonar.api.config.Settings;
import org.sonar.api.resources.Project;
import org.sonar.api.scan.filesystem.FileQuery;
import org.sonar.api.scan.filesystem.ModuleFileSystem;

import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;

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

    private GrammarSensor testingInstance;

    @Before
    public void setUp() {
        testingInstance = new GrammarSensor(settings, fileSystem);
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
}
