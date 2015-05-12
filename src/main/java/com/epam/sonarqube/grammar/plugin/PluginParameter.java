package com.epam.sonarqube.grammar.plugin;

import org.sonar.api.CoreProperties;

/**
 * The pool of constants used in the grammar-plugin
 */
public interface PluginParameter {
    /**
     * The name of sonar-plugin
     */
    String PLUGIN_NAME = "Sonar Grammar";
    /**
     * The name of repository for grammar rules
     */
    String REPOSITORY_NAME = PLUGIN_NAME + " Repository";

    /**
     * The name of grammar profile
     */
    String PROFILE_NAME = PLUGIN_NAME + " Profile";
    /**
     * The language of grammar profile
     */
    String PROFILE_LANGUAGE = CoreProperties.CATEGORY_JAVA;
    /**
     * The key of grammar-rules-repository
     */
    String REPOSITORY_KEY = "snrgrm";
    /**
     * The name of grammar-rule
     */
    String SONAR_GRAMMAR_RULE_KEY = "sonar_grammar_rule";

    /**
     * The name of grammar-rule
     */
    String SONAR_GRAMMAR_RULE_NAME = "Grammar Rule";
    /**
     * The description of grammar-rule
     */
    String SONAR_GRAMMAR_RULE_DESCRIPTION = "Analyses source code for english grammar issues.";

    /**
     * Error description
     */

    String ERROR_DESCRIPTION = "Invalid word is : \'";

    /**
     * Action name for adding to dictionary
     */
    String ADD_TO_DICT = "add-to-dict";

    /**
     * Add  to dictionary link caption
     */
    String SONAR_GRAMMAR_ISSUE_DATA_PROPERTY_KEY = "grammar-issue-key";

    /**
     * Separator for store alternate dictionary
     */
    String SEPARATOR_CHAR = ",";
    /**
     * Parameters for set cost of spelling
     */

    /**
     * exclusion word list
     **/
    String EXCLUSION = "sonarqube-grammar.dictionary.exclusion";

    /**
     * list of words to be included to main dictionary
     **/
    String INCLUSION = "sonarqube-grammar.dictionary.inclusion";

    /**
     * path to main dictionary
     **/
    String DICTIONARY_PATH = "sonarqube-grammar.dictionary.path";

    /**
     * manual dictionary name
     */
    String ALTERNATIVE_DICTIONARY_PROPERTY_KEY = "sonarqube-grammar.alternative.dictionary.path";

    /** the maximum cost of suggested spelling. Any suggestions that cost more are thrown away
     * integer greater than 1)
     */
    String SPELL_THRESHOLD = "sonarqube-grammar.spell.threshold.value";

    /**
     * minimum word length to be analyzed
     */
    String SPELL_MINIMUMWORDLENGTH = "sonarqube-grammar.spell.minimum.word.length";

    /**
     * words that are all upper case are not spell checked, example: "CIA" <br/>(boolean)
     */
    String SPELL_IGNOREUPPERCASE = "sonarqube-grammar.spell.ignore.upper.case";

    /**
     * words that have mixed case are not spell checked, example: "SpellChecker"<br/>(boolean)\
     */
    String SPELL_IGNOREMIXEDCASE = "sonarqube-grammar.spell.ignore.mixed.case";

    /**
     * words that look like an Internet address are not spell checked, example: "http://www.google.com" <br/>(boolean)
     */
    String SPELL_IGNOREINTERNETADDRESSES = "sonarqube-grammar.spell.ignore.internet.address";

    /**
     * words that have digits in them are not spell checked, example: "mach5" <br/>(boolean)
     */
    String SPELL_IGNOREDIGITWORDS = "sonarqube-grammar.spell.ignore.digit.words";

    /**
     * I don't know what this does. It doesn't seem to be used <br/>(boolean)
     */
    String SPELL_IGNOREMULTIPLEWORDS = "sonarqube-grammar.spell.ignore.multiple.words";

    /**
     * the first word of a sentence is expected to start with an upper case letter <br/>(boolean)
     */
    String SPELL_IGNORESENTENCECAPITALIZATION = "sonarqube-grammar.spell.ignore.sentence.capitalization";

    /**
     * Whether to ignore words that are a single letter (common in programming)
     */
    String SPELL_IGNORESINGLELETTERS = "sonarqube-grammar.spell.ignore.single.letters";

}
