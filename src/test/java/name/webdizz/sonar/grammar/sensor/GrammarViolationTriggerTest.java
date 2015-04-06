package name.webdizz.sonar.grammar.sensor;

import com.swabunga.spell.event.SpellCheckEvent;
import java.util.Arrays;
import java.util.List;
import static org.junit.Assert.*;
import org.junit.Test;
import static org.mockito.Mockito.*;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

/**
 *
 * @author Oleg_Sopilnyak1
 */
public class GrammarViolationTriggerTest {
    
    public GrammarViolationTriggerTest() {
    }

    /**
     * Test of spellingError method, of class GrammarViolationTrigger.
     */
    @Test
    public void testSpellingError() {
        System.out.println("Testing spellingError");
        SpellCheckEvent event = mock(SpellCheckEvent.class);
        final String invalidWord = "test";
        final int invalidPos = 100;
        when(event.getInvalidWord()).thenReturn(invalidWord);
        when(event.getWordContextPosition()).thenReturn(invalidPos);
        final List suggesstions = Arrays.asList(new String[]{"suggestion1","suggestion2"});
        when(event.getSuggestions()).thenReturn(suggesstions);
        GrammarIssuesWrapper lineWrapper = mock(GrammarIssuesWrapper.class);
        
        doAnswer(new Answer() {

            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                 Object[] args = invocation.getArguments();
                 String message = (String) args[0];
                 // check the values
                 assertTrue(message.contains(invalidWord));
                 for(Object suggestion : suggesstions){
                     assertTrue(message.contains(suggestion.toString()));
                 }
                 assertEquals(invalidPos, args[1]);
                 
                 System.out.println("incident("+message+","+invalidPos+")");
                 return "Good";
            }
        }).when(lineWrapper).incident(anyString(), eq(invalidPos));
        
        GrammarViolationTrigger instance = new GrammarViolationTrigger(lineWrapper);
        
        instance.spellingError(event);
        
        // check the behavior
        verify(event).getInvalidWord();
        verify(event).getWordContextPosition();
        verify(lineWrapper).incident(contains(invalidWord), eq(invalidPos));
        
        System.out.println("Done.");
    }
    
}
