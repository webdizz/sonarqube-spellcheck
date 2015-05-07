package name.webdizz.sonar.grammar;

import java.util.Arrays;
import java.util.List;

import org.sonar.api.PropertyType;
import org.sonar.api.SonarPlugin;
import org.sonar.api.config.PropertyDefinition;
import name.webdizz.sonar.grammar.issue.tracking.GrammarActionDefinition;
import name.webdizz.sonar.grammar.issue.tracking.LinkFunction;
import name.webdizz.sonar.grammar.profile.GrammarProfileDefinition;
import name.webdizz.sonar.grammar.rule.GrammarRulesDefinition;
import name.webdizz.sonar.grammar.sensor.GrammarIssuesSensor;
import name.webdizz.sonar.grammar.spellcheck.GrammarChecker;
import name.webdizz.sonar.grammar.spellcheck.GrammarDictionaryLoader;
import name.webdizz.sonar.grammar.spellcheck.JavaSourceCodeWordFinder;
import name.webdizz.sonar.grammar.spellcheck.SpellCheckerFactory;
import name.webdizz.sonar.grammar.utils.SpellCheckerUtil;


public class GrammarPlugin extends SonarPlugin {



    @Override
    public List getExtensions() {
        return Arrays
                .asList(// Definitions
                        GrammarRulesDefinition.class,
                        GrammarActionDefinition.class,
                        GrammarProfileDefinition.class,
                        // Metrics
                        GrammarMetrics.class,
                        // Sensors
                        GrammarIssuesSensor.class,
                        //Issue review
                        LinkFunction.class,
                        //Instantiated by IoC as used injection
                        JavaSourceCodeWordFinder.class,
                        GrammarChecker.class,
                        GrammarDictionaryLoader.class,
                        SpellCheckerFactory.class,
                        SpellCheckerUtil.class,

                        PropertyDefinition.builder(PluginParameter.SPELL_MINIMUMWORDLENGTH).name("Minimum word length")
                                .type(PropertyType.INTEGER).description("Defines minimum word length to analyse")
                                .defaultValue("3").subCategory("Spell Checker").build(),
                        PropertyDefinition.builder(PluginParameter.SPELL_IGNOREMIXEDCASE).name("Ignore mixed case")
                                .type(PropertyType.BOOLEAN).description("Words that have mixed case are not spell " +
                                "checked, example: 'SpellChecker'").defaultValue("false").subCategory("Spell Checker")
                                .build(),
                        PropertyDefinition.builder(PluginParameter.SPELL_IGNOREUPPERCASE).name("Ignore uppercase")
                                .type(PropertyType.BOOLEAN).description("Words that are all upper case are not spell " +
                                "checked, example: 'CIA'").defaultValue("true").subCategory("Spell Checker").build(),
                        PropertyDefinition.builder(PluginParameter.SPELL_IGNOREDIGITWORDS)
                                .name("Ignore words with digits").type(PropertyType.BOOLEAN)
                                .description("Words that have digits in them are not spell checked, example: 'mach5'")
                                .defaultValue("false").subCategory("Spell Checker").build(),
                        PropertyDefinition.builder(PluginParameter.SPELL_IGNOREINTERNETADDRESSES)
                                .name("Ignore words like internet address").type(PropertyType.BOOLEAN)
                                .description("Words that look like an Internet address are not spell checked, example:" +
                                        " 'http://www.google.com'").defaultValue("true").subCategory("Spell Checker")
                                .build(),
                        PropertyDefinition.builder(PluginParameter.SPELL_THRESHOLD).name("Threshold value")
                                .type(PropertyType.INTEGER).description("The maximum cost of suggested spelling. Any " +
                                "suggestions that cost more are thrown away").defaultValue("1")
                                .subCategory("Spell Checker").build(),


                        PropertyDefinition.builder(PluginParameter.EXCLUSION).name("Exclusion Property")
                                .type(PropertyType.STRING).description("Defines resources to be excluded from analysis")
                                .subCategory("Dictionary").defaultValue("").build(),
                        PropertyDefinition.builder(PluginParameter.INCLUSION).name("Inclusion Property")
                                .type(PropertyType.STRING).description("Defines resources to be included for analysis")
                                .subCategory("Dictionary").defaultValue("").build(),
                        PropertyDefinition.builder(PluginParameter.DICTIONARY_PATH).name("Default dictionary path")
                                .type(PropertyType.STRING).description("Defines default dictionary path")
                                .defaultValue("/dict/english.0").subCategory("Dictionary").build(),
                        PropertyDefinition.builder(PluginParameter.ALTERNATIVE_DICTIONARY_PROPERTY_KEY)
                                .name("Alternative dictionary").type(PropertyType.STRING).description("Alternative " +
                                "dictionary").defaultValue("").subCategory("Dictionary").build()
                );
    }
}
