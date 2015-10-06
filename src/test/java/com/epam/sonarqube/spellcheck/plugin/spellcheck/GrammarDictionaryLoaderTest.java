package com.epam.sonarqube.spellcheck.plugin.spellcheck;

import com.epam.sonarqube.spellcheck.plugin.PluginParameter;
import com.epam.sonarqube.spellcheck.plugin.exceptions.UnableToLoadDictionary;
import com.google.common.base.Optional;
import com.swabunga.spell.engine.SpellDictionary;
import com.swabunga.spell.engine.SpellDictionaryHashMap;
import org.junit.BeforeClass;
import org.junit.Test;
import org.sonar.api.config.Settings;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by Konstiantyn on 9/29/2015.
 */
public class GrammarDictionaryLoaderTest {

    private static Settings settings = mock(Settings.class);
    private GrammarDictionaryLoader grammarDictionaryLoader = new GrammarDictionaryLoader(settings);

    @BeforeClass
    public static void initSettingsAndLoadDictionaryVeryTimeConsuming() {
        when(settings.getBoolean(PluginParameter.SPELL_IGNOREMIXEDCASE)).thenReturn(false);
        when(settings.getBoolean(PluginParameter.SPELL_IGNOREUPPERCASE)).thenReturn(true);
        when(settings.getBoolean(PluginParameter.SPELL_IGNOREDIGITWORDS)).thenReturn(false);
        when(settings.getBoolean(PluginParameter.SPELL_IGNOREINTERNETADDRESSES)).thenReturn(true);
        when(settings.getInt(PluginParameter.SPELL_THRESHOLD)).thenReturn(1);

        //dictionary = new GrammarDictionaryLoader(settings).loadMainDictionary();
    }

    @Test
    public void shouldLoadMainDictionaryFromResources() throws Exception {
        when(settings.getString(PluginParameter.DICTIONARY_PATH)).thenReturn("/dict/english.0");
        SpellDictionary dictionary = grammarDictionaryLoader.loadMainDictionary();

        assertNotNull(dictionary);
    }

    @Test(expected = UnableToLoadDictionary.class)
    public void shouldThrowExceptionWhenDictionaryPathIsNull() throws Exception {
        when(settings.getString(PluginParameter.DICTIONARY_PATH)).thenReturn(null);
        SpellDictionary dictionary = grammarDictionaryLoader.loadMainDictionary();
    }

    @Test(expected = UnableToLoadDictionary.class)
    public void shouldThrowExceptionWhenHaveWrongDictionaryPath() throws Exception {
        when(settings.getString(PluginParameter.DICTIONARY_PATH)).thenReturn("/res/res");
        SpellDictionary dictionary = grammarDictionaryLoader.loadMainDictionary();
    }

    @Test
    public void shouldLoadAlternateDictionary() throws Exception {
        String altDictionaryProperty = "first,second,third";
        when(settings.getString(PluginParameter.ALTERNATIVE_DICTIONARY_PROPERTY_KEY)).thenReturn(altDictionaryProperty);

        Optional<SpellDictionaryHashMap> dictionary = grammarDictionaryLoader.loadAlternateDictionary();

        assertEquals(true, dictionary.get().isCorrect("first"));
        assertEquals(true, dictionary.get().isCorrect("second"));
        assertEquals(true, dictionary.get().isCorrect("third"));
    }
    
    @Test
    public void shouldLoadAlternateDictionaryWithWhitespace() throws Exception {
        String altDictionaryProperty = "  first , second  ,   third   ";
        when(settings.getString(PluginParameter.ALTERNATIVE_DICTIONARY_PROPERTY_KEY)).thenReturn(altDictionaryProperty);

        Optional<SpellDictionaryHashMap> dictionary = grammarDictionaryLoader.loadAlternateDictionary();

        assertEquals(true, dictionary.get().isCorrect("first"));
        assertEquals(true, dictionary.get().isCorrect("second"));
        assertEquals(true, dictionary.get().isCorrect("third"));
    }

    @Test
    public void shouldCreateEmptyDictionaryWhenAlternativeDictionaryPropertyKeyIsNull() throws Exception {
        when(settings.getString(PluginParameter.ALTERNATIVE_DICTIONARY_PROPERTY_KEY)).thenReturn(null);

        Optional<SpellDictionaryHashMap> dictionary = grammarDictionaryLoader.loadAlternateDictionary();

        assertTrue(dictionary.asSet().isEmpty());
    }

    @Test
    public void shouldReturnTheSameDictionaryWhenCallLoadDictionaryTwoTimes() throws Exception {
        when(settings.getString(PluginParameter.DICTIONARY_PATH)).thenReturn("/dict/english.0");
        SpellDictionary dictionary = grammarDictionaryLoader.loadMainDictionary();
        SpellDictionary dictionary2 = grammarDictionaryLoader.loadMainDictionary();

        assertEquals(dictionary, dictionary2);
    }
}