package name.webdizz.sonar.grammar.spellcheck;

import com.google.common.base.Charsets;
import com.google.common.base.Strings;
import com.google.common.io.Files;
import com.swabunga.spell.engine.SpellDictionary;
import com.swabunga.spell.engine.SpellDictionaryHashMap;
import name.webdizz.sonar.grammar.GrammarPlugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.config.Settings;

import java.io.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class GrammarDictionaryLoader {

    private static final Logger LOGGER = LoggerFactory.getLogger(GrammarDictionaryLoader.class);

    private final Lock locker = new ReentrantLock();

    private Settings settings;

    private AtomicReference<SpellDictionary> dictionary = new AtomicReference<SpellDictionary>();

    public GrammarDictionaryLoader(final Settings settings) {
        this.settings = settings;
    }

    public SpellDictionary load() {
        String dictionaryPath = settings.getString( GrammarPlugin.DICTIONARY );
        SpellDictionary spellDictionary = dictionary.get();
        try {
            locker.lock();
            if (null == spellDictionary) {
                if (!Strings.isNullOrEmpty(dictionaryPath) && new File(dictionaryPath).exists()) {
                    try {
                        BufferedReader bufferedReader = null;
                        bufferedReader = Files.newReader( new File( dictionaryPath ), Charsets.UTF_8 );
                        spellDictionary = loadSpellDictionary(bufferedReader, dictionaryPath);
                    } catch (FileNotFoundException e) {
                        throw new UnableToLoadDictionary("There is no file with dictionary.", e);
                    }
                } else {
                    try {
                        dictionaryPath = "/" + GrammarChecker.DEFAULT_DICT_PATH;
                        InputStream inputStream = this.getClass().getResourceAsStream(dictionaryPath);

                        BufferedReader dictionaryReader = null;
                        dictionaryReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
                        spellDictionary = loadSpellDictionary(dictionaryReader, dictionaryPath);
                    } catch (UnsupportedEncodingException e) {
                        throw new UnableToLoadDictionary("Unable to read dictionary file as UTF-8.", e);
                    }
                }
            }
            return spellDictionary;
        } finally {
            locker.unlock();
        }
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
