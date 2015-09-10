package com.epam.sonarqube.spellcheck.plugin;

import org.sonar.api.CoreProperties;

/**
 * The pool of constants used in the spell-check-plugin
 */
public interface PluginParameter {
    /**
     * The name of sonar-plugin
     */
    String PLUGIN_NAME = "SpellCheck";

    /**
     * The name of repository for spell-check rules
     */
    String REPOSITORY_NAME = PLUGIN_NAME + " Repository";

    /**
     * The name of spell check profile
     */
    String PROFILE_NAME = PLUGIN_NAME + " Profile";

    /**
     * The language of spell check profile
     */
    String PROFILE_LANGUAGE = CoreProperties.CATEGORY_JAVA;

    /**
     * The key of spell-check-rules-repository
     */
    String REPOSITORY_KEY = "snrspl";

    /**
     * The name of spell-check-rule
     */
    String SONAR_SPELL_CHECK_RULE_KEY = "sonar_spell_check_rule";

    String SONAR_SPELL_CHECK_RULE_ATTRIBUTE = "type";

    String SONAR_SPELL_CHECK_RULE_MISSPELLED_WORD = "wrong-word";

    /**
     * The name of spell-check-rule
     */
    String SONAR_SPELL_CHECK_RULE_NAME = "Spell Check Rule";

    /**
     * The description of spell-check-rule
     */
    String SONAR_SPELL_CHECK_RULE_DESCRIPTION = "Analyses source code for english spell check issues.";

    /**
     * Error description
     */
    String ERROR_DESCRIPTION = "Invalid word is: \'";

    /**
     * Action name for adding to dictionary
     */
    String ADD_TO_DICT = "add-to-dict";

    /**
     * Add  to dictionary link caption
     */
    String SONAR_SPELL_CHECK_ISSUE_DATA_PROPERTY_KEY = "spell-check-issue-key";

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
    String EXCLUSION = "sonarqube-spellcheck.dictionary.exclusion";

    /**
     * list of words to be included to main dictionary
     **/
    String INCLUSION = "sonarqube-spellcheck.dictionary.inclusion";

    /**
     * path to main dictionary
     **/
    String DICTIONARY_PATH = "sonarqube-spellcheck.dictionary.path";

    /**
     * manual dictionary name
     */
    String ALTERNATIVE_DICTIONARY_PROPERTY_KEY = "sonarqube-spellcheck.alternative.dictionary";

    /**
     * the maximum cost of suggested spelling. Any suggestions that cost more are thrown away
     * integer greater than 1)
     */
    String SPELL_THRESHOLD = "sonarqube-spellcheck.spell.threshold.value";

    /**
     * minimum word length to be analyzed
     */
    String SPELL_MINIMUMWORDLENGTH = "sonarqube-spellcheck.spell.minimum.word.length";

    /**
     * words that are all upper case are not spell checked, example: "CIA" <br/>(boolean)
     */
    String SPELL_IGNOREUPPERCASE = "sonarqube-spellcheck.spell.ignore.upper.case";

    /**
     * words that have mixed case are not spell checked, example: "SpellChecker"<br/>(boolean)\
     */
    String SPELL_IGNOREMIXEDCASE = "sonarqube-spellcheck.spell.ignore.mixed.case";

    /**
     * words that look like an Internet address are not spell checked, example: "http://www.google.com" <br/>(boolean)
     */
    String SPELL_IGNOREINTERNETADDRESSES = "sonarqube-spellcheck.spell.ignore.internet.address";

    /**
     * words that have digits in them are not spell checked, example: "mach5" <br/>(boolean)
     */
    String SPELL_IGNOREDIGITWORDS = "sonarqube-spellcheck.spell.ignore.digit.words";

}
