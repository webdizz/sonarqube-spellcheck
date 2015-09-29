package com.epam.sonarqube.spellcheck.plugin.spellcheck.automaton.states;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.epam.sonarqube.spellcheck.plugin.spellcheck.automaton.JavaCodeConventionEnglishAutomaton;

public class WordStartStateEventTest {
    private JavaCodeConventionEnglishAutomaton automaton;

    private WordStartStateEvent wordStartStateEvent;

    @Test
    public void shouldCreateWordStartStateEventAndReturnProvidedIndex() throws Exception {
        int expectedIndex = 10;
        wordStartStateEvent = new WordStartStateEvent(automaton, expectedIndex);
        
        assertEquals(expectedIndex, wordStartStateEvent.getIndex());
    }

}
