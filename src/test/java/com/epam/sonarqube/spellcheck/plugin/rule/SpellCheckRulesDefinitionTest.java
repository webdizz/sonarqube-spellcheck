package com.epam.sonarqube.spellcheck.plugin.rule;

import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.rule.RuleStatus;
import org.sonar.api.rule.Severity;
import org.sonar.api.server.rule.RulesDefinition;

import com.epam.sonarqube.spellcheck.plugin.PluginParameter;

public class SpellCheckRulesDefinitionTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(SpellCheckRulesDefinitionTest.class);

    private final RulesDefinition.Context context = mock(RulesDefinition.Context.class);
    private final RulesDefinition.NewRepository repository = prepareMockedRulesRepository();

    private final SpellCheckRulesDefinition instance = new SpellCheckRulesDefinition();

    /**
     * Test of define method, of class GrammarRulesDefinition.
     */
    @Test
    public void testDefine() {
        LOGGER.info("Testing define");

        instance.define(context);

        // check behavior
        verify(context).createRepository(PluginParameter.REPOSITORY_KEY, PluginParameter.PROFILE_LANGUAGE);
        verify(repository).setName(PluginParameter.REPOSITORY_NAME);
        verify(repository).createRule(PluginParameter.SONAR_GRAMMAR_RULE_KEY);
        verify(repository).done();

        LOGGER.info("Done.");
    }

    private RulesDefinition.NewRepository prepareMockedRulesRepository() {
        final RulesDefinition.NewRepository mockedRulesRepository = mock(RulesDefinition.NewRepository.class);
        when(context.createRepository(PluginParameter.REPOSITORY_KEY, PluginParameter.PROFILE_LANGUAGE)).thenReturn(mockedRulesRepository);
        final RulesDefinition.NewRule rule = mock(RulesDefinition.NewRule.class);
        when(mockedRulesRepository.createRule(PluginParameter.SONAR_GRAMMAR_RULE_KEY)).thenReturn(rule);
        when(mockedRulesRepository.setName(PluginParameter.REPOSITORY_NAME)).thenReturn(mockedRulesRepository);
        when(rule.setInternalKey(PluginParameter.SONAR_GRAMMAR_RULE_KEY)).thenReturn(rule);
        when(rule.setName(anyString())).thenReturn(rule);
        when(rule.setHtmlDescription(PluginParameter.SONAR_GRAMMAR_RULE_DESCRIPTION)).thenReturn(rule);
        when(rule.setTags(PluginParameter.REPOSITORY_KEY)).thenReturn(rule);
        when(rule.setStatus(RuleStatus.READY)).thenReturn(rule);
        when(rule.setSeverity(Severity.INFO)).thenReturn(rule);
        return mockedRulesRepository;
    }

}
