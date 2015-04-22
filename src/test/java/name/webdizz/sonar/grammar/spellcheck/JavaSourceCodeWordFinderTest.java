package name.webdizz.sonar.grammar.spellcheck;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.swabunga.spell.engine.SpellDictionary;
import com.swabunga.spell.event.SpellChecker;

public class JavaSourceCodeWordFinderTest {
    private String ERROR_MESSAGE = "This text has errors. If there are no errors, we expect 'errorsSize = -1'";
    private SpellChecker spellChecker = new SpellCheckerFactory().getSpellChecker();
    private SpellDictionary dictionary = new GrammarDictionaryLoader().loadMainDictionary();

    @Test
    public void shouldCheckCamelCaseNameAndReturnMinusOneThatMeansNoErrorTest() throws Exception {
        String testLine = "myWrongCamelTest";
        int errorsSize = getErrorsSize(testLine);
        assertEquals(ERROR_MESSAGE, -1, errorsSize);
    }

    @Test
    public void shouldCheckCamelCaseNameAndReturnOneErrorTest() throws Exception {
        String testLine = "myWrongCameeelTest";
        int errorsSize = getErrorsSize(testLine);
        assertEquals("Wrong error size. Expected = 1", 1, errorsSize);
    }

    @Test
    public void shouldCheckCamelCaseNameAndReturnThreeErrorTest() throws Exception {
        String testLine = "myyWrongCameeelNameeTest";
        int errorsSize = getErrorsSize(testLine);
        assertEquals("Wrong error size. Expected = 3", 3, errorsSize);
    }

    @Test
    public void shouldCheckMixedNameAndReturnMinusOneThatMeansNoErrorTest() throws Exception {
        String testLine = "myWrong111CameeelTest";
        int errorsSize = getErrorsSize(testLine);
        assertEquals(ERROR_MESSAGE, -1, errorsSize);
    }

    @Test
    public void shouldCheckMixedNameAndReturnTwoErrorTest() throws Exception {
        String testLine = "myyWrong111CameeelTeest";
        int errorsSize = getErrorsSize(testLine);
        assertEquals("Wrong error size. Expected = 2", 2, errorsSize);
    }

    @Test
    public void shouldCheckMixedNameAndReturnTreeErrorTest() throws Exception {
        String testLine = "myyWrong111NaameCameeelTeest";
        int errorsSize = getErrorsSize(testLine);
        assertEquals("Wrong error size. Expected = 3", 3, errorsSize);
    }

    private int getErrorsSize(String testLine) {
        JavaSourceCodeTokenizer sourceCodeTokenizer = new JavaSourceCodeTokenizer(testLine, new JavaSourceCodeWordFinder());
        spellChecker.setUserDictionary(dictionary);
        return spellChecker.checkSpelling(sourceCodeTokenizer);
    }
}