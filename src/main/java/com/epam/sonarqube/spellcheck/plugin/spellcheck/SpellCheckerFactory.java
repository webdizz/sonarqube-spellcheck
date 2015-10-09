package com.epam.sonarqube.spellcheck.plugin.spellcheck;

import com.swabunga.spell.engine.Configuration;
import com.swabunga.spell.event.SpellChecker;
import com.epam.sonarqube.spellcheck.plugin.PluginParameter;
import org.picocontainer.annotations.Inject;
import org.sonar.api.BatchExtension;
import org.sonar.api.config.Settings;

public class SpellCheckerFactory implements BatchExtension {

    private Settings settings;

    public SpellChecker getSpellChecker() {
        SpellChecker spellChecker = new SpellChecker();
        setSpellCheckerConfigs(spellChecker);
        return spellChecker;
    }

    private void setSpellCheckerConfigs(SpellChecker spellChecker) {
        spellChecker.getConfiguration().setBoolean(Configuration.SPELL_IGNOREMIXEDCASE, settings.getBoolean(PluginParameter.SPELL_IGNORE_MIXED_CASE));
        spellChecker.getConfiguration().setBoolean(Configuration.SPELL_IGNOREUPPERCASE, settings.getBoolean(PluginParameter.SPELL_IGNORE_UPPERCASE));
        spellChecker.getConfiguration().setBoolean(Configuration.SPELL_IGNOREDIGITWORDS, settings.getBoolean(PluginParameter.SPELL_IGNORE_DIGIT_WORDS));
        spellChecker.getConfiguration().setBoolean(Configuration.SPELL_IGNOREINTERNETADDRESSES, settings.getBoolean(PluginParameter.SPELL_IGNORE_INTERNET_ADDRESSES));
        spellChecker.getConfiguration().setInteger(PluginParameter.SPELL_MINIMUM_WORD_LENGTH, settings.getInt(PluginParameter.SPELL_MINIMUM_WORD_LENGTH));
    }

    @Inject
    public void setSettings(Settings settings) {
        this.settings = settings;
        
        //parameters for Jazzy spellchecker
        this.settings.setProperty(PluginParameter.SPELL_IGNORE_MIXED_CASE, false);
        this.settings.setProperty(PluginParameter.SPELL_IGNORE_DIGIT_WORDS, false);
        this.settings.setProperty(PluginParameter.SPELL_IGNORE_INTERNET_ADDRESSES, false);
    }

}
