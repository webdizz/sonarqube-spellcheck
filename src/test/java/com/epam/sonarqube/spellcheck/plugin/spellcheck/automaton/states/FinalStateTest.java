package com.epam.sonarqube.spellcheck.plugin.spellcheck.automaton.states;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
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
    public void shouldCallStateCallbackWhenItIsNotNull() throws Exception {
        String stateName = "finalState";
        finalState = spy(new FinalState(stateName, callback));

        finalState.callback(stateEvent);

        verify(callback).call(stateEvent);
    }

    @Test
    public void shouldNotCallStateCallbackWhenItIsNull() throws Exception {
        String stateName = "finalState";
        finalState = spy(new FinalState(stateName));
        finalState.callback(stateEvent);

        verify(callback, times(0)).call(stateEvent);
    }

    @Test
    public void shouldUseConstructorWithTwoParamsAndSetCorrectStateName()
            throws Exception {
        String stateName = "finalState";
        String expectedStateName = "State: finalState";
        finalState = new FinalState(stateName, callback);

        assertEquals(expectedStateName, finalState.toString());
    }

    @Test
    public void shouldUseConstructorWithOneParamAndSetCorrectStateName()
            throws Exception {
        String stateName = "finalState";
        String expectedStateName = "State: finalState";
        finalState = new FinalState(stateName);

        assertEquals(expectedStateName, finalState.toString());
    }

    @Test
    public void shouldCheckIfFinalStateIsFinalAndReturnTrue() throws Exception {
        String stateName = "finalState";
        finalState = new FinalState(stateName);

        assertTrue(finalState.isFinal());
    }

}
