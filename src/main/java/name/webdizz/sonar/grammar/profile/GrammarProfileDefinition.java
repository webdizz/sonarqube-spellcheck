package name.webdizz.sonar.grammar.profile;

import name.webdizz.sonar.grammar.PluginParameter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.profiles.ProfileDefinition;
import org.sonar.api.profiles.RulesProfile;
import org.sonar.api.rules.Rule;
import org.sonar.api.rules.RuleFinder;
import org.sonar.api.utils.ValidationMessages;
import org.sonar.api.rules.ActiveRule;

public class GrammarProfileDefinition extends ProfileDefinition {

    private static final Logger LOGGER = LoggerFactory.getLogger(GrammarProfileDefinition.class);

    private final RuleFinder ruleFinder;

    public GrammarProfileDefinition(final RuleFinder ruleFinder) {
        this.ruleFinder = ruleFinder;
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Created bean of rules profile definition.");
        }
    }

    @Override
    public RulesProfile createProfile(final ValidationMessages validationMessages) {
        final RulesProfile profile = RulesProfile.create(PluginParameter.PROFILE_NAME, PluginParameter.PROFILE_LANGUAGE);

        final Rule rule = ruleFinder.findByKey(PluginParameter.REPOSITORY_KEY, PluginParameter.SONAR_GRAMMAR_RULE);
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("For repository_key {} and rule_key {} found {}", PluginParameter.REPOSITORY_KEY, PluginParameter.SONAR_GRAMMAR_RULE, rule);
        }

        final ActiveRule activeRulev = profile.activateRule(rule, null);

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(" For rule {} activated {}", rule, activeRulev);
        }
        return profile;
    }

}
