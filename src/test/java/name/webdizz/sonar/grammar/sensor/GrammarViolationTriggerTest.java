package name.webdizz.sonar.grammar.sensor;

import com.swabunga.spell.event.SpellCheckEvent;
import java.util.Arrays;
import java.util.List;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.contains;
import static org.mockito.Matchers.eq;
import org.mockito.Mockito;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GrammarViolationTriggerTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(GrammarViolationTriggerTest.class);


    private static final String INVALID_WORD = "test";
    private static final int INVALID_WORD_POSITION = 100;

    private final SpellCheckEvent event = mock(SpellCheckEvent.class);
    private final GrammarIssuesWrapper lineWrapper = prepareMockedGrammarIssuesWrapper();
    private final GrammarViolationTrigger instance = new GrammarViolationTrigger(lineWrapper);

    /**
     * Test of spellingError method, of class GrammarViolationTrigger.
     */
    @Test
    public void testSpellingError() {
        LOGGER.info("Testing spellingError");

        instance.spellingError(event);

        // check the behavior
        verify(event, atLeastOnce()).getInvalidWord();
        verify(event, atLeastOnce()).getWordContextPosition();
        verify(lineWrapper).incident(contains(INVALID_WORD), eq(INVALID_WORD_POSITION));

        LOGGER.info("Done.");
    }

    // private methods
    private GrammarIssuesWrapper prepareMockedGrammarIssuesWrapper() {
        when(event.getInvalidWord()).thenReturn(INVALID_WORD);
        when(event.getWordContextPosition()).thenReturn(INVALID_WORD_POSITION);
        final List suggesstions = Arrays.asList(new String[]{"suggestion1", "suggestion2"});
        when(event.getSuggestions()).thenReturn(suggesstions);

        GrammarIssuesWrapper mockedLineWrapper = mock(GrammarIssuesWrapper.class);
        when(mockedLineWrapper.getLine()).thenReturn(INVALID_WORD);
        when(mockedLineWrapper.getKey()).thenReturn("/src/test.code");
        
        doAnswer(new Answer() {

            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                Object[] args = invocation.getArguments();
                String message = (String) args[0];
                // check the values
                assertTrue(message.contains(INVALID_WORD));
                for (Object suggestion : suggesstions) {
                    assertTrue(message.contains(suggestion.toString()));
                }
                assertEquals(INVALID_WORD_POSITION, args[1]);

                LOGGER.info("incident(" + message + "," + INVALID_WORD_POSITION + ")");
                return "Good";
            }
        }).when(mockedLineWrapper).incident(anyString(), eq(INVALID_WORD_POSITION));

        return mockedLineWrapper;
    }

}
