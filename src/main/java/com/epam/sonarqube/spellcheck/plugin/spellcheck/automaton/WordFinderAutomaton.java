package com.epam.sonarqube.spellcheck.plugin.spellcheck.automaton;

public interface WordFinderAutomaton {

    /**
     * Initialize automaton with states and rules for parsing source code
     */
    public void init();
    
    /**
     * Return start position of word, which was found by previous call of
     * searchNextWord(...)
     * 
     * @return int start position of found word
     */
    public abstract int getWordStart();

    /**
     * Return end position of word, which was found by previous call of
     * searchNextWord(...)
     * 
     * @return int end position of found word
     */
    public abstract int getWordEnd();

    /**
     * Look for next word in given {@code text} starting from position
     * {@code beginIndex}.
     * 
     * @param text
     *            text, in which method will look for words
     * @param beginIndex
     *            the beginning index, inclusive
     */
    public abstract void searchNextWord(String text, int beginIndex);

    /**
     * Checks if previous call of searchNextWord(...) has found a word
     * 
     * @return true, if word was found; false - otherwise
     */
    public abstract boolean hasNextWord();

}