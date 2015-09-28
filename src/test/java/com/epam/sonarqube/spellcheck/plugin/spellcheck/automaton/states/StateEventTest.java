package com.epam.sonarqube.spellcheck.plugin.spellcheck.automaton.states;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import com.epam.sonarqube.spellcheck.plugin.spellcheck.automaton.JavaCodeConventionEnglishAutomaton;

public class StateEventTest {
    private JavaCodeConventionEnglishAutomaton automaton;
    
    private StateEvent stateEvent;

    @Before
    public void init() {
        automaton = new JavaCodeConventionEnglishAutomaton();
    }
    
    @Test
    public void testGetAutomaton() throws Exception {
        stateEvent = new StateEvent(automaton);
        assertEquals(automaton, stateEvent.getAutomaton());
    }

}
