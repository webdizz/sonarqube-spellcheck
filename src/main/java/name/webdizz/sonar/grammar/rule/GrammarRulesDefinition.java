package name.webdizz.sonar.grammar.rule;

import name.webdizz.sonar.grammar.PluginParameter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.BatchExtension;
import org.sonar.api.rule.RuleStatus;
import org.sonar.api.rule.Severity;
import org.sonar.api.server.rule.RulesDefinition;

/**
 * Declare rule meta-data in server repository of rules.
 */
public class GrammarRulesDefinition implements RulesDefinition, BatchExtension {

    private static final Logger LOGGER = LoggerFactory.getLogger(GrammarRulesDefinition.class);

    public GrammarRulesDefinition() {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Rules definition bean is creating.");
        }
    }

    @Override
    public void define(Context context) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Making sonar-rules for the Grammar Plugin using {}", context.toString());
        }
        final NewRepository repository = context
                .createRepository(PluginParameter.REPOSITORY_KEY, PluginParameter.PROFILE_LANGUAGE)
                .setName(PluginParameter.REPOSITORY_NAME);
        
        final NewRule grammarRule = repository.createRule(PluginParameter.SONAR_GRAMMAR_RULE_KEY)
                .setInternalKey(PluginParameter.SONAR_GRAMMAR_RULE_KEY)
                .setName(PluginParameter.SONAR_GRAMMAR_RULE_NAME)
                .setHtmlDescription(PluginParameter.SONAR_GRAMMAR_RULE_DESCRIPTION)
                .setTags(PluginParameter.REPOSITORY_KEY)
                .setStatus(RuleStatus.READY)
                .setSeverity(Severity.INFO);
        
        if (LOGGER.isDebugEnabled()){
            LOGGER.debug("Created new rule:{}", grammarRule);
        }
        repository.done();
    }

}
