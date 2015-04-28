package name.webdizz.sonar.grammar.spellcheck;

import org.sonar.api.BatchExtension;

import com.google.common.base.Optional;
import com.swabunga.spell.engine.SpellDictionary;
import com.swabunga.spell.engine.SpellDictionaryHashMap;
import name.webdizz.sonar.grammar.PluginParameter;
import name.webdizz.sonar.grammar.exceptions.UnableToLoadDictionary;
import name.webdizz.sonar.grammar.utils.SpellCheckerUtil;

import org.sonar.api.config.Settings;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class GrammarDictionaryLoader implements BatchExtension {

    private final Lock locker = new ReentrantLock();
    private Settings settings;
    private AtomicReference<SpellDictionary> dictionary = new AtomicReference<>();
    private String dictionaryPath = "/dict/english.0";

    public GrammarDictionaryLoader(Settings settings) {
        this.settings = settings;

    }


    public SpellDictionary loadMainDictionary() {
        String dictionaryPath = settings.getString(PluginParameter.DICTIONARY_PATH);
        SpellDictionary spellDictionary = dictionary.get();
        try (InputStreamReader wordList = new InputStreamReader
                (SpellCheckerUtil.class.getResourceAsStream(dictionaryPath))) {
            locker.lock();
            if (null == spellDictionary) {
                spellDictionary = new SpellDictionaryHashMap(wordList);
                dictionary.set(spellDictionary);
            }
            return spellDictionary;
        } catch (IOException e) {
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

