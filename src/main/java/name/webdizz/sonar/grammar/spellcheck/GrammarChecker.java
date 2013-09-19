package name.webdizz.sonar.grammar.spellcheck;

import com.google.common.base.Strings;
import com.swabunga.spell.engine.SpellDictionary;
import com.swabunga.spell.event.SpellCheckListener;
import com.swabunga.spell.event.SpellChecker;
import com.swabunga.spell.event.StringWordTokenizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.google.common.base.Preconditions.checkArgument;

public class GrammarChecker {

    private static final Logger LOGGER = LoggerFactory.getLogger(GrammarChecker.class);

    private final GrammarDictionaryLoader dictionaryLoader;

    private final SpellDictionary dictionary;

    public GrammarChecker(final GrammarDictionaryLoader dictionaryLoader) {
        this.dictionaryLoader = dictionaryLoader;
        dictionary = dictionaryLoader.load();
    }

    public void checkSpelling(final String input, final SpellCheckListener spellCheckListener) {
        checkArgument(spellCheckListener != null, "Cannot proceed with spell checking without SpellCheckListener");
        checkArgument(!Strings.isNullOrEmpty(input), "Cannot proceed with spell checking for empty input");
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Is about to check spelling for \n{} \n and record with {}", input, spellCheckListener);
        }
        SpellChecker spellCheck;
        spellCheck = new SpellChecker(dictionary);
        spellCheck.addSpellCheckListener(spellCheckListener);
        spellCheck.getConfiguration().setInteger("SPELL_THRESHOLD", 1);
        spellCheck.checkSpelling(new StringWordTokenizer(input));
    }

}
