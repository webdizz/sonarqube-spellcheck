package name.webdizz.sonar.grammar.spellcheck;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.sonar.api.resources.JavaFile;
import org.sonar.api.rules.Rule;
import org.sonar.api.rules.Violation;

import com.google.common.collect.Lists;
import com.swabunga.spell.event.SpellCheckEvent;

@RunWith(MockitoJUnitRunner.class)
public class GrammarSpellCheckListenerTest {

    @org.junit.Rule
    public org.junit.rules.ExpectedException thrown = ExpectedException.none();

    @Mock
    private SpellCheckEvent event;
    private List<Violation> violations;
    private GrammarSpellCheckListener.Builder builder;

    @Before
    public void setUp() {
        violations = new ArrayList<Violation>();
        builder = createBuilder();
    }

    @Test
    public void shouldHandleSpellingError() {
        createFullListener().spellingError(event);
        assertFalse("Violations were not created", violations.isEmpty());
    }

    @Test
    public void shouldCreateViolationWithSuggestion() {
        when(event.getSuggestions()).thenReturn(Lists.newArrayList("Some suggestion"));
        createFullListener().spellingError(event);
        Violation violation = violations.get(0);
        assertTrue("Violation was created without suggestion", violation.getMessage().contains("Suggestions:"));
    }

    @Test
    public void shouldCreateCorrectViolation() {
        createFullListener().spellingError(event);
        Violation violation = violations.get(0);
        assertNotNull("Violation created without message", violation.getMessage());
        assertTrue("Violation was created with wrong line number", violation.getLineId() > 0);
        assertNotNull("Violation created without rule", violation.getRule());
        assertNotNull("Violation created without resource", violation.getResource());
    }

    @Test
    public void shouldValidateListenerIsCreatedWithLineNumber() {
        expectProperException("LineNumber is required.");
        createListener();
    }

    @Test
    public void shouldValidateActionIsCreatedWithRule() {
        appendLineNumber();
        expectProperException("Rule is required.");
        createListener();
    }

    @Test
    public void shouldValidateActionIsCreatedWithResource() {
        appendLineNumber();
        appendRule();
        expectProperException("Resource is required.");
        createListener();
    }

    @Test
    public void shouldValidateActionIsCreatedWithViolations() {
        appendLineNumber();
        appendRule();
        appendResource();
        expectProperException("Violations empty list is required.");
        createListener();
    }

    private GrammarSpellCheckListener createFullListener() {
        appendViolations();
        appendLineNumber();
        appendRule();
        appendResource();
        return builder.build();
    }

    private GrammarSpellCheckListener createListener() {
        return builder.build();
    }

    private void appendViolations() {
        builder.setViolations(violations);
    }

    private void appendLineNumber() {
        builder.setLineNumber(12);
    }

    private void appendRule() {
        builder.setRule(Rule.create());
    }

    private void appendResource() {
        JavaFile resource = mock(JavaFile.class);
        builder.setResource(resource);
    }

    private GrammarSpellCheckListener.Builder createBuilder() {
        return new GrammarSpellCheckListener.Builder();
    }

    private void expectProperException(final String message) {
        thrown.expect(IllegalStateException.class);
        thrown.expectMessage(Matchers.containsString(message));
    }
}
