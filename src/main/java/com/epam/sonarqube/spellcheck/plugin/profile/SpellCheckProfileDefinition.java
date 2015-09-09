package com.epam.sonarqube.spellcheck.plugin.profile;

import com.epam.sonarqube.spellcheck.plugin.PluginParameter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.profiles.ProfileDefinition;
import org.sonar.api.profiles.RulesProfile;
import org.sonar.api.rules.Rule;
import org.sonar.api.rules.RuleFinder;
import org.sonar.api.utils.ValidationMessages;
import org.sonar.api.rules.ActiveRule;

public class SpellCheckProfileDefinition extends ProfileDefinition {

    private static final Logger LOGGER = LoggerFactory.getLogger(SpellCheckProfileDefinition.class);

    private final RuleFinder ruleFinder;

    public SpellCheckProfileDefinition(final RuleFinder ruleFinder) {
        this.ruleFinder = ruleFinder;
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Created bean of rules profile definition.");
        }
    }

    @Override
    public RulesProfile createProfile(final ValidationMessages validationMessages) {
        final RulesProfile profile = RulesProfile.create(PluginParameter.PROFILE_NAME, PluginParameter.PROFILE_LANGUAGE);

        final Rule rule = ruleFinder.findByKey(PluginParameter.REPOSITORY_KEY, PluginParameter.SONAR_GRAMMAR_RULE_KEY);
        if (LOGGER.isDebugEnabled()) {
            final Object[] arguments = new Object[]{PluginParameter.REPOSITORY_KEY, PluginParameter.SONAR_GRAMMAR_RULE_KEY, rule};
            LOGGER.debug("For repository_key {} and rule_key {} found {}", arguments);
        }

        final ActiveRule activeRule = profile.activateRule(rule, null);

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(" For rule {} activated {}", rule, activeRule);
        }
        return profile;
    }

}
