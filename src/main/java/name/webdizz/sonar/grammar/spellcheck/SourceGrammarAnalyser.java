package name.webdizz.sonar.grammar.spellcheck;

import static com.google.common.base.Preconditions.checkArgument;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.ServerExtension;
import org.sonar.api.batch.SensorContext;
import org.sonar.api.config.Settings;
import org.sonar.api.resources.JavaFile;
import org.sonar.api.rules.Rule;
import org.sonar.api.rules.RuleFinder;
import org.sonar.api.rules.Violation;
import name.webdizz.sonar.grammar.rule.RuleFinderHelper;

import com.google.common.base.Strings;
import java.util.Arrays;

public class SourceGrammarAnalyser implements ServerExtension {

    private final static Logger LOGGER = LoggerFactory.getLogger(SourceGrammarAnalyser.class);

    private GrammarChecker grammarChecker;
    private final Rule grammarRule;
    private final Settings settings;

    public SourceGrammarAnalyser(final RuleFinder ruleFinder, final Settings settings) {
        this.grammarChecker = new GrammarChecker(new GrammarDictionaryLoader(settings));
        this.settings = settings;
        this.grammarRule = RuleFinderHelper.findGrammarRule(ruleFinder);
    }

    public void initialize() {
        grammarChecker.initialize();
    }

    public List<Violation> analyseSource(final File file, final SensorContext sensorContext) {
        validateArguments(file, sensorContext);

        String fileName = FilenameUtils.getBaseName(file.getName());
        String packageName = getPackageNameForSource(file);
        JavaFile resource = new JavaFile(packageName, fileName);
        sensorContext.index(resource);
        List<Violation> violations = collectGrammarViolations(file, resource);
        sensorContext.saveViolations(violations);
        return new ArrayList<Violation>(violations);
    }

    private List<Violation> collectGrammarViolations(final File file, final JavaFile resource) {
        final List<Violation> violations = new ArrayList();
        try {
            List<String> sourceLines = FileUtils.readLines(file);
            int lineNumber = 0;
            for (String sourceLine : sourceLines) {
                lineNumber++;
                if (Strings.isNullOrEmpty(sourceLine)) {
                    continue;
                }
                SpellAction spellCheckAction;
                spellCheckAction = createSpellAction(resource, lineNumber, sourceLine);
                violations.addAll(spellCheckAction.spell());
            }
        } catch (IOException e) {
            LOGGER.error("Unable to read source file", e);
        }
        return violations;
    }

    private SpellAction createSpellAction(final JavaFile resource, final int lineNumber, final String sourceLine) {
        SpellAction.Builder spellCheckActionBuilder;
        spellCheckActionBuilder = new SpellAction.Builder();
        spellCheckActionBuilder.setLineNumber(lineNumber);
        spellCheckActionBuilder.setResource(resource);
        spellCheckActionBuilder.setRule(this.grammarRule);
        spellCheckActionBuilder.setSourceLine(sourceLine);
        spellCheckActionBuilder.setGrammarChecker(this.grammarChecker);
        return spellCheckActionBuilder.build();
    }

    private String getPackageNameForSource(final File file) {
        final String fileParentPath = file.getParent();
        String sonarSourcePath = settings.getString("sonar.sources");
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Resolving package name from sonar.sources is: '{}', file directory path is: '{}'", sonarSourcePath, fileParentPath);
        }
        if (sonarSourcePath.startsWith(",")){
            // path from root
            sonarSourcePath = sonarSourcePath.substring(1);
        }else {
            // path relative to parent
            final int index;
            if ((index=fileParentPath.indexOf(sonarSourcePath)) < 0){
                // in parent sonar-source-path is not exists
                // empty package name will be returned 
                return " ";
            }
            sonarSourcePath = fileParentPath.substring(0, index + sonarSourcePath.length());
        }
        final File sonarSource = new File(sonarSourcePath).getAbsoluteFile();
        final List<File> sourceDir = Arrays.asList(new File[]{sonarSource});
        final JavaFile resource = JavaFile.fromIOFile(file, sourceDir, false);
        return resource == null ? " " : resource.getParent().getKey();
    }

    private void validateArguments(final File file, final SensorContext sensorContext) {
        checkArgument(file != null, "Source code is null");
        checkArgument(sensorContext != null, "SensorContext is null");
        checkArgument(file.exists(), "Source code file does not exist");
    }
}
