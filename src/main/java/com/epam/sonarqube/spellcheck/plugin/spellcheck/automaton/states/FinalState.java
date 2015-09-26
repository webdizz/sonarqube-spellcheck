package com.epam.sonarqube.spellcheck.plugin.spellcheck.automaton.states;

/**
 * Represents final state of finite state machine(automaton)
 */
public class FinalState extends State {

    private StateCallback callback;

    public FinalState(String name) {
        super(name);
    }

    public FinalState(String name, StateCallback callback) {
        super(name);
        this.callback = callback;
    }

    public void callback() {
        if (callback != null) {
            callback.call();
        }
    }

}
