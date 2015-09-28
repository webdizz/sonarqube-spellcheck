package com.epam.sonarqube.spellcheck.plugin.spellcheck.automaton.states;

/**
 * Represents final state of finite state machine(automaton)
 */
public class FinalState extends State {

    public FinalState(String name) {
        super(name);
        super.isFinalState = true;
    }

    public FinalState(String name, StateCallback callback) {
        super(name, callback);
        super.isFinalState = true;
    }
    

}
