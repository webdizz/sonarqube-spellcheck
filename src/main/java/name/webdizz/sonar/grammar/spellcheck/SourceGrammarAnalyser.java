package name.webdizz.sonar.grammar.spellcheck;

import name.webdizz.sonar.grammar.rule.GrammarRuleRepository;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
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
import org.sonar.api.rules.RuleQuery;
import org.sonar.api.rules.Violation;

import com.google.common.base.Strings;
import com.swabunga.spell.event.SpellCheckEvent;
import com.swabunga.spell.event.SpellCheckListener;

import static com.google.common.base.Preconditions.checkArgument;

public class SourceGrammarAnalyser implements ServerExtension {

    private final static Logger LOGGER = LoggerFactory.getLogger(SourceGrammarAnalyser.class);

    private final RuleFinder ruleFinder;

    private GrammarChecker grammarChecker;

    public SourceGrammarAnalyser(final RuleFinder ruleFinder, final Settings settings) {
        this.ruleFinder = ruleFinder;
        this.grammarChecker = new GrammarChecker(new GrammarDictionaryLoader(settings));
    }

    public void initialize() {
        grammarChecker.initialize();
    }

    public List<Violation> analyseSource(final File file, final SensorContext sensorContext) {
        validateArguments(file, sensorContext);
        Rule rule = findGrammarRule();

        String fileName = FilenameUtils.getBaseName(file.getName());
        String packageName = getPackageNameForSource(file);
        JavaFile resource = new JavaFile(packageName, fileName);
        sensorContext.index(resource);
        List<Violation> violations = collectGrammarViolations(file, rule, resource);
        sensorContext.saveViolations(violations);
        return new ArrayList<Violation>(violations);
    }

    private List<Violation> collectGrammarViolations(final File file, final Rule rule, final JavaFile resource) {
        final List<Violation> violations = new ArrayList();
        try {
            List<String> sourceLines = FileUtils.readLines(file);
            int lineNumber = 1;
            for (String sourceLine : sourceLines) {
                if (!Strings.isNullOrEmpty(sourceLine)) {
                    final int currentLineNumber = lineNumber;
                    grammarChecker.checkSpelling(sourceLine, new SpellCheckListener() {
                        @Override
                        public void spellingError(final SpellCheckEvent event) {
                            List suggestions = event.getSuggestions();
                            if (CollectionUtils.isNotEmpty(suggestions)) {
                                LOGGER.info("MISSPELT WORD: {} at {}", event.getInvalidWord(), event.getWordContextPosition());
                                for (Object suggestion : suggestions) {

                                    final Violation violation = Violation.create(rule, resource);
                                    violation.setLineId(currentLineNumber);
                                    violation.setMessage(suggestion.toString());
                                    violations.add(violation);

                                    LOGGER.info("\nSuggested Word: {}", suggestion);
                                }
                            } else {
                                LOGGER.info("MISSPELT WORD: {} at {}", event.getInvalidWord(), event.getWordContextPosition());
                                LOGGER.info("\nNo suggestions");
                            }
                        }
                    });
                }
                lineNumber++;
            }
        } catch (IOException e) {
            LOGGER.error("Unable to read source file", e);
        }

        return violations;
    }

    private String getPackageNameForSource(final File file) {
        return file.getParent().split("src/main/java/")[1].replaceAll("/", ".");
    }

    private Rule findGrammarRule() {
        RuleQuery ruleQuery = RuleQuery.create()
                .withRepositoryKey(GrammarRuleRepository.REPOSITORY_KEY)
                .withConfigKey("sonar_grammar_rule");
        return ruleFinder.find(ruleQuery);
    }

    private void validateArguments(final File file, final SensorContext sensorContext) {
        checkArgument(file != null, "Source code is null");
        checkArgument(sensorContext != null, "SensorContext is null");
        checkArgument(file.exists(), "Source code file does not exist");
    }
}
