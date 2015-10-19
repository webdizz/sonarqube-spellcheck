package com.epam.sonarqube.spellcheck.plugin;

import java.util.Arrays;
import java.util.List;

import com.epam.sonarqube.spellcheck.plugin.spellcheck.*;
import org.sonar.api.PropertyType;
import org.sonar.api.SonarPlugin;
import org.sonar.api.config.PropertyDefinition;

import com.epam.sonarqube.spellcheck.plugin.decorator.SpellCheckMisspellingDecorator;
import com.epam.sonarqube.spellcheck.plugin.issue.tracking.AddWordFromIssueFunction;
import com.epam.sonarqube.spellcheck.plugin.issue.tracking.SpellCheckActionDefinition;
import com.epam.sonarqube.spellcheck.plugin.metric.SpellCheckMetrics;
import com.epam.sonarqube.spellcheck.plugin.rule.SpellCheckRulesDefinition;
import com.epam.sonarqube.spellcheck.plugin.sensor.SpellCheckIssuesSensor;


public class SpellCheckPlugin extends SonarPlugin {

    private static final String SPELL_CHECKER_CATEGORY = "Spell Checker";
    private static final String DICTIONARY_CATEGORY = "Dictionary";

    @Override
    public List getExtensions() {
        return Arrays
                .asList(// Definitions
                        SpellCheckRulesDefinition.class,
                        SpellCheckActionDefinition.class,
                        // Metrics
                        SpellCheckMetrics.class,
                        // Sensors
                        SpellCheckIssuesSensor.class,
                        //Decorators
                        SpellCheckMisspellingDecorator.class,
                        //Issue review
                        AddWordFromIssueFunction.class,
                        //Instantiated by IoC as used injection
                        JavaSourceCodeWordFinder.class,
                        SpellChecker.class,
                        SpellDictionaryLoader.class,
                        SpellCheckerFactory.class,
                        SpellCheckGlobalPropertyHandler.class,

                        PropertyDefinition.builder(PluginParameter.SPELL_MINIMUM_WORD_LENGTH).name("Minimum word length")
                                .type(PropertyType.INTEGER).description("Defines minimum word length to analyse")
                                .defaultValue("3").subCategory(SPELL_CHECKER_CATEGORY).build(),

                        PropertyDefinition.builder(PluginParameter.URL_DICTIONARY_TIMEOUT).name("URL dictionary load timeout")
                                .type(PropertyType.INTEGER).description("Defines timeout (in ms) for dictionary load waiting")
                                .defaultValue("5000").subCategory(DICTIONARY_CATEGORY).build(),

                        PropertyDefinition.builder(PluginParameter.SPELL_IGNORE_UPPERCASE).name("Ignore uppercase")
                                .type(PropertyType.BOOLEAN).description("Words that are all upper case are not spell checked, example: 'CIA'")
                                .defaultValue("true").subCategory(SPELL_CHECKER_CATEGORY).build(),
                                
                        PropertyDefinition.builder(PluginParameter.DICTIONARY_PATH).name("Default dictionary path")
                                .type(PropertyType.STRING).description("Defines default dictionary path")
                                .defaultValue("/dict/english.0").subCategory(DICTIONARY_CATEGORY).build(),

                        PropertyDefinition.builder(PluginParameter.URL_DICTIONARY_PATH).name("URL dictionary path")
                                .type(PropertyType.STRING).description("Defines url dictionary path")
                                .subCategory(DICTIONARY_CATEGORY)
                                .build(),

                        PropertyDefinition.builder(PluginParameter.ALTERNATIVE_DICTIONARY_PROPERTY_KEY)
                                .name("Alternative dictionary").type(PropertyType.STRING)
                                .description("The list of comma separated words to compile alternative  dictionary specific to your installation")
                                .type(PropertyType.TEXT).defaultValue("").subCategory(DICTIONARY_CATEGORY).build()
                );
    }
}
