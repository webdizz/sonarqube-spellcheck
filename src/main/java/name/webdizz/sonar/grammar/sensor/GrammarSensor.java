package name.webdizz.sonar.grammar.sensor;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.batch.Sensor;
import org.sonar.api.batch.SensorContext;
import org.sonar.api.config.Settings;
import org.sonar.api.resources.Project;
import org.sonar.api.scan.filesystem.FileQuery;
import org.sonar.api.scan.filesystem.FileType;
import org.sonar.api.scan.filesystem.ModuleFileSystem;
import org.sonar.squid.api.SourceFile;
import org.sonar.squid.recognizer.CodeRecognizer;
import org.sonar.squid.recognizer.Detector;
import org.sonar.squid.recognizer.LanguageFootprint;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class GrammarSensor implements Sensor {

    private static final Logger LOGGER = LoggerFactory.getLogger(GrammarSensor.class);

    private Settings settings;

    private ModuleFileSystem fileSystem;

    public GrammarSensor(final Settings settings, final ModuleFileSystem fileSystem) {
        this.settings = settings;
        this.fileSystem = fileSystem;
    }

    @Override
    public void analyse(final Project project, final SensorContext sensorContext) {
        LOGGER.info("Is about to analyse grammar on project {}", project.getName());
        FileQuery fileQuery = FileQuery.on(FileType.SOURCE);
        List<File> files = fileSystem.files(fileQuery);
        analyseResources(files);
        LOGGER.info("Source files: " + files);
    }

    private void analyseResources(final List<File> files) {
        Reader reader = null;
        for (File file : files) {
            try {
                reader = new StringReader(FileUtils.readFileToString(file, fileSystem.sourceCharset()));
                org.sonar.api.resources.File resource = org.sonar.api.resources.File.fromIOFile(file, fileSystem.sourceDirs());
                SourceFile sourceFile = new SourceFile(resource.getKey(), resource.getName());

            } catch (IOException e) {
                LOGGER.error("Unable to read resource", e);
            } finally {
                IOUtils.closeQuietly(reader);
            }
        }
    }

    class GrammarCodeRecognizer extends CodeRecognizer {

        public GrammarCodeRecognizer() {
            super(1.0, new LanguageFootprint() {
                @Override
                public Set<Detector> getDetectors() {
                    return Collections.emptySet();
                }
            });
        }
    }

    @Override
    public boolean shouldExecuteOnProject(final Project project) {
        return true;
    }
}
