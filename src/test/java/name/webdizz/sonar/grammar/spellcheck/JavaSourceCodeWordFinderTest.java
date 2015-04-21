package name.webdizz.sonar.grammar.spellcheck;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import name.webdizz.sonar.grammar.utils.SpellCheckerUtil;

import com.swabunga.spell.event.SpellChecker;

public class JavaSourceCodeWordFinderTest {
    private String ERROR_MESSAGE = "This text has errors. If there are no errors, we expect 'errorsSize = -1'";

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
        assertEquals(1, errorsSize);
    }

    @Test
    public void shouldCheckCamelCaseNameAndReturnThreeErrorTest() throws Exception {
        String testLine = "myyWrongCameeelNameeTest";
        int errorsSize = getErrorsSize(testLine);
        assertEquals(3, errorsSize);
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
        assertEquals(2, errorsSize);
    }

    @Test
    public void shouldCheckMixedNameAndReturnTreeErrorTest() throws Exception {
        String testLine = "myyWrong111NaameCameeelTeest";
        int errorsSize = getErrorsSize(testLine);
        assertEquals(3, errorsSize);
    }

    private int getErrorsSize(String testLine) {
        SpellChecker spellChecker = new SpellCheckerUtil().getSpellChecker();
        JavaSourceCodeTokenizer sourceCodeTokenizer = new JavaSourceCodeTokenizer(testLine, new JavaSourceCodeWordFinder());
        return spellChecker.checkSpelling(sourceCodeTokenizer);
    }
}