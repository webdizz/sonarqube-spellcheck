package com.epam.sonarqube.spellcheck.plugin.spellcheck;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
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

public class SpellDictionaryLoader implements BatchExtension {

    private final Lock locker = new ReentrantLock();
    private Settings settings;
    private AtomicReference<SpellDictionary> dictionary = new AtomicReference<>();

    // another dictionary for url
    private AtomicReference<SpellDictionary> urlDictionary = new AtomicReference<>();

    public SpellDictionaryLoader(Settings settings) {
        this.settings = settings;
    }

    public SpellDictionary loadURLDictionary(boolean forceLoad) {
        String urlDictionaryPath = settings.getString(PluginParameter.URL_DICTIONARY_PATH);
        Integer timeout = settings.getInt(PluginParameter.URL_DICTIONARY_TIMEOUT);
        SpellDictionary spellURLDictionary = urlDictionary.get();
        // return existed dictionary
        if(!forceLoad && (spellURLDictionary != null)) {
            return spellURLDictionary;
        }
        URLConnection conn = null;
        InputStreamReader wordList = null;
        locker.lock();
        try {
            conn = new URL(urlDictionaryPath).openConnection();
            conn.setConnectTimeout(timeout);
            conn.setReadTimeout(timeout);
            wordList = new InputStreamReader(conn.getInputStream());
            spellURLDictionary = new SpellDictionaryHashMap(wordList);
            urlDictionary.set(spellURLDictionary);
            return spellURLDictionary;
        } catch (MalformedURLException e) {
            throw new UnableToLoadDictionary("Bad URL or there isn't such dictionary file", e);
        } catch (IOException e) {
            throw new UnableToLoadDictionary("Can't load dictionary from file", e);
        } finally {
            try {
                if(wordList != null) wordList.close();
            } catch (IOException e) {
            }
            locker.unlock();
        }
    }

    public SpellDictionary loadMainDictionary() {
        String dictionaryPath = settings.getString(PluginParameter.DICTIONARY_PATH);
        SpellDictionary spellDictionary = dictionary.get();
        locker.lock();
        try (InputStreamReader wordList = new InputStreamReader
                (SpellDictionaryLoader.class.getResourceAsStream(dictionaryPath))) {
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
            alternateDict = alternateDict.replace(" ", "");
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

