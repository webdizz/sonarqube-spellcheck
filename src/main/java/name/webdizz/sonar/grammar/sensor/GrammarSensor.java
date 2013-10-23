package name.webdizz.sonar.grammar.sensor;

import name.webdizz.sonar.grammar.spellcheck.SourceGrammarAnalyser;

import java.io.File;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.batch.Sensor;
import org.sonar.api.batch.SensorContext;
import org.sonar.api.config.Settings;
import org.sonar.api.resources.Project;
import org.sonar.api.rules.RuleFinder;
import org.sonar.api.scan.filesystem.FileQuery;
import org.sonar.api.scan.filesystem.FileType;
import org.sonar.api.scan.filesystem.ModuleFileSystem;

public class GrammarSensor implements Sensor {

    private static final Logger LOGGER = LoggerFactory.getLogger(GrammarSensor.class);

    private Settings settings;

    private ModuleFileSystem fileSystem;

    private final RuleFinder ruleFinder;

    private SourceGrammarAnalyser grammarAnalyser;

    public GrammarSensor(final Settings settings, final ModuleFileSystem fileSystem, final RuleFinder ruleFinder) {
        this.settings = settings;
        this.fileSystem = fileSystem;
        this.ruleFinder = ruleFinder;
        this.grammarAnalyser = new SourceGrammarAnalyser(ruleFinder, settings);
        grammarAnalyser.initialize();
    }

    @Override
    public void analyse(final Project project, final SensorContext sensorContext) {
        LOGGER.info("Is about to analyse grammar on project {}", project.getName());
        FileQuery fileQuery = FileQuery.on(FileType.SOURCE);
        List<File> files = fileSystem.files(fileQuery);
        analyseResources(files, sensorContext);
        if (LOGGER.isDebugEnabled()) {
            LOGGER.info("Next source files were processed: {}", files);
        }
    }

    private void analyseResources(final List<File> files, final SensorContext sensorContext) {
        for (File file : files) {
            grammarAnalyser.analyseSource(file, sensorContext);
        }
    }

    @Override
    public boolean shouldExecuteOnProject(final Project project) {
        return true;
    }

    public void setGrammarAnalyser(final SourceGrammarAnalyser grammarAnalyser) {
        this.grammarAnalyser = grammarAnalyser;
    }
}
