package name.webdizz.sonar.grammar.sensor;

import name.webdizz.sonar.grammar.PluginParameter;
import name.webdizz.sonar.grammar.spellcheck.GrammarChecker;
import name.webdizz.sonar.grammar.spellcheck.GrammarDictionaryLoader;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.batch.Sensor;
import org.sonar.api.batch.SensorContext;
import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.component.ResourcePerspectives;
import org.sonar.api.resources.Project;
import org.sonar.api.rule.RuleKey;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * The sensor for project files
 *
 */
public class GrammarIssuesSensor implements Sensor {

    private static final Logger LOGGER = LoggerFactory.getLogger(GrammarIssuesSensor.class);

    private final FileSystem fs;
    private final ResourcePerspectives perspectives;
    private final GrammarChecker grammarChecker;
    private final Lock wrapperLock = new ReentrantLock();
    private GrammarIssuesWrapper templateWrapper = null;

    /**
     * Use of IoC to get FileSystem
     *
     * @param fs
     * @param perspectives
     */
    public GrammarIssuesSensor(FileSystem fs, ResourcePerspectives perspectives) {
        this.fs = fs;
        this.perspectives = perspectives;
        this.grammarChecker = new GrammarChecker(new GrammarDictionaryLoader());
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Created the bean of grammar sensor.");
        }
    }

    @Override
    public String toString() {
        return "Grammar Issues";
    }

    @Override
    public void analyse(Project module, SensorContext context) {
        if (LOGGER.isDebugEnabled()) {
            Object[] arguments = new Object[]{module.getName(), module.getKey(), module.getDescription()};
            // about module
            LOGGER.debug("Module name={} key={} description=\"{}\"", arguments);
            // about context
            LOGGER.debug("SensorContext {}", context.toString());
        }
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Initialize the GrammarChecker.");
        }
        grammarChecker.initialize();
        
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Checking {}-files.", PluginParameter.PROFILE_LANGUAGE);
        }
        
        for (final InputFile inputFile : fs.inputFiles(fs.predicates().hasLanguage(PluginParameter.PROFILE_LANGUAGE))) {
            processInputFile(inputFile);
        }
    }

    @Override
    public boolean shouldExecuteOnProject(Project project) {
        // This sensor is executed only when there are Java files
        return fs.hasFiles(fs.predicates().hasLanguage(PluginParameter.PROFILE_LANGUAGE));
    }
// private methods

    private void processInputFile(final InputFile inputFile) {
        if (LOGGER.isDebugEnabled()){
            LOGGER.debug("Processing input-file {}", inputFile.toString());
        }
        // initialize a lines counter
        int lineNumber = 1;
        try {
            final List<String> code = FileUtils.readLines(inputFile.file());
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Has read code-lines from {}\nHave read {} lines.", inputFile.toString(), code.size());
            }

            for (final String line : code) {
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("Processing {}:\"{}\"", lineNumber, line);
                }
                // processing current line
                processInputCodeLine(line, inputFile, lineNumber);
                // increment the lines counter
                lineNumber++;
            }
        } catch (IOException ex) {
            LOGGER.error("Can't read data from file " + inputFile.absolutePath(), ex);
        }
    }

    private void processInputCodeLine(final String line, final InputFile inputFile, int lineNumber) {
        if (!StringUtils.isEmpty(line)) {
            // prepare wrapper for the line
            final GrammarIssuesWrapper lineWrapper = wrap(inputFile, line, lineNumber);
            
            if (LOGGER.isDebugEnabled()) {
                final Object[] argumnets = new Object[]{lineNumber, line, inputFile.toString()};
                LOGGER.debug("Prepared issues-wrapper for \n {}:\"{}\"\nin{}", argumnets);
                LOGGER.debug(lineWrapper.toString());
            }
            
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Begin check line \"{}\"", line);
            }
            
            // checking the line
            grammarChecker.checkSpelling(line, new GrammarViolationTrigger(lineWrapper));
            
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("End check line \"{}\"", line);
            }
        } else {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Skipped empty line #{} in {}", lineNumber, inputFile.toString());
            }
        }
    }

    private GrammarIssuesWrapper wrap(final InputFile resourse, final String line, final int lineNumber) {
        wrapperLock.lock();
        try {
            return (templateWrapper == null)
                    ? templateWrapper = GrammarIssuesWrapper.builder()
                    .setInputFile(resourse)
                    .setLine(line)
                    .setLineNumber(lineNumber)
                    .setPerspectives(perspectives)
                    .setRuleKey(RuleKey.of(PluginParameter.REPOSITORY_KEY, PluginParameter.SONAR_GRAMMAR_RULE_KEY))
                    .build()
                    : GrammarIssuesWrapper.builder(templateWrapper)
                    .setInputFile(resourse)
                    .setLine(line)
                    .setLineNumber(lineNumber)
                    .build();

        } finally {
            wrapperLock.unlock();
        }
    }

}
