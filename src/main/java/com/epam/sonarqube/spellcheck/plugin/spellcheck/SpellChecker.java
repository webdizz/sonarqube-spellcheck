package com.epam.sonarqube.spellcheck.plugin.spellcheck;

import com.google.common.base.Optional;
import static com.google.common.base.Preconditions.checkArgument;

import com.swabunga.spell.event.SpellCheckListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import com.google.common.base.Strings;
import com.swabunga.spell.engine.SpellDictionary;
import com.swabunga.spell.engine.SpellDictionaryHashMap;

import org.sonar.api.BatchExtension;


public class SpellChecker implements BatchExtension {

    private static final Logger LOGGER = LoggerFactory.getLogger(SpellChecker.class);
    private SpellCheckerFactory spellCheckerFactory;
    private SpellDictionary dictionary;
    private Optional<SpellDictionaryHashMap> alternateDictionary;
    private GrammarDictionaryLoader dictionaryLoader;

    private JavaSourceCodeWordFinder javaSourceCodeWordFinder;



    public SpellChecker(final GrammarDictionaryLoader dictionaryLoader,
                        JavaSourceCodeWordFinder javaSourceCodeWordFinder, SpellCheckerFactory spellCheckerFactory) {
        this.spellCheckerFactory = spellCheckerFactory;
        this.javaSourceCodeWordFinder = javaSourceCodeWordFinder;
        this.dictionaryLoader = dictionaryLoader;
    }

    public void initialize() {
        dictionary = dictionaryLoader.loadMainDictionary();
        alternateDictionary = dictionaryLoader.loadAlternateDictionary();
    }

    public void checkSpelling(final String inputLine, final SpellCheckListener spellCheckListener) {
        parametersValidation(inputLine, spellCheckListener);
        com.swabunga.spell.event.SpellChecker spellCheck = createSpellChecker(spellCheckListener);
        JavaSourceCodeTokenizer sourceCodeTokenizer = new JavaSourceCodeTokenizer(inputLine, javaSourceCodeWordFinder);
        spellCheck.checkSpelling(sourceCodeTokenizer);
        spellCheck.reset();
    }

    private static void parametersValidation(final String inputLine, final SpellCheckListener spellCheckListener) {
        checkArgument(spellCheckListener != null, "Cannot proceed with spell checking without SpellCheckListener");
        checkArgument(!Strings.isNullOrEmpty(inputLine), "Cannot proceed with spell checking for empty inputLine");
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Is about to check spelling for \n{} \n and record with {}", inputLine, spellCheckListener);
        }
    }

    private com.swabunga.spell.event.SpellChecker createSpellChecker(final SpellCheckListener spellCheckListener) {
        com.swabunga.spell.event.SpellChecker spellCheck = spellCheckerFactory.getSpellChecker();
        spellCheck.addSpellCheckListener(spellCheckListener);
        spellCheck.setUserDictionary(dictionary);

        if (alternateDictionary.isPresent()) {
            spellCheck.addDictionary(alternateDictionary.get());
        }
        return spellCheck;
    }


}
