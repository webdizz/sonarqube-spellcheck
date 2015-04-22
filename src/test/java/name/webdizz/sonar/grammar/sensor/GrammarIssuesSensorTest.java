package name.webdizz.sonar.grammar.sensor;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.File;
import java.util.List;

import org.junit.Test;
import org.sonar.api.batch.SensorContext;
import org.sonar.api.batch.fs.FilePredicate;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.fs.internal.DefaultFileSystem;
import org.sonar.api.batch.fs.internal.DefaultInputFile;
import org.sonar.api.component.ResourcePerspectives;
import org.sonar.api.config.Settings;
import org.sonar.api.resources.Project;

import com.google.common.collect.Lists;

public class GrammarIssuesSensorTest {


    private final DefaultFileSystem fileSystem = createFileSystem();

    private final ResourcePerspectives perspectives = mock(ResourcePerspectives.class);

    private final Settings settings = mock(Settings.class);

    private final GrammarIssuesSensor testingInstance = new GrammarIssuesSensor(fileSystem, perspectives, settings);
    private final Project module = new Project("sonar-grammar");
    private final SensorContext context = mock(SensorContext.class);

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

    private InputFile createInputFile() {
        InputFile inputFile = spy(new DefaultInputFile(module.key(), "GrammarPlugin.java"));
        ((DefaultInputFile) inputFile).setModuleBaseDir(fileSystem.baseDirPath());
        return inputFile;
    }

    private DefaultFileSystem createFileSystem() {
        File sourceFile = new File("src/main/java/name/webdizz/sonar/grammar");
        return spy(new DefaultFileSystem(sourceFile));
    }

}
