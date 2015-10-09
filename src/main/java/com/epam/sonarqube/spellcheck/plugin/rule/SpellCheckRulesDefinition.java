package com.epam.sonarqube.spellcheck.plugin.rule;

import com.epam.sonarqube.spellcheck.plugin.PluginParameter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.BatchExtension;
import org.sonar.api.rule.RuleStatus;
import org.sonar.api.rule.Severity;
import org.sonar.api.server.rule.RulesDefinition;

/**
 * Declare rule meta-data in server repository of rules.
 */
public class SpellCheckRulesDefinition implements RulesDefinition, BatchExtension {

    private static final Logger LOGGER = LoggerFactory.getLogger(SpellCheckRulesDefinition.class);

    public SpellCheckRulesDefinition() {
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


        final NewRule grammarRule = repository
                .createRule(PluginParameter.SONAR_SPELL_CHECK_RULE_KEY)
                .setName(PluginParameter.SONAR_SPELL_CHECK_RULE_NAME)
                .setHtmlDescription(PluginParameter.SONAR_SPELL_CHECK_RULE_DESCRIPTION)
                .setTags(PluginParameter.REPOSITORY_KEY)
                .setStatus(RuleStatus.READY)
                .setInternalKey(PluginParameter.SONAR_SPELL_CHECK_RULE_KEY)
                .setSeverity(Severity.INFO);


        if (LOGGER.isDebugEnabled()){
            LOGGER.debug("Created new rule:{}", grammarRule);
        }
        repository.done();
    }

}
