package name.webdizz.sonar.grammar.rule;

import name.webdizz.sonar.grammar.PluginParameter;
import org.junit.Test;
import static org.mockito.Mockito.*;
import org.sonar.api.rule.RuleStatus;
import org.sonar.api.rule.Severity;
import org.sonar.api.server.rule.RulesDefinition;

/**
 *
 * @author Oleg_Sopilnyak1
 */
public class GrammarRulesDefinitionTest {
    
    public GrammarRulesDefinitionTest() {
    }

    /**
     * Test of define method, of class GrammarRulesDefinition.
     */
    @Test
    public void testDefine() {
        System.out.println("Testing define");
        RulesDefinition.Context context = mock(RulesDefinition.Context.class);
        RulesDefinition.NewRepository repository = mock(RulesDefinition.NewRepository.class);
        when(context.createRepository(PluginParameter.REPOSITORY_KEY, PluginParameter.PROFILE_LANGUAGE)).thenReturn(repository);
        RulesDefinition.NewRule rule = mock(RulesDefinition.NewRule.class);
        when(repository.createRule(PluginParameter.SONAR_GRAMMAR_RULE)).thenReturn(rule);
        when(repository.setName(PluginParameter.REPOSITORY_NAME)).thenReturn(repository);

        when(rule.setInternalKey(PluginParameter.SONAR_GRAMMAR_RULE)).thenReturn(rule);
        when(rule.setName(anyString())).thenReturn(rule);
        when(rule.setHtmlDescription(PluginParameter.SONAR_GRAMMAR_RULE_DESCRIPTION)).thenReturn(rule);
        when(rule.setTags(PluginParameter.REPOSITORY_KEY)).thenReturn(rule);
        when(rule.setStatus(RuleStatus.READY)).thenReturn(rule);
        when(rule.setSeverity(Severity.INFO)).thenReturn(rule);
        
        GrammarRulesDefinition instance = new GrammarRulesDefinition();
        instance.define(context);

        // check behavior
        verify(context).createRepository(PluginParameter.REPOSITORY_KEY, PluginParameter.PROFILE_LANGUAGE);
        verify(repository).setName(PluginParameter.REPOSITORY_NAME);
        verify(repository).createRule(PluginParameter.SONAR_GRAMMAR_RULE);
        verify(repository).done();
                
        System.out.println("Done.");
    }
    
}
