package name.webdizz.sonar.grammar.spellcheck;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.concurrent.atomic.AtomicReference;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.config.Settings;
import name.webdizz.sonar.grammar.GrammarPlugin;

import com.google.common.base.Strings;
import com.swabunga.spell.engine.SpellDictionary;
import com.swabunga.spell.engine.SpellDictionaryHashMap;

public class GrammarDictionaryLoader {

    private static final Logger LOGGER = LoggerFactory.getLogger(GrammarDictionaryLoader.class);
    private static final String DEFAULT_DICT_PATH = "dict/english.0";

    private final Object locker = new Object();

    private Settings settings;

    private AtomicReference<SpellDictionary> dictionary = new AtomicReference<SpellDictionary>();

    public GrammarDictionaryLoader(final Settings settings) {
        this.settings = settings;
    }

    public SpellDictionary load() {
        String dictionaryPath = settings.getString(GrammarPlugin.DICTIONARY);
        SpellDictionary spellDictionary = dictionary.get();
        //TODO: use Guava for IO
        synchronized (locker) {
            if (null == spellDictionary) {
                if (!Strings.isNullOrEmpty(dictionaryPath) && new File(dictionaryPath).exists()) {
                    try {
                        FileReader fileReader = null;
                        fileReader = new FileReader(new File(dictionaryPath));
                        spellDictionary = loadSpellDictionary(fileReader, dictionaryPath);
                    } catch (FileNotFoundException e) {
                        throw new UnableToLoadDictionary("There is no file with dictionary.", e);
                    }
                } else {
                    try {
                        dictionaryPath = "/" + DEFAULT_DICT_PATH;
                        InputStream inputStream = this.getClass().getResourceAsStream(dictionaryPath);

                        BufferedReader dictionaryReader = null;
                        dictionaryReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
                        spellDictionary = loadSpellDictionary(dictionaryReader, dictionaryPath);
                    } catch (UnsupportedEncodingException e) {
                        throw new UnableToLoadDictionary("Unable to read dictionary file as UTF-8.", e);
                    }
                }
            }
        }
        return spellDictionary;
    }

    private SpellDictionary loadSpellDictionary(final Reader dictionaryReader, final String dictionaryPath) {
        try {
            dictionary.set(new SpellDictionaryHashMap(dictionaryReader));
            LOGGER.info("Dictionary was loaded.");
        } catch (IOException e) {
            String message = String.format("Unable to load dictionary from %s", dictionaryPath);
            throw new UnableToLoadDictionary(message, e);
        }
        return dictionary.get();
    }

    static class UnableToLoadDictionary extends RuntimeException {
        UnableToLoadDictionary(final String message, final Throwable cause) {
            super(message, cause);
        }
    }
}
