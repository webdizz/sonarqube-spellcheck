package com.epam.sonarqube.spellcheck.plugin.spellcheck.filter;

/**
 * Interface WordFilter provides filtering capabilities for separate words
 */
public interface WordFilter {
    /**
     * Method tests if {@code word} is accepted by this filter
     *
     * @param word word to be checked by filter
     * @return true, if filter accepts this word; false - otherwise
     */
    boolean accept(String word);
}
