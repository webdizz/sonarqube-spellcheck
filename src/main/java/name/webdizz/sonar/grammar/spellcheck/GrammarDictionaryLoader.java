package name.webdizz.sonar.grammar.spellcheck;

import com.sonar.sslr.api.Grammar;
import com.swabunga.spell.engine.SpellDictionary;
import com.swabunga.spell.engine.SpellDictionaryHashMap;
import name.webdizz.sonar.grammar.GrammarPlugin;
import name.webdizz.sonar.grammar.exceptions.UnableToLoadDictionary;
import name.webdizz.sonar.grammar.utils.SpellCheckerUtil;
import org.picocontainer.annotations.Inject;
import org.sonar.api.BatchExtension;
import org.sonar.api.config.Settings;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class GrammarDictionaryLoader implements BatchExtension {

    private final Lock locker = new ReentrantLock();

    private AtomicReference<SpellDictionary> dictionary = new AtomicReference<>();
    private String dictionaryPath;

    public GrammarDictionaryLoader(Settings settings) {
        dictionaryPath = settings.getString(GrammarPlugin.DICTIONARY);
    }

    public SpellDictionary loadMainDictionary() {

        SpellDictionary spellDictionary = dictionary.get();
        try (InputStreamReader wordList = new InputStreamReader(SpellCheckerUtil.class.getResourceAsStream(dictionaryPath))) {
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

}
