package com.epam.sonarqube.spellcheck.plugin.spellcheck.automaton.states;

import static org.junit.Assert.assertEquals;
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
    public void testWordStartStateName() throws Exception {
        wordStartState = new WordStartState("word start state");

        assertEquals("State: word start state", wordStartState.toString());
    }
    
    @Test
    public void testCallbackCallsBehavior() throws Exception {
        wordStartState = spy(new WordStartState("word start state", callback));
        
        wordStartState.callback(stateEvent);

        verify(callback).call(stateEvent);
    }

    @Test
    public void testWhenCallbackIsNullBehavior() throws Exception {
        wordStartState = spy(new WordStartState("word start state"));
        wordStartState.callback(stateEvent);

        verify(callback, times(0)).call(stateEvent);
    }

    @Test
    public void testConstructorWithTwoParamsName() throws Exception {
        wordStartState = spy(new WordStartState("word start state", callback));

        assertEquals("State: word start state", wordStartState.toString());
    }
    
    @Test
    public void testConstructorWithTwoParamsCallbackCallsBehavior()
            throws Exception {
        wordStartState = spy(new WordStartState("word start state", callback));

        wordStartState.callback(stateEvent);

        verify(callback).call(stateEvent);
    }

}
