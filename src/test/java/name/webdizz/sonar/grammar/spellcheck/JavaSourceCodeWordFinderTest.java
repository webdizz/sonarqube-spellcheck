package name.webdizz.sonar.grammar.spellcheck;

import static org.junit.Assert.assertEquals;

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
        assertFoundWords("Camel", "Case", "", " ", "");
        assertFoundWords("Java", "World", "", "", "");
        assertFoundWords("Java", "World", "", ";", "");
        assertFoundWords("wild", "World", "", ";", "");
        assertFoundWords("IO", "Exception", "", "", "");
    }

    @Test
    public void shouldTreatConstantsCorrectly() {
        assertFoundWords("CONSTANT", "VALUE", "=", "", "_");
        assertFoundWords("some", "underscore", " ", " ", "_");
    }

    private void assertFoundWords(final String firstWord, final String secondWord, final String headSymbol, final String tailSymbol, final String separator) {
        String newText = headSymbol + firstWord + separator + secondWord + tailSymbol;
        testingInstance.setText(newText);
        String firstFoundWord = testingInstance.next().getText();
        assertEquals("First word is incorrect", firstWord, firstFoundWord);
        assertEquals("Second word is incorrect", secondWord, testingInstance.next().getText());
    }
}
