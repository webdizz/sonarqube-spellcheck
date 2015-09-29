package com.epam.sonarqube.spellcheck.plugin.spellcheck.automaton;

/**
 * Represents Automaton, that breaks source code into separate words.
 * 
 * Must call init() before using this automaton.
 * 
 */
public interface WordFinderAutomaton {

    /**
     * Initialize automaton with states and rules for parsing source code
     */
    void init();

    /**
     * Return start position of word, which was found by previous call of
     * searchNextWord(...)
     * 
     * @return int start position of found word
     */
    int getWordStart();

    /**
     * Return end position of word, which was found by previous call of
     * searchNextWord(...)
     * 
     * @return int end position of found word
     */
    int getWordEnd();

    /**
     * Look for next word in given {@code text} starting from position
     * {@code beginIndex}.
     * 
     * @param text
     *            text, in which method will look for words
     * @param beginIndex
     *            the beginning index, inclusive
     */
    void searchNextWord(String text, int beginIndex);

    /**
     * Checks if previous call of searchNextWord(...) has found a word
     * 
     * @return true, if word was found; false - otherwise
     */
    boolean hasNextWord();

}