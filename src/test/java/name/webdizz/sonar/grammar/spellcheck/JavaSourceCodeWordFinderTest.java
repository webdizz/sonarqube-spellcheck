package name.webdizz.sonar.grammar.spellcheck;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.swabunga.spell.engine.SpellDictionary;
import com.swabunga.spell.event.SpellChecker;

public class JavaSourceCodeWordFinderTest {
    private String ERROR_MESSAGE = "This text has errors. If there are no errors, we expect 'errorsSize = -1'";
    private SpellChecker spellChecker = new SpellCheckerFactory().getSpellChecker();
    private SpellDictionary dictionary = new GrammarDictionaryLoader().loadMainDictionary();
    private int minimumWordLength = 4;

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
        String testLine = "myyyyWrongCameeelNameeTest";
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
        String testLine = "myyyyWrong111CameeelTeest";
        int errorsSize = getErrorsSize(testLine);
        assertEquals("Wrong error size. Expected = 2", 2, errorsSize);
    }

    @Test
    public void shouldCheckMixedNameAndReturnTreeErrorTest() throws Exception {
        String testLine = "myyyyWrong111NaameCameeelTeest";
        int errorsSize = getErrorsSize(testLine);
        assertEquals("Wrong error size. Expected = 3", 3, errorsSize);
    }

    @Test
    public void shouldCheckMinWordLengthAndSkipWordsWithSizeLessThenDefined() throws Exception {
        minimumWordLength = 3;
        String testLine = "tru.Test.package";

        int errorsSize = getErrorsSize(testLine);

        assertEquals("Wrong error size. Expected = 1", 1, errorsSize);
    }


    @Test
    public void shouldCheckMinWordLengthAndSkipALLWordsWithSizeLessThenDefined() throws Exception {
        minimumWordLength = 4;
        String testLine = "er.wrn.Test.package";

        int errorsSize = getErrorsSize(testLine);

        assertEquals("Wrong error size. Expected = -1", -1, errorsSize);
    }


    @Test
    public void shouldCheckALLWordsGreaterThenSizeOfMinimumWordLengthVariable() throws Exception {
        minimumWordLength = 2;
        String testLine = "ert.wrn.Tesst.package";

        int errorsSize = getErrorsSize(testLine);

        assertEquals("Wrong error size. Expected = 3", 3, errorsSize);
    }

    private int getErrorsSize(String testLine) {

        JavaSourceCodeWordFinder javaSourceCodeWordFinder =  new JavaSourceCodeWordFinder();
        javaSourceCodeWordFinder.setMinimumWordLength(minimumWordLength);
        JavaSourceCodeTokenizer sourceCodeTokenizer = new JavaSourceCodeTokenizer(testLine,javaSourceCodeWordFinder);
        spellChecker.setUserDictionary(dictionary);
        return spellChecker.checkSpelling(sourceCodeTokenizer);
    }
}