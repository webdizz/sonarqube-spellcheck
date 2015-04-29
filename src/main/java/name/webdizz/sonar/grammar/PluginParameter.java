package name.webdizz.sonar.grammar;

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
     * manual dictionary name
     */
    String ALTERNATIVE_DICTIONARY_PROPERTY_KEY = "sonar.alternative.dictionary";
    /**
     * Separator for store alternate dictionary
     */
    String SEPARATOR_CHAR = ",";
    /**
     * Parameters for set cost of spelling
     */
    int SPELL_THRESHOLD_VALUE = 1;
    String SPELL_THRESHOLD = "SPELL_THRESHOLD";
}
