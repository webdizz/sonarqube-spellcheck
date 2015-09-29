package com.epam.sonarqube.spellcheck.plugin.spellcheck.automaton.states;

import com.epam.sonarqube.spellcheck.plugin.spellcheck.automaton.JavaCodeConventionEnglishAutomaton;

public class WordStartStateEvent extends StateEvent {

    private final int index;
    
    public WordStartStateEvent(JavaCodeConventionEnglishAutomaton automaton, int index) {
        super(automaton);
        this.index = index;
    }

    public int getIndex() {
        return index;
    }
    
}
