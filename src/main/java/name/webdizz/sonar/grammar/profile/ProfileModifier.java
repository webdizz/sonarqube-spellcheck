package name.webdizz.sonar.grammar.profile;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.profiles.ProfileDefinition;
import org.sonar.api.profiles.RulesProfile;
import org.sonar.api.resources.Java;
import org.sonar.api.rules.Rule;
import org.sonar.api.rules.RuleFinder;
import org.sonar.api.utils.ValidationMessages;
import name.webdizz.sonar.grammar.rule.RuleFinderHelper;

public class ProfileModifier extends ProfileDefinition {

    private final static Logger LOGGER = LoggerFactory.getLogger(ProfileModifier.class);

    private RuleFinder ruleFinder;

    public ProfileModifier(final RuleFinder ruleFinder) {
        this.ruleFinder = ruleFinder;
    }

    @Override
    public RulesProfile createProfile(final ValidationMessages validationMessages) {
        String profileName = RulesProfile.SONAR_WAY_NAME;
        return activateRuleInProfile(profileName);
    }

    private RulesProfile activateRuleInProfile(final String profileName) {
        RulesProfile profile = RulesProfile.create();
        Rule rule = RuleFinderHelper.findGrammarRule(ruleFinder);
        profile.activateRule(rule, rule.getSeverity());
        profile.setLanguage(Java.KEY);
        profile.setName(profileName);
        LOGGER.info("Modifying a rules profile: '{}' to activate rule: '{}'", profile, rule);
        return profile;
    }
}
