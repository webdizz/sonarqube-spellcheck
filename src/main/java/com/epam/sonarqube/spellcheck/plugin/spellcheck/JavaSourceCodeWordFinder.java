package com.epam.sonarqube.spellcheck.plugin.spellcheck;

import org.sonar.api.BatchExtension;
import org.sonar.api.config.Settings;

import com.epam.sonarqube.spellcheck.plugin.PluginParameter;
import com.epam.sonarqube.spellcheck.plugin.spellcheck.automaton.JavaCodeConventionEnglishAutomaton;
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
                    .getInt(PluginParameter.SPELL_MINIMUMWORDLENGTH);
        }

        if (nextWord == null) {
            throw new WordNotFoundException("No more words found.");
        }

        currentWord.copy(nextWord);

        setSentenceIterator(currentWord);

        int beginIndex = currentWord.getEnd();

        JavaCodeConventionEnglishAutomaton automaton = new JavaCodeConventionEnglishAutomaton();
        automaton.init();

        automaton.searchNextWord(text, beginIndex);

        if (!automaton.hasNextWord()) {
            nextWord = null;
        } else {
            nextWord.setStart(automaton.getWordStart());
            nextWord.setText(text.substring(automaton.getWordStart(),
                    automaton.getWordEnd()));
        }

        if (isWordLessThenMinLength(currentWord)) {
            currentWord.setText("");
        }

        return currentWord;
    }

    private boolean isWordLessThenMinLength(final Word word) {
        return word.length() < minimumWordLength;
    }

}
