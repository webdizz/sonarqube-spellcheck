package com.epam.sonarqube.spellcheck.plugin.spellcheck;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.sonar.api.BatchExtension;
import org.sonar.api.config.Settings;

import com.epam.sonarqube.spellcheck.plugin.PluginParameter;
import com.epam.sonarqube.spellcheck.plugin.exceptions.UnableToLoadDictionary;
import com.google.common.base.Optional;
import com.swabunga.spell.engine.SpellDictionary;
import com.swabunga.spell.engine.SpellDictionaryHashMap;

public class GrammarDictionaryLoader implements BatchExtension {

    private final Lock locker = new ReentrantLock();
    private Settings settings;
    private AtomicReference<SpellDictionary> dictionary = new AtomicReference<>();

    public GrammarDictionaryLoader(Settings settings) {
        this.settings = settings;
    }

    public SpellDictionary loadMainDictionary() {
        String dictionaryPath = settings.getString(PluginParameter.DICTIONARY_PATH);
        SpellDictionary spellDictionary = dictionary.get();
        locker.lock();
        try (InputStreamReader wordList = new InputStreamReader
                (GrammarDictionaryLoader.class.getResourceAsStream(dictionaryPath))) {
            if (null == spellDictionary) {
                spellDictionary = new SpellDictionaryHashMap(wordList);
                dictionary.set(spellDictionary);
            }
            return spellDictionary;
        } catch (IOException | NullPointerException e){
            throw new UnableToLoadDictionary("There is no file with dictionary.", e);
        } finally {
            locker.unlock();
        }
    }

    Optional<SpellDictionaryHashMap> loadAlternateDictionary() {
        String alternateDict = settings.getString(PluginParameter.ALTERNATIVE_DICTIONARY_PROPERTY_KEY);
        Reader reader;
        Optional<SpellDictionaryHashMap> result = Optional.absent();
        if (alternateDict != null) {
            alternateDict = alternateDict.replace(PluginParameter.SEPARATOR_CHAR, "\n");
            reader = new StringReader(alternateDict);
            try {
                result = Optional.of(new SpellDictionaryHashMap(reader));
            } catch (IOException e) {
                throw new UnableToLoadDictionary("Can't load alternate dictionary.", e);
            }
        }
        return result;
    }
}

