package name.webdizz.sonar.grammar.spellcheck;

import static com.google.common.base.Preconditions.checkArgument;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Strings;
import com.swabunga.spell.engine.SpellDictionary;
import com.swabunga.spell.event.SpellCheckListener;
import com.swabunga.spell.event.SpellChecker;

public class GrammarChecker {

    public static final String DEFAULT_DICT_PATH = "src/main/resources/dict/english.0";
    private static final Logger LOGGER = LoggerFactory.getLogger(GrammarChecker.class);
    private SpellDictionary dictionary;
    private GrammarDictionaryLoader dictionaryLoader;
    private int minimumWordLengths;

    public GrammarChecker(final GrammarDictionaryLoader dictionaryLoader, int minimumWordLengths) {
        this.dictionaryLoader = dictionaryLoader;
        this.minimumWordLengths = minimumWordLengths;
    }

    public void initialize() {
        dictionary = dictionaryLoader.loadMainDictionary();
    }

    public void checkSpelling(final String inputLine, final SpellCheckListener spellCheckListener) {
        parametersValidation(inputLine, spellCheckListener);
        SpellChecker spellCheck = createSpellChecker(spellCheckListener);
        JavaSourceCodeWordFinder javaSourceCodeWordFinder = new JavaSourceCodeWordFinder();
        javaSourceCodeWordFinder.setMinimumWordLength(minimumWordLengths);
        JavaSourceCodeTokenizer sourceCodeTokenizer = new JavaSourceCodeTokenizer(inputLine,javaSourceCodeWordFinder);
        spellCheck.checkSpelling(sourceCodeTokenizer);
        spellCheck.reset();
    }

    private void parametersValidation(final String inputLine, final SpellCheckListener spellCheckListener) {
        checkArgument(spellCheckListener != null, "Cannot proceed with spell checking without SpellCheckListener");
        checkArgument(!Strings.isNullOrEmpty(inputLine), "Cannot proceed with spell checking for empty inputLine");
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Is about to check spelling for \n{} \n and record with {}", inputLine, spellCheckListener);
        }
    }

    private SpellChecker createSpellChecker(final SpellCheckListener spellCheckListener) {
        SpellChecker spellCheck = new SpellCheckerFactory().getSpellChecker();
        spellCheck.addSpellCheckListener(spellCheckListener);
        spellCheck.getConfiguration().setInteger("SPELL_THRESHOLD", 1);
        spellCheck.setUserDictionary(dictionary);
        return spellCheck;
    }

}
