package com.epam.sonarqube.spellcheck.plugin.spellcheck.automaton.states;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import org.mockito.runners.MockitoJUnitRunner;

import com.epam.sonarqube.spellcheck.plugin.spellcheck.automaton.JavaCodeConventionEnglishAutomaton;

@RunWith(MockitoJUnitRunner.class)
public class FinalStateTest {
    @Mock
    private StateCallback callback;

    private FinalState finalState;
    
    @Mock
    private JavaCodeConventionEnglishAutomaton automaton;
    
    private StateEvent stateEvent;

    @Before
    public void init() {
        stateEvent = new StateEvent(automaton);
    }
    
    @Test
    public void testCallbackCallsBehavior() throws Exception {
        finalState = spy(new FinalState("finalState", callback));
        
        finalState.callback(stateEvent);

        verify(callback).call(stateEvent);
    }

    @Test
    public void testWhenCallbackIsNullBehavior() throws Exception {
        finalState = spy(new FinalState("finalState"));
        finalState.callback(stateEvent);

        verify(callback, times(0)).call(stateEvent);
    }

    @Test
    public void testConstructorWithTwoParamsName() throws Exception {
        finalState = spy(new FinalState("finalState", callback));

        assertEquals("State: finalState", finalState.toString());
    }

    @Test
    public void testConstructorWithTwoParamsCallbackCallsBehavior()
            throws Exception {
        finalState = spy(new FinalState("finalState", callback));

        finalState.callback(stateEvent);

        verify(callback).call(stateEvent);
    }

    @Test
    public void testName() throws Exception {
        finalState = spy(new FinalState("finalState"));

        assertEquals("State: finalState", finalState.toString());
    }

}
