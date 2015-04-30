package name.webdizz.sonar.grammar.spellcheck;

import com.swabunga.spell.engine.SpellDictionary;
import com.swabunga.spell.event.SpellChecker;
import name.webdizz.sonar.grammar.PluginParameter;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.FromDataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;
import org.sonar.api.config.Settings;

import static com.swabunga.spell.event.SpellChecker.SPELLCHECK_OK;
import static org.fest.assertions.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


@RunWith(Theories.class)
public class JavaSourceCodeWordFinderTest {
    private String ERROR_MESSAGE = "This text has errors. If there are no errors, we expect 'errorsSize = -1'";
    private Settings settings = mock(Settings.class);
    private SpellChecker spellChecker = new SpellCheckerFactory().getSpellChecker();
    private SpellDictionary dictionary;
    private int minimumWordLength = 4;

    @Before
    public void init(){
        when(settings.getString(PluginParameter.DICTIONARY_PATH)).thenReturn("/dict/english.0");
        dictionary = new GrammarDictionaryLoader(settings).loadMainDictionary();

    }


    @DataPoints("validDigitWords")
    public static String[] validDigitWords = new String[] {"word1", "word12", "convert2String", "convert23String",
            "4wordsWithDigits", "14wordsWithDigits"};

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
    public void shouldCheckMixedNameAndReturnOneErrorTest() throws Exception {
        String testLine = "myWrong111CameeelTest";
        int errorsSize = getErrorsSize(testLine);
        assertEquals("Wrong error size. Expected = 1", 1, errorsSize);
    }

    @Test
    public void shouldCheckMixedNameAndReturnTwoErrorTest() throws Exception {
        String testLine = "myyyyWrong111CameeelTeest";
        int errorsSize = getErrorsSize(testLine);
        assertEquals("Wrong error size. Expected = 3", 3, errorsSize);
    }

    @Test
    public void shouldCheckMixedNameAndReturnTreeErrorTest() throws Exception {
        String testLine = "myyyyWrong111NaameCameeelTeest";
        int errorsSize = getErrorsSize(testLine);
        assertEquals("Wrong error size. Expected = 4", 4, errorsSize);
    }

    @Theory
    public void shouldCheckWordsWithDigitsAndReturnNoErrorsTest(@FromDataPoints("validDigitWords") String word) {
        int errorsSize = getErrorsSize(word);
        assertThat(errorsSize).isEqualTo(SPELLCHECK_OK);
    }

    @Test
    public void shouldCheckMinWordLengthAndSkipWordsWithSizeLessThenDefined() throws Exception {
        minimumWordLength = 3;
        when(settings.getInt(PluginParameter.MIN_WORD_LENGTH)).thenReturn(minimumWordLength);
        String testLine = "tru.Test.package";

        int errorsSize = getErrorsSize(testLine);

        assertEquals("Wrong error size. Expected = 1", 1, errorsSize);
    }


    @Test
    public void shouldCheckMinWordLengthAndSkipALLWordsWithSizeLessThenDefined() throws Exception {
        minimumWordLength = 4;
        when(settings.getInt(PluginParameter.MIN_WORD_LENGTH)).thenReturn(minimumWordLength);
        String testLine = "er.wrn.Test.package";

        int errorsSize = getErrorsSize(testLine);

        assertEquals("Wrong error size. Expected = -1", -1, errorsSize);
    }


    @Test
    public void shouldCheckALLWordsGreaterThenSizeOfMinimumWordLengthVariable() throws Exception {
        minimumWordLength = 2;
        when(settings.getInt(PluginParameter.MIN_WORD_LENGTH)).thenReturn(minimumWordLength);
        String testLine = "ert.wrn.Tesst.package";

        int errorsSize = getErrorsSize(testLine);

        assertEquals("Wrong error size. Expected = 3", 3, errorsSize);
    }

    private int getErrorsSize(String testLine) {
        JavaSourceCodeWordFinder javaSourceCodeWordFinder =  new JavaSourceCodeWordFinder(settings);
        JavaSourceCodeTokenizer sourceCodeTokenizer = new JavaSourceCodeTokenizer(testLine,javaSourceCodeWordFinder);
        spellChecker.setUserDictionary(dictionary);
        return spellChecker.checkSpelling(sourceCodeTokenizer);
    }
}