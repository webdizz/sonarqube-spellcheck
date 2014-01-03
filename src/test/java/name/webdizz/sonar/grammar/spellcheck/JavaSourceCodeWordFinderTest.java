package name.webdizz.sonar.grammar.spellcheck;

import static org.junit.Assert.assertEquals;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import com.swabunga.spell.event.WordNotFoundException;

@RunWith(MockitoJUnitRunner.class)
public class JavaSourceCodeWordFinderTest {

    private JavaSourceCodeWordFinder testingInstance = new JavaSourceCodeWordFinder();

    @Test(expected = WordNotFoundException.class)
    public void shouldNotFindNextWord() {
        testingInstance.next();
    }

    @Test
    public void shouldTreatCamelCaseAsSeparateWords() {
        assertFoundCamelCaseWords("Camel", "Case", " ");
        assertFoundCamelCaseWords("Java", "World", "");
        assertFoundCamelCaseWords("Java", "World", ";");
    }

    @Ignore
    @Test
    public void shouldTreatConstantsCorrectly() {

    }

    private void assertFoundCamelCaseWords(final String firstWord, final String secondWord, final String space) {
        testingInstance.setText(firstWord + secondWord + space);
        assertEquals("First word is incorrect", firstWord, testingInstance.next().getText());
        assertEquals("Second word is incorrect", secondWord, testingInstance.next().getText());
    }
}
