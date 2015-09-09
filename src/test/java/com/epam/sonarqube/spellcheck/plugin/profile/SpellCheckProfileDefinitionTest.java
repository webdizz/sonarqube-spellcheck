package com.epam.sonarqube.spellcheck.plugin.profile;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.sonar.api.profiles.RulesProfile;
import org.sonar.api.rules.Rule;
import org.sonar.api.rules.RuleFinder;
import org.sonar.api.utils.ValidationMessages;

import com.epam.sonarqube.spellcheck.plugin.PluginParameter;

public class SpellCheckProfileDefinitionTest {


    private final RuleFinder ruleFinder = mock(RuleFinder.class);
    private final SpellCheckProfileDefinition instance = new SpellCheckProfileDefinition(ruleFinder);
    private final Rule rule = prepareMockedRule();

    /**
     * Test of createProfile method, of class GrammarProfileDefinition.
     */
    @Test
    public void shouldCreateProfile() {

        RulesProfile expected = RulesProfile.create(PluginParameter.PROFILE_NAME, PluginParameter.PROFILE_LANGUAGE);
        RulesProfile result = instance.createProfile(ValidationMessages.create());

        assertEquals(expected, result);
    }

    @Test
    public void shouldCreateActiveRules(){

        RulesProfile result = instance.createProfile(ValidationMessages.create());

        assertEquals(rule, result.getActiveRules().get(0).getRule());
        assertEquals(rule, result.getActiveRule(rule).getRule());
    }

    @Test
    public void shouldGetActiveRuleWhenProfileCreatedAndActiveRulesNotEmpty(){

        RulesProfile result = instance.createProfile(ValidationMessages.create());

        assertTrue(!result.getActiveRules().isEmpty());
        assertEquals(rule, result.getActiveRule(PluginParameter.REPOSITORY_KEY, PluginParameter.SONAR_GRAMMAR_RULE_KEY).getRule());
    }


    private Rule prepareMockedRule() {
        final Rule mockedRule = mock(Rule.class);
        when(mockedRule.getRepositoryKey()).thenReturn(PluginParameter.REPOSITORY_KEY);
        when(mockedRule.getKey()).thenReturn(PluginParameter.SONAR_GRAMMAR_RULE_KEY);
        when(mockedRule.isEnabled()).thenReturn(Boolean.TRUE);
        when(ruleFinder.findByKey(PluginParameter.REPOSITORY_KEY, PluginParameter.SONAR_GRAMMAR_RULE_KEY)).thenReturn(mockedRule);
        return mockedRule;
    }

}
