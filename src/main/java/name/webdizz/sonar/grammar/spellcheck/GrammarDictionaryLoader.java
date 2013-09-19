package name.webdizz.sonar.grammar.spellcheck;

import com.google.common.base.Strings;
import com.swabunga.spell.engine.SpellDictionary;
import com.swabunga.spell.engine.SpellDictionaryHashMap;
import name.webdizz.sonar.grammar.GrammarPlugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.config.Settings;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicReference;

public class GrammarDictionaryLoader {

    private static final Logger LOGGER = LoggerFactory.getLogger(GrammarDictionaryLoader.class);

    private final Object locker = new Object();

    private Settings settings;

    private AtomicReference<SpellDictionary> dictionary = new AtomicReference<SpellDictionary>();

    public GrammarDictionaryLoader(final Settings settings) {
        this.settings = settings;
    }

    public SpellDictionary load() {
        String dictionaryPath = settings.getString(GrammarPlugin.DICTIONARY);
        SpellDictionary spellDictionary = dictionary.get();
        if (!Strings.isNullOrEmpty(dictionaryPath)) {
            spellDictionary = loadSpellDictionary(dictionaryPath);
        }
        return spellDictionary;
    }

    private SpellDictionary loadSpellDictionary(final String dictionaryPath) {
        try {
            synchronized (locker) {
                if (null == dictionary.get()) {
                    dictionary.set(new SpellDictionaryHashMap(new File(dictionaryPath)));
                    LOGGER.info("Dictionary was loaded.");
                }
            }
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
