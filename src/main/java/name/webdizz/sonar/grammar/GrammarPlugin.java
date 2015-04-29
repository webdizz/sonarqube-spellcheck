package name.webdizz.sonar.grammar;

import java.util.Arrays;
import java.util.List;
import name.webdizz.sonar.grammar.profile.GrammarProfileDefinition;

import name.webdizz.sonar.grammar.spellcheck.GrammarChecker;
import name.webdizz.sonar.grammar.spellcheck.GrammarDictionaryLoader;
import name.webdizz.sonar.grammar.spellcheck.JavaSourceCodeWordFinder;
import org.sonar.api.Properties;
import org.sonar.api.Property;
import org.sonar.api.SonarPlugin;
import name.webdizz.sonar.grammar.rule.GrammarRulesDefinition;
import name.webdizz.sonar.grammar.sensor.GrammarIssuesSensor;

@Properties({
    @Property(
            key = GrammarPlugin.EXCLUSION,
            name = "Exclusion Property",
            description = "Defines resources to be excluded from analysis",
            defaultValue = ""),
    @Property(
            key = GrammarPlugin.INCLUSION,
            name = "Inclusion Property",
            description = "Defines resources to be included for analysis",
            defaultValue = ""),
    @Property(
            key = GrammarPlugin.DICTIONARY,
            name = "Dictionary path",
            description = "Defines resources to be included for analysis",
            defaultValue = "/dict/english.0"),
    @Property(
            key = GrammarPlugin.MIN_WORD_LENGTH,
            name = "Minimum word length",
            description = "Defines minimum word length to analyse",
            defaultValue = "4")})
public class GrammarPlugin extends SonarPlugin {

    public static final String EXCLUSION = "sonar.grammar.exclusion";
    public static final String INCLUSION = "sonar.grammar.inclusion";
    public static final String DICTIONARY = "sonar.grammar.dictionary";
    public static final String PLUGIN_NAME = "Sonar Grammar";
    public static final String MIN_WORD_LENGTH = "sonar.minimum.word.length";

    @Override
    public List getExtensions() {
        return Arrays
                .asList(// Definitions
                        GrammarRulesDefinition.class,
                        GrammarProfileDefinition.class,
                        // Metrics
                        GrammarMetrics.class,
                        // Sensor
                        GrammarIssuesSensor.class,
                        GrammarChecker.class,
                        GrammarDictionaryLoader.class,
                        JavaSourceCodeWordFinder.class
                );
    }
}
