package com.epam.sonarqube.spellcheck.plugin.spellcheck.automaton.states;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.epam.sonarqube.spellcheck.plugin.spellcheck.automaton.JavaCodeConventionEnglishAutomaton;

public class StateEventTest {

    private StateEvent stateEvent;

    @Test
    public void shouldCreateStateEventAndReturnAutomaton() throws Exception {
        JavaCodeConventionEnglishAutomaton automaton = new JavaCodeConventionEnglishAutomaton();
        JavaCodeConventionEnglishAutomaton expectedAutomaton = automaton;
        
        stateEvent = new StateEvent(automaton);

        assertEquals(expectedAutomaton, stateEvent.getAutomaton());
    }

}
