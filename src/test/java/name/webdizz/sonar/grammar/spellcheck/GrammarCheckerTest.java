package name.webdizz.sonar.grammar.spellcheck;

import com.swabunga.spell.engine.SpellDictionary;
import com.swabunga.spell.engine.SpellDictionaryHashMap;
import com.swabunga.spell.event.SpellCheckEvent;
import com.swabunga.spell.event.SpellCheckListener;
import com.swabunga.spell.event.SpellChecker;
import com.swabunga.spell.event.StringWordTokenizer;
import com.swabunga.spell.event.WordTokenizer;
import org.apache.commons.collections.CollectionUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.List;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class GrammarCheckerTest {

    @Mock
    private GrammarDictionaryLoader dictionary;

    @Mock
    private SpellCheckListener listener;

    private GrammarChecker testingInstance;

    @Before
    public void setUp() {
        testingInstance = new GrammarChecker(dictionary);
    }

    @Test
    public void shouldCallForDictionaryLoad() {
        new GrammarChecker(dictionary);
        verify(dictionary, atLeast(1)).load();
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldNotCheckSpellingForGivenEmptyInput() {
        testingInstance.checkSpelling("", listener);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldNotProceedSpellCheckingWithoutListener() {
        testingInstance.checkSpelling("", null);
    }

    @Test
    public void shouldSpellCheck() {
        String input = "package name;  class HelloTlansformer { HelloTlansformer(){super();} public void hello(){} \n public void good(){} }";
        SpellDictionary spellDictionary = mock(SpellDictionary.class);
        when(dictionary.load()).thenReturn(spellDictionary);

        testingInstance = new GrammarChecker(dictionary);

        testingInstance.checkSpelling(input, listener);
        verify(listener, atLeast(1)).spellingError(any(SpellCheckEvent.class));
    }

    private static class GrammarCheckerDemo implements SpellCheckListener {
        private static final Logger LOGGER = LoggerFactory.getLogger(GrammarCheckerDemo.class);

        private static final String DICT_FILE = "dict/english.0";

        private SpellChecker spellCheck;

        public GrammarCheckerDemo(final String input) {
            try {
                SpellDictionary dictionary = new SpellDictionaryHashMap(new File(DICT_FILE));
                LOGGER.info("Dictionary was loaded with custom.");

                spellCheck = new SpellChecker(dictionary);
                spellCheck.addSpellCheckListener(this);
                spellCheck.getConfiguration().setInteger("SPELL_THRESHOLD", 1);

                WordTokenizer tokenizer = new StringWordTokenizer(input);

                spellCheck.checkSpelling(tokenizer);
            } catch (Exception e) {
                LOGGER.error("There was an error during grammar check", e);
            }
        }

        public void spellingError(final SpellCheckEvent event) {
            List suggestions = event.getSuggestions();
            if (CollectionUtils.isNotEmpty(suggestions)) {
                LOGGER.info("MISSPELT WORD: {} at {}", event.getInvalidWord(), event.getWordContextPosition());
                for (Object suggestion : suggestions) {
                    LOGGER.info("\nSuggested Word: {}", suggestion);
                }
            } else {
                LOGGER.info("MISSPELT WORD: {} at {}", event.getInvalidWord(), event.getWordContextPosition());
                LOGGER.info("\nNo suggestions");
            }
        }

    }
}
