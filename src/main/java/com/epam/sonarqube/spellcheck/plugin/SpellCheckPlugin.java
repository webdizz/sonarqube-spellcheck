package com.epam.sonarqube.spellcheck.plugin;

import java.util.Arrays;
import java.util.List;

import org.sonar.api.PropertyType;
import org.sonar.api.SonarPlugin;
import org.sonar.api.config.PropertyDefinition;

import com.epam.sonarqube.spellcheck.plugin.issue.tracking.AddWordFromIssueFunction;
import com.epam.sonarqube.spellcheck.plugin.issue.tracking.SpellCheckActionDefinition;
import com.epam.sonarqube.spellcheck.plugin.profile.SpellCheckProfileDefinition;
import com.epam.sonarqube.spellcheck.plugin.rule.SpellCheckRulesDefinition;
import com.epam.sonarqube.spellcheck.plugin.sensor.SpellCheckIssuesSensor;
import com.epam.sonarqube.spellcheck.plugin.spellcheck.GrammarDictionaryLoader;
import com.epam.sonarqube.spellcheck.plugin.spellcheck.JavaSourceCodeWordFinder;
import com.epam.sonarqube.spellcheck.plugin.spellcheck.SpellChecker;
import com.epam.sonarqube.spellcheck.plugin.spellcheck.SpellCheckerFactory;


public class SpellCheckPlugin extends SonarPlugin {

    private static final String SPELL_CHECKER_CATEGORY = "Spell Checker";
    private static final String DICTIONARY_CATEGORY = "Dictionary";

    @Override
    public List getExtensions() {
        return Arrays
                .asList(// Definitions
                        SpellCheckRulesDefinition.class,
                        SpellCheckActionDefinition.class,
                        SpellCheckProfileDefinition.class,
                        // Metrics
                        SpellCheckMetrics.class,
                        // Sensors
                        SpellCheckIssuesSensor.class,
                        //Issue review
                        AddWordFromIssueFunction.class,
                        //Instantiated by IoC as used injection
                        JavaSourceCodeWordFinder.class,
                        SpellChecker.class,
                        GrammarDictionaryLoader.class,
                        SpellCheckerFactory.class,

                        PropertyDefinition.builder(PluginParameter.SPELL_MINIMUMWORDLENGTH).name("Minimum word length")
                                .type(PropertyType.INTEGER).description("Defines minimum word length to analyse")
                                .defaultValue("3").subCategory(SPELL_CHECKER_CATEGORY).build(),

                        PropertyDefinition.builder(PluginParameter.SPELL_IGNOREUPPERCASE).name("Ignore uppercase")
                                .type(PropertyType.BOOLEAN).description("Words that are all upper case are not spell checked, example: 'CIA'")
                                .defaultValue("true").subCategory(SPELL_CHECKER_CATEGORY).build(),
                                
                        PropertyDefinition.builder(PluginParameter.SPELL_THRESHOLD).name("Threshold value")
                                .type(PropertyType.INTEGER).description("The maximum cost of suggested spelling. Any suggestions that cost more are thrown away")
                                .defaultValue("1").subCategory(SPELL_CHECKER_CATEGORY).build(),

                        PropertyDefinition.builder(PluginParameter.EXCLUSION).name("Exclusions")
                                .type(PropertyType.STRING).description("Defines resources to be excluded from analysis")
                                .subCategory(DICTIONARY_CATEGORY).defaultValue("").build(),

                        PropertyDefinition.builder(PluginParameter.INCLUSION).name("Inclusions")
                                .type(PropertyType.STRING).description("Defines resources to be included for analysis")
                                .subCategory(DICTIONARY_CATEGORY).defaultValue("").build(),

                        PropertyDefinition.builder(PluginParameter.DICTIONARY_PATH).name("Default dictionary path")
                                .type(PropertyType.STRING).description("Defines default dictionary path")
                                .defaultValue("/dict/english.0").subCategory(DICTIONARY_CATEGORY).build(),

                        PropertyDefinition.builder(PluginParameter.ALTERNATIVE_DICTIONARY_PROPERTY_KEY)
                                .name("Alternative dictionary").type(PropertyType.STRING)
                                .description("The list of comma separated words to compile alternative  dictionary specific to your installation")
                                .type(PropertyType.TEXT).defaultValue("").subCategory(DICTIONARY_CATEGORY).build()
                );
    }
}
