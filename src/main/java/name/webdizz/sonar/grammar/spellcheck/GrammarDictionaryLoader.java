package name.webdizz.sonar.grammar.spellcheck;

import com.google.common.base.Strings;
import com.google.common.io.InputSupplier;
import com.google.common.io.Resources;
import com.swabunga.spell.engine.SpellDictionary;
import com.swabunga.spell.engine.SpellDictionaryHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.URL;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static com.google.common.base.Preconditions.checkNotNull;

public class GrammarDictionaryLoader {

    private static final Logger LOGGER = LoggerFactory.getLogger(GrammarDictionaryLoader.class);

    private final Lock locker = new ReentrantLock();

    private AtomicReference<SpellDictionary> dictionary = new AtomicReference<SpellDictionary>();


    public SpellDictionary load() {
        Properties properties = checkNotNull(readProperties("sonnar.grammar.properties"), "Unable to read plugin properties file.");
        String dictionaryPath =properties.getProperty("dictionary.default.path");
        SpellDictionary spellDictionary = dictionary.get();
        //TODO: use Guava for IO
        try {
            locker.lock();
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
    private Properties readProperties(String configFileName) {
        Properties properties = null;
        try {
        URL url = Resources.getResource(configFileName);
        InputSupplier<InputStream> inputSupplier = Resources.newInputStreamSupplier(url);

            properties = new Properties();
            properties.load(inputSupplier.getInput());
        } catch (Exception e) {
            LOGGER.error("Cannot find config file: " + configFileName);
        }
        return properties;
    }

    static class UnableToLoadDictionary extends RuntimeException {

        UnableToLoadDictionary(final String message, final Throwable cause) {
            super(message, cause);
        }
    }
}
