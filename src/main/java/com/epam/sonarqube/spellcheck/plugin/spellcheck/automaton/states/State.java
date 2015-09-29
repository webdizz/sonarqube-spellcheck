package com.epam.sonarqube.spellcheck.plugin.spellcheck.automaton.states;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Class represents the state for finite state machine (automaton)
 */
public class State {

    /* Flag that shows if this state is final */
    protected boolean isFinalState = false;

    /* Name of state. For logging purposes */
    private String name;

    /* Map of all transitions */
    private Map<Character, State> transitions;

    /* Default transition */
    private State elseState;

    /*
     * Callback method of state. Usually called by Automaton which uses this
     * state
     */
    private StateCallback callback;

    public State(String name) {
        this.name = name;
        this.transitions = new ConcurrentHashMap<Character, State>();
    }

    public State(String name, StateCallback callback) {
        this(name);
        this.callback = callback;
    }

    /**
     * Add transition rule
     * 
     * @param ch
     *            char, which will trigger transition
     * @param state
     *            new state, where we will move by given trigger
     */
    public void addTransition(Character ch, State state) {
        transitions.put(ch, state);
    }

    /**
     * Add transition rule using string
     * 
     * @param str
     *            each char in {@code str} will be added as trigger for
     *            transition
     * @param state
     *            new state, where we will move by given trigger
     */
    public void addTransition(String str, State state) {
        for (Character ch : str.toCharArray()) {
            transitions.put(ch, state);
        }
    }

    /**
     * Do transition from current state to new state
     * 
     * @param ch
     *            char to trigger transition
     * @return new state
     */
    public State next(char ch) {
        State newState = transitions.get(ch);

        if (newState == null) {
            newState = elseState;
        }
        return newState;
    }

    /**
     * Default transition. Will be used if no acceptable transition will be
     * found.
     * 
     * @param state
     *            state, where we will move, if no acceptable transition will be
     *            found
     */
    public void elseTransition(State state) {
        this.elseState = state;
    }

    /**
     * Call callback method of state.
     * 
     * @param stateEvent
     *            data object, that contains reference to Automaton and other
     *            related to this callback parameters, which will be used in
     *            callback
     */
    public void callback(StateEvent stateEvent) {
        if (callback != null) {
            callback.call(stateEvent);
        }
    }

    /**
     * Check if this state is final
     * 
     * @return boolean true, if state is final; false - otherwise
     */
    public boolean isFinal() {
        return isFinalState;
    }

    @Override
    public String toString() {
        return "State: " + name;
    }

}
