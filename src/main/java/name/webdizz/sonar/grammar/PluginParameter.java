package name.webdizz.sonar.grammar;

import org.sonar.api.CoreProperties;

/**
 * The pool of constants used in the grammar-plugin
 *
 * @author Oleg_Sopilnyak1
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
    String SONAR_GRAMMAR_RULE = "sonar_grammar_rule";

    /**
     * The description of grammar-rule
     */
    String SONAR_GRAMMAR_RULE_DESCRIPTION = "Analyses source code for english grammar issues.";
}
