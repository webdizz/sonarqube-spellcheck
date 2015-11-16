package com.epam.sonarqube.spellcheck.plugin.spellcheck;

import ch.qos.logback.classic.Level;
import com.epam.sonarqube.spellcheck.plugin.PluginParameter;
import com.swabunga.spell.event.SpellCheckListener;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.LoggerFactory;
import org.sonar.api.config.Settings;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;


public class SpellCheckerTest {

    private SpellCheckerFactory spellCheckerFactory;
    private SpellDictionaryLoader spellDictionaryLoader;
    private SpellCheckListener listener = mock(SpellCheckListener.class);
    private JavaSourceCodeWordFinder javaSourceCodeWordFinder;
    private SpellChecker spellChecker;
    private Settings settings = mock(Settings.class);

    @Before
    public void init() {
        when(settings.getString(PluginParameter.DICTIONARY_PATH)).thenReturn("/dict/english.0");
        when(settings.getBoolean(PluginParameter.SPELL_IGNORE_MIXED_CASE)).thenReturn(false);
        when(settings.getBoolean(PluginParameter.SPELL_IGNORE_UPPERCASE)).thenReturn(true);
        when(settings.getBoolean(PluginParameter.SPELL_IGNORE_DIGIT_WORDS)).thenReturn(false);
        when(settings.getBoolean(PluginParameter.SPELL_IGNORE_INTERNET_ADDRESSES)).thenReturn(true);

        javaSourceCodeWordFinder = new JavaSourceCodeWordFinder(settings);
        spellDictionaryLoader = spy(new SpellDictionaryLoader(settings));
        spellCheckerFactory = new SpellCheckerFactory();
        spellCheckerFactory.setSettings(settings);
        spellChecker = new SpellChecker(spellDictionaryLoader,
                javaSourceCodeWordFinder,spellCheckerFactory);
        spellChecker.initialize();
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionWhenCheckSpellingWithoutProperListenerArgTest() {
        spellChecker.checkSpelling("qweerert", null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionWhenSpellingWithoutProperArgsTest() {
        spellChecker.checkSpelling(null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionWhenSpellingWithEmptyLineArgTest() {
        spellChecker.checkSpelling("", listener);
    }

    @Test
    public void shouldCheckSpell() {
        ((ch.qos.logback.classic.Logger)
                LoggerFactory.getLogger(SpellChecker.class))
                .setLevel(Level.DEBUG);
        spellChecker.checkSpelling("abcdef", listener);
    }

    @Test
    public void shouldReturnNewSpellCheckerTest() {
        assertNotNull(new SpellChecker(spellDictionaryLoader,
                javaSourceCodeWordFinder, spellCheckerFactory));
    }

    @Test
    public void shouldCallMethodsWhenInitializeSpellCheckerTest() {
        verify(spellDictionaryLoader).loadMainDictionary();
        verify(spellDictionaryLoader).loadAlternateDictionary();
    }
}
