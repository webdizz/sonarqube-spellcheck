package com.epam.sonarqube.spellcheck.plugin.spellcheck.automaton.states;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
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
public class WordStartStateTest {
    @Mock
    private StateCallback callback;

    private WordStartState wordStartState;

    @Mock
    private JavaCodeConventionEnglishAutomaton automaton;

    private StateEvent stateEvent;

    @Before
    public void init() {
        stateEvent = new StateEvent(automaton);
    }

    @Test
    public void shouldCallStateCallbackWhenItIsNotNull() throws Exception {
        String stateName = "word start state";
        wordStartState = spy(new WordStartState(stateName, callback));

        wordStartState.callback(stateEvent);

        verify(callback).call(stateEvent);
    }

    @Test
    public void shouldNotCallStateCallbackWhenItIsNull() throws Exception {
        String stateName = "word start state";
        wordStartState = spy(new WordStartState(stateName));
        wordStartState.callback(stateEvent);

        verify(callback, times(0)).call(stateEvent);
    }

    @Test
    public void shouldUseConstructorWithTwoParamsAndSetCorrectStateName()
            throws Exception {
        String stateName = "word start state";
        String expectedStateName = "State: word start state";
        wordStartState = spy(new WordStartState(stateName, callback));

        assertEquals(expectedStateName, wordStartState.toString());
    }

    @Test
    public void shouldUseConstructorWithOneParamAndSetCorrectStateName()
            throws Exception {
        String stateName = "word start state";
        String expectedStateName = "State: word start state";
        wordStartState = new WordStartState(stateName);

        assertEquals(expectedStateName, wordStartState.toString());
    }

    @Test
    public void shouldCheckIfWordStartStateIsFinalAndReturnFalse() throws Exception {
        String stateName = "word start state";
        wordStartState = new WordStartState(stateName);

        assertFalse(wordStartState.isFinal());
    }

}
