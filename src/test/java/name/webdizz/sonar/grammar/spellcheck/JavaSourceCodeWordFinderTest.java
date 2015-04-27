package name.webdizz.sonar.grammar.spellcheck;

import com.swabunga.spell.engine.SpellDictionary;
import com.swabunga.spell.event.SpellChecker;
import org.junit.Test;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static com.swabunga.spell.event.SpellChecker.SPELLCHECK_OK;


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
    public void shouldCheckMixedNameAndReturnOneErrorTest() throws Exception {
        String testLine = "myWrong111CameeelTest";
        int errorsSize = getErrorsSize(testLine);
        assertEquals("Wrong error size. Expected = 1", 1, errorsSize);
    }

    @Test
    public void shouldCheckMixedNameAndReturnTwoErrorTest() throws Exception {
        String testLine = "myyWrong111CameeelTeest";
        int errorsSize = getErrorsSize(testLine);
        assertEquals("Wrong error size. Expected = 3", 3, errorsSize);
    }

    @Test
    public void shouldCheckMixedNameAndReturnTreeErrorTest() throws Exception {
        String testLine = "myyWrong111NaameCameeelTeest";
        int errorsSize = getErrorsSize(testLine);
        assertEquals("Wrong error size. Expected = 4", 4, errorsSize);
    }

    @Test
    public void shouldCheckWordWithOneEndDigitAndReturnNoErrorsTest() {
        String testLine1 = "word1";

        int errorsSize = getErrorsSize(testLine1);
        assertThat(errorsSize).isEqualTo(SPELLCHECK_OK);
    }

    @Test
    public void shouldCheckWordWithMultiEndDigitsAndReturnNoErrorsTest() {
        String testLine = "word12";

        int errorsSize = getErrorsSize(testLine);
        assertThat(errorsSize).isEqualTo(SPELLCHECK_OK);;
    }

    @Test
    public void shouldCheckWordWithOneInnerDigitAndReturnNoErrorsTest() {
        String testLine = "convert2String";

        int errorsSize = getErrorsSize(testLine);
        assertThat(errorsSize).isEqualTo(SPELLCHECK_OK);
    }

    @Test
    public void shouldCheckWordWithMultiInnerDigitsAndReturnNoErrorsTest() {
        String testLine = "convert23String";

        int errorsSize = getErrorsSize(testLine);
        assertThat(errorsSize).isEqualTo(SPELLCHECK_OK);
    }

    @Test
    public void shouldCheckWordWithOneStartDigitAndReturnNoErrorsTest() {
        String testLine = "4wordsWithDigits";

        int errorsSize = getErrorsSize(testLine);
        assertThat(errorsSize).isEqualTo(SPELLCHECK_OK);
    }

    @Test
    public void shouldCheckWordWithMultiStartDigitsAndReturnNoErrorsTest() {
        String testLine = "14wordsWithDigits";

        int errorsSize = getErrorsSize(testLine);
        assertThat(errorsSize).isEqualTo(SPELLCHECK_OK);
    }

    private int getErrorsSize(String testLine) {
        JavaSourceCodeTokenizer sourceCodeTokenizer = new JavaSourceCodeTokenizer(testLine, new JavaSourceCodeWordFinder());
        spellChecker.setUserDictionary(dictionary);
        return spellChecker.checkSpelling(sourceCodeTokenizer);
    }
}