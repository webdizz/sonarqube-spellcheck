package com.epam.sonarqube.spellcheck.plugin.spellcheck.automaton.states;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

public class StateTest {

    private State state;

    @Before
    public void init() {
        state = new State("testState");
    }

    @Test
    public void testNextWhenTransitionsIsEmpty() throws Exception {
        char anyChar = 'a';

        assertNull(state.next(anyChar));
    }

    @Test
    public void testNextWhenElseTransitionIsEmpty() throws Exception {
        char transitionChar = 'a';
        State expectedState = state;
        state.addTransition(transitionChar, expectedState);

        State actualState = state.next(transitionChar);

        assertEquals(expectedState, actualState);
    }

    @Test
    public void testNextWhenElseTransitionNonEmpty() throws Exception {
        char transitionChar = 'a';
        State expectedElseState = new State("StateTo");

        state.elseTransition(expectedElseState);

        State actualElseState = state.next(transitionChar);

        assertEquals(expectedElseState, actualElseState);
    }

    @Test
    public void testToString() throws Exception {
        String expectedString = "State: testState";

        assertEquals(expectedString, state.toString());
    }

    @Test
    public void testAddTwoTransitions() throws Exception {
        char transitionChar1 = 'a';
        char transitionChar2 = 'b';

        State stateTo1 = new State("stateTo1");
        State stateTo2 = new State("stateTo2");

        state.addTransition(transitionChar1, stateTo1);
        state.addTransition(transitionChar2, stateTo2);

        assertEquals(stateTo1, state.next(transitionChar1));
        assertNotEquals(stateTo1, state.next(transitionChar2));
        assertEquals(stateTo2, state.next(transitionChar2));
        assertNotEquals(stateTo2, state.next(transitionChar1));
    }

    @Test
    public void testAddStringTransitions() throws Exception {
        String transitions = "abc";

        State stateTo = new State("stateTo");

        state.addTransition(transitions, stateTo);

        assertEquals(stateTo, state.next(transitions.charAt(0)));
        assertEquals(stateTo, state.next(transitions.charAt(1)));
        assertEquals(stateTo, state.next(transitions.charAt(2)));
    }

    @Test
    public void testAddTwoStringTransitions() throws Exception {
        String transitions1 = "ab";
        String transitions2 = "cd";

        State stateTo1 = new State("stateTo1");
        State stateTo2 = new State("stateTo2");

        state.addTransition(transitions1, stateTo1);
        state.addTransition(transitions2, stateTo2);

        assertEquals(stateTo1, state.next(transitions1.charAt(0)));
        assertEquals(stateTo1, state.next(transitions1.charAt(1)));
        assertEquals(stateTo2, state.next(transitions2.charAt(0)));
        assertEquals(stateTo2, state.next(transitions2.charAt(1)));

        assertNotEquals(stateTo1, state.next(transitions2.charAt(0)));
        assertNotEquals(stateTo1, state.next(transitions2.charAt(1)));
        assertNotEquals(stateTo2, state.next(transitions1.charAt(0)));
        assertNotEquals(stateTo2, state.next(transitions1.charAt(1)));
    }

}
