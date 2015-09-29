package com.epam.sonarqube.spellcheck.plugin.spellcheck.automaton.states;

import com.epam.sonarqube.spellcheck.plugin.spellcheck.automaton.JavaCodeConventionEnglishAutomaton;

public class StateEvent {

    private final JavaCodeConventionEnglishAutomaton automaton;
    
    public StateEvent(JavaCodeConventionEnglishAutomaton automaton) {
        this.automaton = automaton;
    }

    public JavaCodeConventionEnglishAutomaton getAutomaton() {
        return automaton;
    }

}
