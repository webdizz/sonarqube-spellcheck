package name.webdizz.sonar.grammar.rule;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.sonar.api.resources.Java;
import org.sonar.api.rules.Rule;
import org.sonar.api.rules.RulePriority;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;

@RunWith(MockitoJUnitRunner.class)
public class GrammarRuleRepositoryTest {

    private GrammarRuleRepository testingInstance = new GrammarRuleRepository();

    @Test
    public void shouldAppendGrammarRules() {
        assertNotNull("Created rules are null", testingInstance.createRules());
    }

    @Test
    public void shouldCreateDefaultSetOfRules() {
        assertFalse("Created rules list is empty", testingInstance.createRules().isEmpty());
    }

    @Test
    public void shouldCreateSpecificRules() {
        Rule actual = testingInstance.createRules().get(0);
        assertEquals("Key is incorrect", GrammarRuleRepository.REPOSITORY_KEY, actual.getRepositoryKey());
        assertEquals("Name is incorrect", "Sonar Grammar", actual.getName());
        assertEquals("Language is incorrect", Java.KEY, actual.getLanguage());
        assertEquals("Severity is incorrect", RulePriority.BLOCKER, actual.getSeverity());
        assertEquals("Status is incorrect", Rule.STATUS_READY, actual.getStatus());
    }
}
