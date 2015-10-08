package com.epam.sonarqube.spellcheck.plugin.spellcheck;

import org.sonar.api.BatchExtension;
import org.sonar.api.config.Settings;

import com.epam.sonarqube.spellcheck.plugin.PluginParameter;
import com.epam.sonarqube.spellcheck.plugin.spellcheck.automaton.JavaCodeConventionEnglishAutomaton;
import com.epam.sonarqube.spellcheck.plugin.spellcheck.automaton.WordFinderAutomaton;
import com.swabunga.spell.event.AbstractWordFinder;
import com.swabunga.spell.event.Word;
import com.swabunga.spell.event.WordNotFoundException;

public class JavaSourceCodeWordFinder extends AbstractWordFinder implements
        BatchExtension {
    private int minimumWordLength;
    private Settings settings;

    public JavaSourceCodeWordFinder(final Settings settings) {
        this.settings = settings;
    }

    /**
     * This method scans the text from the end of the last word, and returns a
     * new Word object corresponding to the next word.
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

        if (nextWord == null) {
            throw new WordNotFoundException("No more words found.");
        }

        currentWord.copy(nextWord);

        setSentenceIterator(currentWord);
        
        do {
            nextWord = searchNextWord(nextWord, text);
        } while (nextWord != null && isWordLessThenMinLength(nextWord));

        return currentWord;
    }

    private Word searchNextWord(Word nextWord, String text) {
        Word newNextWord = new Word("", 0);
        int beginIndex = nextWord.getEnd();
        
        WordFinderAutomaton automaton = new JavaCodeConventionEnglishAutomaton();
        automaton.init();
   
        automaton.searchNextWord(text, beginIndex);
   
        if (!automaton.hasNextWord()) {
            newNextWord = null;
        } else {
            newNextWord.setStart(automaton.getWordStart());
            newNextWord.setText(text.substring(automaton.getWordStart(),
                    automaton.getWordEnd()));
        }
        
        return newNextWord;
    }

    private boolean isWordLessThenMinLength(final Word word) {
        return word.length() < minimumWordLength;
    }

}
