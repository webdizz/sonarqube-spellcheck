package name.webdizz.sonar.grammar.profile;

import name.webdizz.sonar.grammar.PluginParameter;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.profiles.RulesProfile;
import org.sonar.api.rules.Rule;
import org.sonar.api.rules.RuleFinder;
import org.sonar.api.utils.ValidationMessages;

public class GrammarProfileDefinitionTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(GrammarProfileDefinitionTest.class);

    private final RuleFinder ruleFinder = mock(RuleFinder.class);
    private final GrammarProfileDefinition instance = new GrammarProfileDefinition(ruleFinder);
    private final Rule rule = prepareMockedRule();

    /**
     * Test of createProfile method, of class GrammarProfileDefinition.
     */
    @Test
    public void testCreateProfile() {
        LOGGER.info("Testing createProfile");

        RulesProfile expected = RulesProfile.create(PluginParameter.PROFILE_NAME, PluginParameter.PROFILE_LANGUAGE);

        RulesProfile result = instance.createProfile(ValidationMessages.create());

        assertEquals(expected, result);
        LOGGER.info("Checking active-rules {}", result.getActiveRules());

        assertTrue(result.getActiveRules().size() > 0);
        assertEquals(rule, result.getActiveRules().get(0).getRule());
        assertEquals(rule, result.getActiveRule(rule).getRule());
        assertEquals(rule, result.getActiveRule(PluginParameter.REPOSITORY_KEY, PluginParameter.SONAR_GRAMMAR_RULE_KEY).getRule());

        LOGGER.info("Done.");
    }

    // private methods
    private Rule prepareMockedRule() {
        final Rule mockedRule = mock(Rule.class);
        when(mockedRule.getRepositoryKey()).thenReturn(PluginParameter.REPOSITORY_KEY);
        when(mockedRule.getKey()).thenReturn(PluginParameter.SONAR_GRAMMAR_RULE_KEY);
        when(mockedRule.isEnabled()).thenReturn(Boolean.TRUE);
        when(ruleFinder.findByKey(PluginParameter.REPOSITORY_KEY, PluginParameter.SONAR_GRAMMAR_RULE_KEY)).thenReturn(mockedRule);
        return mockedRule;
    }

}
