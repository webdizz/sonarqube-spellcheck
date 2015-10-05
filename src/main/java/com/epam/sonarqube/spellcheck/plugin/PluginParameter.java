package com.epam.sonarqube.spellcheck.plugin;

import org.sonar.api.CoreProperties;

/**
 * The pool of constants used in the spell-check-plugin
 */
public abstract class PluginParameter {

    /**
     * The name of sonar-plugin
     */
    public static final String PLUGIN_NAME = "SpellCheck";

    /**
     * The name of repository for spell-check rules
     */
    public static final String REPOSITORY_NAME = PLUGIN_NAME + " Repository";

    /**
     * The name of spell check profile
     */
    public static final String PROFILE_NAME = PLUGIN_NAME + " Profile";

    /**
     * The language of spell check profile
     */
    public static final String PROFILE_LANGUAGE = CoreProperties.CATEGORY_JAVA;

    /**
     * The key of spell-check-rules-repository
     */
    public static final String REPOSITORY_KEY = "snrspl";

    /**
     * The name of spell-check-rule
     */
    public static final String SONAR_SPELL_CHECK_RULE_KEY = "sonar_spell_check_rule";

    public static final String SONAR_SPELL_CHECK_RULE_ATTRIBUTE = "type";

    public static final String SONAR_SPELL_CHECK_RULE_MISSPELLED_WORD = "wrong-word";

    /**
     * The name of spell-check-rule
     */
    public static final String SONAR_SPELL_CHECK_RULE_NAME = "Spell Check Rule";

    /**
     * The description of spell-check-rule
     */
    public static final String SONAR_SPELL_CHECK_RULE_DESCRIPTION = "Analyses source code for english spell check issues.";

    /**
     * Error description
     */
    public static final String ERROR_DESCRIPTION = "Invalid word is: \'";

    /**
     * Action name for adding to dictionary
     */
    public static final String ADD_TO_DICT = "add-to-dict";

    /**
     * Add  to dictionary link caption
     */
    public static final String SONAR_SPELL_CHECK_ISSUE_DATA_PROPERTY_KEY = "spell-check-issue-key";

    /**
     * Separator for read and split alternate dictionary.
     * Whitespace in each word will be trimmed, during reading.
     */
    public static final String SEPARATOR_CHAR = ",";
    
    /**
     * Separator for store alternate dictionary
     */
    public static final String EXTENDED_SEPARATOR = SEPARATOR_CHAR + " ";
    

    /**
     * Parameters for set cost of spelling
     */

    /**
     * exclusion word list
     **/
    public static final String EXCLUSION = "sonarqube-spellcheck.dictionary.exclusion";

    /**
     * list of words to be included to main dictionary
     **/
    public static final String INCLUSION = "sonarqube-spellcheck.dictionary.inclusion";

    /**
     * path to main dictionary
     **/
    public static final String DICTIONARY_PATH = "sonarqube-spellcheck.dictionary.path";

    /**
     * manual dictionary name
     */
    public static final String ALTERNATIVE_DICTIONARY_PROPERTY_KEY = "sonarqube-spellcheck.alternative.dictionary";

    /**
     * the maximum cost of suggested spelling. Any suggestions that cost more are thrown away
     * integer greater than 1)
     */
    public static final String SPELL_THRESHOLD = "sonarqube-spellcheck.spell.threshold.value";

    /**
     * minimum word length to be analyzed
     */
    public static final String SPELL_MINIMUMWORDLENGTH = "sonarqube-spellcheck.spell.minimum.word.length";

    /**
     * words that are all upper case are not spell checked, example: "CIA" <br/>(boolean)
     */
    public static final String SPELL_IGNOREUPPERCASE = "sonarqube-spellcheck.spell.ignore.upper.case";

    /**
     * words that have mixed case are not spell checked, example: "SpellChecker"<br/>(boolean)\
     */
    public static final String SPELL_IGNOREMIXEDCASE = "sonarqube-spellcheck.spell.ignore.mixed.case";

    /**
     * words that look like an Internet address are not spell checked, example: "http://www.google.com" <br/>(boolean)
     */
    public static final String SPELL_IGNOREINTERNETADDRESSES = "sonarqube-spellcheck.spell.ignore.internet.address";

    /**
     * words that have digits in them are not spell checked, example: "match5" <br/>(boolean)
     */
    public static final String SPELL_IGNOREDIGITWORDS = "sonarqube-spellcheck.spell.ignore.digit.words";

    private PluginParameter() {
    }
}
