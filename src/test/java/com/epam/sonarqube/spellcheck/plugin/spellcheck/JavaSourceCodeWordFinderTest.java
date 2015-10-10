package com.epam.sonarqube.spellcheck.plugin.spellcheck;

import static com.swabunga.spell.event.SpellChecker.SPELLCHECK_OK;
import static org.fest.assertions.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.experimental.theories.FromDataPoints;
import org.junit.experimental.theories.Theory;
import org.sonar.api.config.Settings;

import com.epam.sonarqube.spellcheck.plugin.PluginParameter;
import com.swabunga.spell.engine.SpellDictionary;
import com.swabunga.spell.event.SpellChecker;
import com.swabunga.spell.event.WordNotFoundException;

public class JavaSourceCodeWordFinderTest {
    private String ERROR_MESSAGE = "This text has errors. If there are no errors, we expect 'errorsSize = -1'";
    private SpellChecker spellChecker;
    private int minimumWordLength = 4;
    
    private static Settings settings;
    private static SpellDictionary dictionary;

    @BeforeClass
    public static void initSettingsAndLoadDictionaryVeryTimeConsuming() {
        createDefaultSettingsMock();
        
        dictionary = new GrammarDictionaryLoader(settings).loadMainDictionary();
    }

    @Before
    public void init() {
        SpellCheckerFactory spellCheckerFactory = new SpellCheckerFactory();
        spellCheckerFactory.setSettings(settings);

        spellChecker = spellCheckerFactory.getSpellChecker();
    }

    @After
    public void tearDown() {
        createDefaultSettingsMock();
    }
    
    @Test(expected = WordNotFoundException.class)
    public void shouldThrowExceptionWhenThereIsNoNextWord() throws Exception {
        
        String testLine = "myCamelTestVariable";
        
        JavaSourceCodeWordFinder javaSourceCodeWordFinder = new JavaSourceCodeWordFinder(settings);
        JavaSourceCodeTokenizer sourceCodeTokenizer = new JavaSourceCodeTokenizer(testLine, javaSourceCodeWordFinder);
        spellChecker.setUserDictionary(dictionary);
        
        spellChecker.checkSpelling(sourceCodeTokenizer);
        
        javaSourceCodeWordFinder.next(); //throws WordNotFoundException
    }
    
    @Test
    public void shouldIgnoreMixedCaseMisspelledWordAtStartOfSentenceWhenIgnoreMixedCaseFlagTurnedOn() throws Exception {
        when(settings.getBoolean(PluginParameter.SPELL_IGNORE_MIXED_CASE)).thenReturn(true);
        //reinitialize after changing mock for "settings"
        init();
        
        String testLine = "Theeee pool of constants used in the spell-check-plugin";
        
        int errorsSize = getErrorsSize(testLine);
        assertEquals("Wrong error size. Expected = -1", -1, errorsSize);
    }
    
    @Test
    public void shouldIgnoreMixedCaseMisspelledWordInMiddleAndEndOfSentenceWhenIgnoreMixedCaseFlagTurnedOn() throws Exception {
        when(settings.getBoolean(PluginParameter.SPELL_IGNORE_MIXED_CASE)).thenReturn(true);
        //reinitialize after changing mock for "settings"
        init();
        
        String testLine = "The pool of constants Errrroor used in the spell-check-plugin Errrorrrrr Errrorororr";
        
        int errorsSize = getErrorsSize(testLine);
        assertEquals("Wrong error size. Expected = -1", -1, errorsSize);
    }
    
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
    public void shouldCheckWordsGreaterThenMinimumWordLength() throws Exception {
        minimumWordLength = 4;
        when(settings.getInt(PluginParameter.SPELL_MINIMUM_WORD_LENGTH)).thenReturn(minimumWordLength);
        String testLine = "ert.wrn.Tesst.packaage.service";

        int errorsSize = getErrorsSize(testLine);

        assertEquals("Wrong error size. Expected = 2", 2, errorsSize);
    }


    @Test
    public void shouldNotCheckWordsLessThenMinimumWordLength() throws Exception {
        minimumWordLength = 4;
        when(settings.getInt(PluginParameter.SPELL_MINIMUM_WORD_LENGTH)).thenReturn(minimumWordLength);
        String testLine = "erq.zzw.test.package";

        int errorsSize = getErrorsSize(testLine);

        assertEquals("Wrong error size. Expected = -1",-1, errorsSize);
    }
    
    @Test
    public void shouldNotCheckLastWordIfItIsLessThenMinimumWordLength() throws Exception {
        minimumWordLength = 4;
        when(settings.getInt(PluginParameter.SPELL_MINIMUM_WORD_LENGTH)).thenReturn(minimumWordLength);
        String testLine = "erq.zzw.test.package.wqr";

        int errorsSize = getErrorsSize(testLine);

        assertEquals("Wrong error size. Expected = -1",-1, errorsSize);
    }

    private int getErrorsSize(String testLine) {
        JavaSourceCodeWordFinder javaSourceCodeWordFinder = new JavaSourceCodeWordFinder(settings);
        JavaSourceCodeTokenizer sourceCodeTokenizer = new JavaSourceCodeTokenizer(testLine, javaSourceCodeWordFinder);
        spellChecker.setUserDictionary(dictionary);
        return spellChecker.checkSpelling(sourceCodeTokenizer);
    }
    
    private static void createDefaultSettingsMock() {
        settings = mock(Settings.class);
        when(settings.getString(PluginParameter.DICTIONARY_PATH)).thenReturn("/dict/english.0");

        when(settings.getBoolean(PluginParameter.SPELL_IGNORE_MIXED_CASE)).thenReturn(false);
        when(settings.getBoolean(PluginParameter.SPELL_IGNORE_UPPERCASE)).thenReturn(true);
        when(settings.getBoolean(PluginParameter.SPELL_IGNORE_DIGIT_WORDS)).thenReturn(false);
        when(settings.getBoolean(PluginParameter.SPELL_IGNORE_INTERNET_ADDRESSES)).thenReturn(true);
    }
    
}