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


public class SpellDictionaryLoaderTest {

    private static Settings settings = mock(Settings.class);
    private SpellDictionaryLoader spellDictionaryLoader = new SpellDictionaryLoader(settings);

    @BeforeClass
    public static void init() {
        when(settings.getBoolean(PluginParameter.SPELL_IGNORE_MIXED_CASE)).thenReturn(false);
        when(settings.getBoolean(PluginParameter.SPELL_IGNORE_UPPERCASE)).thenReturn(true);
        when(settings.getBoolean(PluginParameter.SPELL_IGNORE_DIGIT_WORDS)).thenReturn(false);
        when(settings.getInt(PluginParameter.URL_DICTIONARY_TIMEOUT)).thenReturn(5000);
        when(settings.getBoolean(PluginParameter.SPELL_IGNORE_INTERNET_ADDRESSES)).thenReturn(true);

    }

    @Test
    public void shouldLoadURLDictionaryFromResources() throws Exception {
        when(settings.getString(PluginParameter.URL_DICTIONARY_PATH)).thenReturn("https://raw.githubusercontent.com/GurVic/PizzaDelivery/master/english.0");
        SpellDictionary dictionary = spellDictionaryLoader.loadURLDictionary(true);
        assertNotNull(dictionary);
        assertFalse(dictionary.isCorrect("sekleklzsk"));
        assertTrue(dictionary.isCorrect("ACCTTYPE"));
    }

    @Test(expected = UnableToLoadDictionary.class)
    public void shouldThrowExceptionWhenURLDictionaryPathIsNull() {
        when(settings.getString(PluginParameter.URL_DICTIONARY_PATH)).thenReturn(null);
        SpellDictionary dictionary = spellDictionaryLoader.loadURLDictionary(true);
    }

    @Test
    public void shouldReturnLoadedURLDictionary() {
        when(settings.getString(PluginParameter.URL_DICTIONARY_PATH)).thenReturn("https://raw.githubusercontent.com/GurVic/PizzaDelivery/master/english.0");
        SpellDictionary dictionary = spellDictionaryLoader.loadURLDictionary(true);
        assertNotNull(dictionary);
        dictionary = null;
        dictionary = spellDictionaryLoader.loadURLDictionary(true);
        assertNotNull(dictionary);
    }

    @Test(expected = UnableToLoadDictionary.class)
    public void shouldThrowExceptionWhenHaveNoURLDictionary() {
        when(settings.getString(PluginParameter.DICTIONARY_PATH)).thenReturn("https://raw.githubusercontent.com/GurVic/PizzaDelivery/master/english.1");
        SpellDictionary dictionary = spellDictionaryLoader.loadMainDictionary();
    }

    @Test(expected = UnableToLoadDictionary.class)
    public void shouldThrowExceptionWhenHaveWrongURL() {
        when(settings.getString(PluginParameter.DICTIONARY_PATH)).thenReturn("https://rawqwe.githubusercontent.com/GurVic/PizzaDelivery/master/english.1");
        SpellDictionary dictionary = spellDictionaryLoader.loadMainDictionary();
    }

    @Test
    public void shouldLoadMainDictionaryFromResources() throws Exception {
        when(settings.getString(PluginParameter.DICTIONARY_PATH)).thenReturn("/dict/english.0");
        SpellDictionary dictionary = spellDictionaryLoader.loadMainDictionary();

        assertNotNull(dictionary);
    }

    @Test(expected = UnableToLoadDictionary.class)
    public void shouldThrowExceptionWhenDictionaryPathIsNull() throws Exception {
        when(settings.getString(PluginParameter.DICTIONARY_PATH)).thenReturn(null);
        SpellDictionary dictionary = spellDictionaryLoader.loadMainDictionary();
    }

    @Test(expected = UnableToLoadDictionary.class)
    public void shouldThrowExceptionWhenHaveWrongDictionaryPath() throws Exception {
        when(settings.getString(PluginParameter.DICTIONARY_PATH)).thenReturn("/res/res");
        SpellDictionary dictionary = spellDictionaryLoader.loadMainDictionary();
    }

    @Test
    public void shouldLoadAlternateDictionary() throws Exception {
        String altDictionaryProperty = "first,second,third";
        when(settings.getString(PluginParameter.ALTERNATIVE_DICTIONARY_PROPERTY_KEY)).thenReturn(altDictionaryProperty);

        Optional<SpellDictionaryHashMap> dictionary = spellDictionaryLoader.loadAlternateDictionary();

        assertEquals(true, dictionary.get().isCorrect("first"));
        assertEquals(true, dictionary.get().isCorrect("second"));
        assertEquals(true, dictionary.get().isCorrect("third"));
    }
    
    @Test
    public void shouldLoadAlternateDictionaryWithWhitespace() throws Exception {
        String altDictionaryProperty = "  first , second  ,   third   ";
        when(settings.getString(PluginParameter.ALTERNATIVE_DICTIONARY_PROPERTY_KEY)).thenReturn(altDictionaryProperty);

        Optional<SpellDictionaryHashMap> dictionary = spellDictionaryLoader.loadAlternateDictionary();

        assertEquals(true, dictionary.get().isCorrect("first"));
        assertEquals(true, dictionary.get().isCorrect("second"));
        assertEquals(true, dictionary.get().isCorrect("third"));
    }

    @Test
    public void shouldCreateEmptyDictionaryWhenAlternativeDictionaryPropertyKeyIsNull() throws Exception {
        when(settings.getString(PluginParameter.ALTERNATIVE_DICTIONARY_PROPERTY_KEY)).thenReturn(null);

        Optional<SpellDictionaryHashMap> dictionary = spellDictionaryLoader.loadAlternateDictionary();

        assertTrue(dictionary.asSet().isEmpty());
    }

    @Test
    public void shouldReturnTheSameDictionaryWhenCallLoadDictionaryTwoTimes() throws Exception {
        when(settings.getString(PluginParameter.DICTIONARY_PATH)).thenReturn("/dict/english.0");
        SpellDictionary dictionary = spellDictionaryLoader.loadMainDictionary();
        SpellDictionary dictionary2 = spellDictionaryLoader.loadMainDictionary();

        assertEquals(dictionary, dictionary2);
    }
}