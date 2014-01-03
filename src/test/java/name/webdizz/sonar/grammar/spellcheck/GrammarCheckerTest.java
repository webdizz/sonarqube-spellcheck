package name.webdizz.sonar.grammar.spellcheck;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.concurrent.atomic.AtomicInteger;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.swabunga.spell.engine.SpellDictionary;
import com.swabunga.spell.event.SpellCheckEvent;
import com.swabunga.spell.event.SpellCheckListener;

@RunWith(MockitoJUnitRunner.class)
public class GrammarCheckerTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(GrammarCheckerTest.class);

    @Mock
    private GrammarDictionaryLoader dictionaryLoader;

    @Mock
    private SpellCheckListener listener;

    private GrammarChecker testingInstance;

    @Before
    public void setUp() {
        testingInstance = new GrammarChecker(dictionaryLoader);
    }

    @Test
    public void shouldCallForDictionaryLoad() {
        new GrammarChecker(dictionaryLoader).initialize();
        verify(dictionaryLoader, atLeast(1)).load();
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
        String input = "package name.webdizz.sonar.grammar;\n" +
                "\n" +
                "\n import org.sonar.api.measures.CoreMetrics;\n /* commented code */  class HelloTlansformer { HelloTlansformer(){super();} public void hello(){} \n public void good(){ int i = 0; i++; (e);}\n return Arrays.asList(\n" +
                "                // Definitions\n" +
                "                GrammarMetrics.class,\n" +
                "                GrammarRuleRepository.class); }";
        SpellDictionary spellDictionary = mock(SpellDictionary.class);
        when(dictionaryLoader.load()).thenReturn(spellDictionary);

        testingInstance = new GrammarChecker(dictionaryLoader);
        testingInstance.initialize();
        final AtomicInteger counter = new AtomicInteger(0);
        testingInstance.checkSpelling(input, new SpellCheckListener() {
            @Override
            public void spellingError(final SpellCheckEvent event) {
                counter.incrementAndGet();
                LOGGER.info(event.getInvalidWord());
            }
        });
        assertEquals("Amount of tokens is wrong", 39, counter.get());
    }

}
