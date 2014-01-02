package name.webdizz.sonar.grammar.spellcheck;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.sonar.api.resources.JavaFile;

import com.swabunga.spell.event.SpellCheckListener;

@RunWith(MockitoJUnitRunner.class)
public class SpellActionTest {

    private static final String SOURCE_LINE = "some source";
    @Rule
    public org.junit.rules.ExpectedException thrown = ExpectedException.none();

    @Mock
    private GrammarChecker grammarChecker;

    private SpellAction.Builder builder;
    private SpellAction testingInstance;

    @Before
    public void setUp() {
        builder = new SpellAction.Builder();
    }

    @Test
    public void shouldValidateActionIsCreatedWithGrammarChecker() {
        expectProperException("GrammarChecker is required.");
        createTestingInstance();
    }

    @Test
    public void shouldValidateActionIsCreatedWithLineNumber() {
        appendActionWithGrammarChecker();
        expectProperException("LineNumber is required.");
        createTestingInstance();
    }

    @Test
    public void shouldValidateActionIsCreatedWithRule() {
        appendActionWithGrammarChecker();
        appendActionWithLineNumber();
        String message = "Rule is required.";
        expectProperException(message);
        createTestingInstance();
    }

    @Test
    public void shouldValidateActionIsCreatedWithSourceLine() {
        appendActionWithGrammarChecker();
        appendActionWithLineNumber();
        appendActionWithRule();
        String message = "SourceLine is required.";
        expectProperException(message);
        createTestingInstance();
    }

    @Test
    public void shouldValidateActionIsCreatedWithResource() {
        appendActionWithGrammarChecker();
        appendActionWithLineNumber();
        appendActionWithRule();
        appendActionWithSourceLine();
        String message = "Resource is required.";
        expectProperException(message);
        createTestingInstance();
    }

    @Test
    public void shouldCallGrammarChecker() {
        createAction();
        testingInstance.spell();
        verify(grammarChecker).checkSpelling(eq(SOURCE_LINE), any(SpellCheckListener.class));
    }

    private void createAction() {
        appendActionWithGrammarChecker();
        appendActionWithLineNumber();
        appendActionWithRule();
        appendActionWithSourceLine();
        appendActionWithResource();
        createTestingInstance();
    }

    private void createTestingInstance() {
        testingInstance = builder.build();
    }

    private void appendActionWithGrammarChecker() {
        builder.setGrammarChecker(grammarChecker);
    }

    private void appendActionWithLineNumber() {
        builder.setLineNumber(10);
    }

    private void appendActionWithSourceLine() {
        builder.setSourceLine(SOURCE_LINE);
    }

    private void appendActionWithResource() {
        JavaFile resource = Mockito.mock(JavaFile.class);
        builder.setResource(resource);
    }

    private void appendActionWithRule() {
        builder.setRule(org.sonar.api.rules.Rule.create());
    }

    private void expectProperException(final String message) {
        thrown.expect(IllegalStateException.class);
        thrown.expectMessage(Matchers.containsString(message));
    }
}
