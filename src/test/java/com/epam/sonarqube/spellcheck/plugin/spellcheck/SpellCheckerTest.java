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
    private GrammarDictionaryLoader grammarDictionaryLoader;
    private SpellCheckListener listener = mock(SpellCheckListener.class);
    private JavaSourceCodeWordFinder javaSourceCodeWordFinder;
    private SpellChecker spellChecker;
    private Settings settings = mock(Settings.class);

    @Before
    public void init() {
        when(settings.getString(PluginParameter.DICTIONARY_PATH)).thenReturn("/dict/english.0");
        when(settings.getBoolean(PluginParameter.SPELL_IGNOREMIXEDCASE)).thenReturn(false);
        when(settings.getBoolean(PluginParameter.SPELL_IGNOREUPPERCASE)).thenReturn(true);
        when(settings.getBoolean(PluginParameter.SPELL_IGNOREDIGITWORDS)).thenReturn(false);
        when(settings.getBoolean(PluginParameter.SPELL_IGNOREINTERNETADDRESSES)).thenReturn(true);

        javaSourceCodeWordFinder = new JavaSourceCodeWordFinder(settings);
        grammarDictionaryLoader = spy(new GrammarDictionaryLoader(settings));
        spellCheckerFactory = new SpellCheckerFactory();
        spellCheckerFactory.setSettings(settings);
        spellChecker = new SpellChecker(grammarDictionaryLoader,
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
        assertNotNull(new SpellChecker(grammarDictionaryLoader,
                javaSourceCodeWordFinder, spellCheckerFactory));
    }

    @Test
    public void shouldCallMethodsWhenInitializeSpellCheckerTest() {
        verify(grammarDictionaryLoader).loadMainDictionary();
        verify(grammarDictionaryLoader).loadAlternateDictionary();
    }
}
