package com.epam.sonarqube.spellcheck.plugin.spellcheck.automaton;

import com.epam.sonarqube.spellcheck.plugin.spellcheck.automaton.states.FinalState;
import com.epam.sonarqube.spellcheck.plugin.spellcheck.automaton.states.State;
import com.epam.sonarqube.spellcheck.plugin.spellcheck.automaton.states.StateCallback;
import com.epam.sonarqube.spellcheck.plugin.spellcheck.automaton.states.WordStartState;

/**
 * Breaks java source code into separate words accordingly to Java Code
 * Convention. Finds separate words in camelCase declarations, packages,
 * constant declarations, regular words. Accepts only words written in English
 * alphabet, other symbols will be skipped.
 * 
 * Not thread safe.
 *
 */
public class JavaCodeConventionEnglishAutomaton {

    private static final String ENGLISH_CAPITAL_LETTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String ENGLISH_SMALL_LETTERS = "abcdefghijklmnopqrstuvwxyz";

    private State current;
    private State init;

    private int wordStart;
    private int wordEnd;

    /**
     * Initialize automaton with states and rules for parsing java source code
     * written in English alphabet
     */
    public void init() {

        State stateSkipNonEnglishLetters = new State("SkipNonEnglishLetters");

        State finalStateRegular = new FinalState("finalRegular");

        final JavaCodeConventionEnglishAutomaton automaton = this;
        State finalStateCapitalLettersAbbreviation = new FinalState(
                "finalCapitalLettersAbbreviation", new StateCallback() {
                    @Override
                    public void call() {
                        automaton.wordEnd--;
                    }
                });

        State stateStartSmallLetterWord = new WordStartState(
                "StartSmallLetterWord");
        State stateProcessSmallLetters = new State("ProcessSmallLetters");
        State stateStartCapitalLetterWord = new WordStartState(
                "StartCapitalLetterWord");
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

    /**
     * Return start position of word, which was found by previous call of
     * searchNextWord(...)
     * 
     * @return int start position of found word
     */
    public int getWordStart() {
        return wordStart;
    }

    /**
     * Return end position of word, which was found by previous call of
     * searchNextWord(...)
     * 
     * @return int end position of found word
     */
    public int getWordEnd() {
        return wordEnd;
    }

    /**
     * Look for next word in given {@code text} starting from position
     * {@code beginIndex}.
     * 
     * @param text
     *            text, in which method will look for words
     * @param beginIndex
     *            the beginning index, inclusive
     */
    public void searchNextWord(String text, int beginIndex) {
        reset();
        String tmpText = text + " ";
        int i = beginIndex;
        while (i < tmpText.length()) {

            char ch = tmpText.charAt(i);

            if (isWordStartState()) {
                wordStart = i - 1;
            }
            wordEnd = i;

            next(ch);

            if (isFinalState()) {
                processFinalState();
                break;
            }

            i++;
        }
    }

    /**
     * Checks if previous call of searchNextWord(...) has found a word
     * 
     * @return true, if word was found; false - otherwise
     */
    public boolean hasNextWord() {
        return wordStart != -1;
    }

    private void processFinalState() {
        ((FinalState) current).callback();
    }

    private void next(char ch) {
        current = current.next(ch);
    }

    /**
     * Reset automaton state to initial
     */
    private void reset() {
        current = init;
        wordStart = -1;
        wordEnd = 0;
    }

    /**
     * Check if current state is Final
     * 
     * @return true, if current state is Final; false - otherwise
     */
    private boolean isFinalState() {
        return current instanceof FinalState;
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
