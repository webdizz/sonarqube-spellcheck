package com.epam.sonarqube.spellcheck.plugin.spellcheck.automaton.states;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Class represents the state for finite state machine (automaton)
 */
public class State {

    /* Name of state. For logging purposes */
    private String name;

    /* Map of all transitions */
    private Map<Character, State> transitions;
    /* Default transition */
    private State elseState;

    public State(String name) {
        this.name = name;
        this.transitions = new ConcurrentHashMap<Character, State>();
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

    @Override
    public String toString() {
        return "State: " + name;
    }

}
