package name.webdizz.sonar.grammar.rule;

import name.webdizz.sonar.grammar.PluginParameter;
import org.junit.Test;
import static org.mockito.Mockito.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.rule.RuleStatus;
import org.sonar.api.rule.Severity;
import org.sonar.api.server.rule.RulesDefinition;

public class GrammarRulesDefinitionTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(GrammarRulesDefinitionTest.class);

    private final RulesDefinition.Context context = mock(RulesDefinition.Context.class);
    private final RulesDefinition.NewRepository repository = prepareMockedRulesRepository();

    private final GrammarRulesDefinition instance = new GrammarRulesDefinition();

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
