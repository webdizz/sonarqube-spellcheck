package com.epam.sonarqube.spellcheck.plugin.spellcheck.automaton.states;

/**
 * Represents state in which parsed word has been started.
 */
public class WordStartState extends State {

    public WordStartState(String name) {
        super(name);
    }
    
    public WordStartState(String name, StateCallback stateCallback) {
        super(name, stateCallback);
    }

}
