package name.webdizz.sonar.grammar.profile;

import name.webdizz.sonar.grammar.PluginParameter;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import org.sonar.api.profiles.RulesProfile;
import org.sonar.api.rules.Rule;
import org.sonar.api.rules.RuleFinder;
import org.sonar.api.utils.ValidationMessages;

/**
 *
 * @author Oleg_Sopilnyak1
 */
//@RunWith(PowerMockRunner.class)
//@PrepareForTest(RulesProfile.class)
public class GrammarProfileDefinitionTest {

    private final RuleFinder ruleFinder = mock(RuleFinder.class);

    /**
     * Test of createProfile method, of class GrammarProfileDefinition.
     */
    @Test
    public void testCreateProfile() {
        System.out.println("Testing createProfile");
        ValidationMessages validationMessages = null;
        GrammarProfileDefinition instance = new GrammarProfileDefinition(ruleFinder);
        RulesProfile profile = RulesProfile.create(PluginParameter.PROFILE_NAME, PluginParameter.PROFILE_LANGUAGE);
        Rule rule = mock(Rule.class);
        when(rule.getRepositoryKey()).thenReturn(PluginParameter.REPOSITORY_KEY);
        when(rule.getKey()).thenReturn(PluginParameter.SONAR_GRAMMAR_RULE);
        when(rule.isEnabled()).thenReturn(Boolean.TRUE);
        when(ruleFinder.findByKey(PluginParameter.REPOSITORY_KEY, PluginParameter.SONAR_GRAMMAR_RULE)).thenReturn(rule);

        RulesProfile result = instance.createProfile(validationMessages);
        
        assertEquals(profile, result);
        assertTrue(result.getActiveRules().size() > 0);
        assertEquals(rule, result.getActiveRules().get(0).getRule());
        assertEquals(rule, result.getActiveRule(rule).getRule());
        assertEquals(rule, result.getActiveRule(PluginParameter.REPOSITORY_KEY, PluginParameter.SONAR_GRAMMAR_RULE).getRule());
        

        System.out.println("Done.");
    }

}
