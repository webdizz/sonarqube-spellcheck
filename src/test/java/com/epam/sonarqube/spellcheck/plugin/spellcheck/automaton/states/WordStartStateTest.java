package com.epam.sonarqube.spellcheck.plugin.spellcheck.automaton.states;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class WordStartStateTest {

    private WordStartState wordStartState;

    @Test
    public void testWordStartStateName() throws Exception {
        wordStartState = new WordStartState("word start state");

        assertEquals("State: word start state", wordStartState.toString());
    }

}
