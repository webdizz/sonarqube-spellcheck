package name.webdizz.sonar.grammar.sensor;

import name.webdizz.sonar.grammar.rule.GrammarRuleRepository;

import java.io.File;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.batch.Sensor;
import org.sonar.api.batch.SensorContext;
import org.sonar.api.config.Settings;
import org.sonar.api.resources.JavaFile;
import org.sonar.api.resources.Project;
import org.sonar.api.rules.Rule;
import org.sonar.api.rules.RuleFinder;
import org.sonar.api.rules.RuleQuery;
import org.sonar.api.rules.Violation;
import org.sonar.api.scan.filesystem.FileQuery;
import org.sonar.api.scan.filesystem.FileType;
import org.sonar.api.scan.filesystem.ModuleFileSystem;

public class GrammarSensor implements Sensor {

    private static final Logger LOGGER = LoggerFactory.getLogger(GrammarSensor.class);

    private Settings settings;

    private ModuleFileSystem fileSystem;

    private final RuleFinder ruleFinder;

    public GrammarSensor(final Settings settings, final ModuleFileSystem fileSystem, final RuleFinder ruleFinder) {
        this.settings = settings;
        this.fileSystem = fileSystem;
        this.ruleFinder = ruleFinder;
    }

    @Override
    public void analyse(final Project project, final SensorContext sensorContext) {
        LOGGER.info("Is about to analyse grammar on project {}", project.getName());
        FileQuery fileQuery = FileQuery.on(FileType.SOURCE);
        List<File> files = fileSystem.files(fileQuery);
        analyseResources(files, sensorContext);
        LOGGER.info("Source files: {}", files);
    }

    private void analyseResources(final List<File> files, final SensorContext sensorContext) {
        for (File file : files) {
            if ("GrammarSensor.java".equals(file.getName())) {
                boolean unitTest = false;
                JavaFile resource = new JavaFile("name.webdizz.sonar.grammar.sensor", "GrammarSensor", unitTest);
                sensorContext.index(resource);
                RuleQuery ruleQuery = RuleQuery.create()
                        .withRepositoryKey(GrammarRuleRepository.REPOSITORY_KEY)
                        .withConfigKey("first_sonar_grammar_rule_key");
                Rule rule = ruleFinder.find(ruleQuery);

                Violation violation = Violation.create(rule, resource);
                violation.setLineId(10);
                violation.setNew(true);
                violation.setMessage("hello");
                sensorContext.saveViolation(violation);
                break;
            }
        }
    }

    @Override
    public boolean shouldExecuteOnProject(final Project project) {
        return true;
    }
}
