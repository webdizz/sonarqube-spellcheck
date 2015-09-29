package com.epam.sonarqube.spellcheck.plugin.spellcheck.automaton;

import com.epam.sonarqube.spellcheck.plugin.spellcheck.automaton.states.FinalState;
import com.epam.sonarqube.spellcheck.plugin.spellcheck.automaton.states.State;
import com.epam.sonarqube.spellcheck.plugin.spellcheck.automaton.states.StateCallback;
import com.epam.sonarqube.spellcheck.plugin.spellcheck.automaton.states.StateEvent;
import com.epam.sonarqube.spellcheck.plugin.spellcheck.automaton.states.WordStartState;
import com.epam.sonarqube.spellcheck.plugin.spellcheck.automaton.states.WordStartStateEvent;

/**
 * Breaks java source code into separate words accordingly to Java Code
 * Convention. Finds separate words in camelCase declarations, packages,
 * constant declarations, regular words. Accepts only words written in English
 * alphabet, other symbols will be skipped.
 * 
 * Must call init() before using this automaton.
 * 
 * Not thread safe.
 *
 */
public class JavaCodeConventionEnglishAutomaton implements WordFinderAutomaton {

    private static final String ENGLISH_CAPITAL_LETTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String ENGLISH_SMALL_LETTERS = "abcdefghijklmnopqrstuvwxyz";

    private State current;
    private State init;

    private int wordStart;
    private int wordEnd;
    
    private boolean foundNextWord;

    /**
     * Initialize automaton with states and rules for parsing java source code
     * written in English alphabet
     */
    @Override
    public void init() {

        State stateSkipNonEnglishLetters = new State("SkipNonEnglishLetters");

        State finalStateRegular = new FinalState("finalRegular");

        State finalStateCapitalLettersAbbreviation = new FinalState(
                "finalCapitalLettersAbbreviation", new StateCallback() {
                    @Override
                    public void call(StateEvent stateEvent) {
                        stateEvent.getAutomaton().wordEnd--;
                    }
                });

        StateCallback wordStartCallback = new StateCallback() {
            @Override
            public void call(StateEvent stateEvent) {
                if (stateEvent instanceof WordStartStateEvent) {
                    WordStartStateEvent wordStartStateEvent = (WordStartStateEvent)stateEvent;
                    wordStartStateEvent.getAutomaton().wordStart = wordStartStateEvent.getIndex() - 1;
                }
            }
        };
        State stateStartSmallLetterWord = new WordStartState(
                "StartSmallLetterWord", wordStartCallback);
        State stateProcessSmallLetters = new State("ProcessSmallLetters");
        State stateStartCapitalLetterWord = new WordStartState(
                "StartCapitalLetterWord", wordStartCallback);
        State stateProcessCapitalLetters = new State("ProcessCapitalLetters");

        stateSkipNonEnglishLetters.addTransition(ENGLISH_SMALL_LETTERS,
                stateStartSmallLetterWord);
        stateSkipNonEnglishLetters.addTransition(ENGLISH_CAPITAL_LETTERS,
                stateStartCapitalLetterWord);
        stateSkipNonEnglishLetters.elseTransition(stateSkipNonEnglishLetters);

        stateStartSmallLetterWord.addTransition(ENGLISH_SMALL_LETTERS,
                stateProcessSmallLetters);
        stateStartSmallLetterWord.elseTransition(finalStateRegular);

        stateProcessSmallLetters.addTransition(ENGLISH_SMALL_LETTERS,
                stateProcessSmallLetters);
        stateProcessSmallLetters.elseTransition(finalStateRegular);

        stateStartCapitalLetterWord.addTransition(ENGLISH_CAPITAL_LETTERS,
                stateProcessCapitalLetters);
        stateStartCapitalLetterWord.addTransition(ENGLISH_SMALL_LETTERS,
                stateProcessSmallLetters);
        stateStartCapitalLetterWord.elseTransition(finalStateRegular);

        stateProcessCapitalLetters.addTransition(ENGLISH_CAPITAL_LETTERS,
                stateProcessCapitalLetters);
        stateProcessCapitalLetters.addTransition(ENGLISH_SMALL_LETTERS,
                finalStateCapitalLettersAbbreviation);
        stateProcessCapitalLetters.elseTransition(finalStateRegular);

        finalStateRegular.elseTransition(finalStateRegular);
        finalStateCapitalLettersAbbreviation
                .elseTransition(finalStateCapitalLettersAbbreviation);

        init = stateSkipNonEnglishLetters;
        reset();
    }

    /* (non-Javadoc)
     * @see com.epam.sonarqube.spellcheck.plugin.spellcheck.automaton.WordFinderAutomaton#getWordStart()
     */
    @Override
    public int getWordStart() {
        return wordStart;
    }

    /* (non-Javadoc)
     * @see com.epam.sonarqube.spellcheck.plugin.spellcheck.automaton.WordFinderAutomaton#getWordEnd()
     */
    @Override
    public int getWordEnd() {
        return wordEnd;
    }

    /* (non-Javadoc)
     * @see com.epam.sonarqube.spellcheck.plugin.spellcheck.automaton.WordFinderAutomaton#searchNextWord(java.lang.String, int)
     */
    @Override
    public void searchNextWord(String text, int beginIndex) {
        reset();
        String tmpText = text + " ";
        int i = beginIndex;
        while (i < tmpText.length()) {

            char ch = tmpText.charAt(i);

            if (isWordStartState()) {
                current.callback(new WordStartStateEvent(this, i));
                foundNextWord = true;
            }
            wordEnd = i;

            next(ch);

            if (isFinalState()) {
                current.callback(new StateEvent(this));
                break;
            }

            i++;
        }
    }

    /* (non-Javadoc)
     * @see com.epam.sonarqube.spellcheck.plugin.spellcheck.automaton.WordFinderAutomaton#hasNextWord()
     */
    @Override
    public boolean hasNextWord() {
        return foundNextWord;
    }

    private void next(char ch) {
        current = current.next(ch);
    }

    /**
     * Reset automaton state to initial
     */
    private void reset() {
        current = init;
        foundNextWord = false;
        wordStart = 0;
        wordEnd = 0;
    }

    /**
     * Check if current state is Final
     * 
     * @return true, if current state is Final; false - otherwise
     */
    private boolean isFinalState() {
        return current.isFinal();
    }

    /**
     * Check if current state is WordStart
     * 
     * @return true, if current state is WordStart; false - otherwise
     */
    private boolean isWordStartState() {
        return current instanceof WordStartState;
    }

}
