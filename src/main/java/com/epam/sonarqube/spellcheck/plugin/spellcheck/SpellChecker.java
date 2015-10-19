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
    private SpellDictionary urlDictionary;
    private Optional<SpellDictionaryHashMap> alternateDictionary;
    private SpellDictionaryLoader dictionaryLoader;

    private JavaSourceCodeWordFinder javaSourceCodeWordFinder;



    public SpellChecker(final SpellDictionaryLoader dictionaryLoader,
                        JavaSourceCodeWordFinder javaSourceCodeWordFinder, SpellCheckerFactory spellCheckerFactory) {
        this.spellCheckerFactory = spellCheckerFactory;
        this.javaSourceCodeWordFinder = javaSourceCodeWordFinder;
        this.dictionaryLoader = dictionaryLoader;
    }

    public void initialize() {
        dictionary = dictionaryLoader.loadMainDictionary();
        alternateDictionary = dictionaryLoader.loadAlternateDictionary();
        urlDictionary = dictionaryLoader.loadURLDictionary(true);
    }

    /**
     * Method reload URL-dictionary
     * @return true if force loading url-dictionary is succeeded
     */
    public boolean reloadURLDictionary() {
        urlDictionary = dictionaryLoader.loadURLDictionary(true);
        if(urlDictionary != null) return true;
        return false;
    }

    /**
     * This method is called to check the spelling of the words in line.
     * <p/>
     * For each invalid word the action listeners will be informed with a new 
     * SpellCheckEvent.<p>
     *
     * @param  inputLine  The line to be spell checked
     * @param spellCheckListener Listener to be called when an invalid word during spell check process is found
     * @return the number of errors found
     */
    public int checkSpelling(final String inputLine, final SpellCheckListener spellCheckListener) {
        parametersValidation(inputLine, spellCheckListener);
        com.swabunga.spell.event.SpellChecker spellCheck = createSpellChecker(spellCheckListener);
        JavaSourceCodeTokenizer sourceCodeTokenizer = new JavaSourceCodeTokenizer(inputLine, javaSourceCodeWordFinder);
        int errors = spellCheck.checkSpelling(sourceCodeTokenizer);
        
        //filter errors, because it could be less than zero, 
        //if spellchecker returns error code instead of errors count
        if (errors < 0) {
            errors = 0;
        }
        spellCheck.reset();
        return errors;
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
        if(urlDictionary != null) {
            spellCheck.addDictionary(urlDictionary);
        }
        return spellCheck;
    }


}
