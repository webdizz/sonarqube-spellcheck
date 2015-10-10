package com.epam.sonarqube.spellcheck.plugin.spellcheck;

import java.util.Iterator;
import java.util.List;

import org.sonar.api.BatchExtension;
import org.sonar.api.config.Settings;

import com.epam.sonarqube.spellcheck.plugin.PluginParameter;
import com.swabunga.spell.event.AbstractWordFinder;
import com.swabunga.spell.event.Word;
import com.swabunga.spell.event.WordNotFoundException;

public class JavaSourceCodeWordFinder extends AbstractWordFinder implements
        BatchExtension {
    private int minimumWordLength;
    private Settings settings;
    private List<String> words;
    private Iterator<String> wordsIterator;
    
    private static final Word FAKE_WORD_PARAM_WHICH_IS_NOT_USED_IN_METHOD = null;
    private static final int FAKE_WORD_START_TO_SKIP_START_SENTENCE_CHECK = 1; // should != 0 
    
    private String oldText;

    public JavaSourceCodeWordFinder(final Settings settings) {
        this.settings = settings;
    }
    
    /**
     * Method iterates over list of split words and returns 
     * a new Word object corresponding to the next word.
     *
     * @return the next word.
     * @throws com.swabunga.spell.event.WordNotFoundException
     *             search string contains no more words.
     */
    @Override
    public Word next() {
        if (settings != null) {
            minimumWordLength = settings
                    .getInt(PluginParameter.SPELL_MINIMUM_WORD_LENGTH);
        }
        
        //if text for finding words was changed (e.g. via setText(...)),
        //then split it into separate words
        if (text != oldText) {
            eagerSplitTextIntoWords();
            oldText = text;
        }

        if (nextWord == null) {
            throw new WordNotFoundException("No more words found.");
        }

        currentWord.copy(nextWord);
        
        //need to call this method, to skip "Start Sentence Check" in Jazzy
        setSentenceIterator(FAKE_WORD_PARAM_WHICH_IS_NOT_USED_IN_METHOD);
        
        nextWord = searchNextWord();

        return currentWord;
    }

    private Word searchNextWord() {
        Word newNextWord = null;
        String word;
        
        while (wordsIterator.hasNext()) {
            word = wordsIterator.next();
            if (isWordGreaterOrEqualThenMinLength(word)) {
                newNextWord = new Word(word, FAKE_WORD_START_TO_SKIP_START_SENTENCE_CHECK);
                break;
            }
        }
        
        return newNextWord;
    }
    
    private boolean isWordGreaterOrEqualThenMinLength(final String word) {
        return word.length() >= minimumWordLength;
    }

    private void eagerSplitTextIntoWords() {
        words = new JavaSourceCodeSplitter().split(text);
        wordsIterator = words.iterator();
    }
}
