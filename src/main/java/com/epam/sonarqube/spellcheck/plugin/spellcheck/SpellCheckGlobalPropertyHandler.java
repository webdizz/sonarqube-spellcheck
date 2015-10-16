package com.epam.sonarqube.spellcheck.plugin.spellcheck;

import org.sonar.api.config.GlobalPropertyChangeHandler;


/**
 * Observe changes of global properties done from web application.
 *
 * onChange will call when a property would change
 */

public class SpellCheckGlobalPropertyHandler extends GlobalPropertyChangeHandler {

    @Override
    public void onChange(GlobalPropertyChangeHandler.PropertyChange propertyChange) {
        // TODO get SpellChecker instance & call reloadURLDictionary() if propertyChange.getKey() == URL_DICTIONARY_PATH
        // & propertyChange.getNewValue() != oldValue
    }
}
