package name.webdizz.sonar.grammar.spellcheck;

import com.swabunga.spell.engine.Configuration;
import com.swabunga.spell.event.SpellChecker;
import name.webdizz.sonar.grammar.PluginParameter;
import org.picocontainer.annotations.Inject;
import org.sonar.api.BatchExtension;
import org.sonar.api.config.Settings;

public class SpellCheckerFactory implements BatchExtension {

    @Inject
    private Settings settings;

    public SpellCheckerFactory() {

    }

    public SpellChecker getSpellChecker() {
        SpellChecker spellChecker = new SpellChecker();
        setSpellCheckerConfigs(spellChecker);
        return spellChecker;
    }

    private void setSpellCheckerConfigs(SpellChecker spellChecker) {
        spellChecker.getConfiguration().setBoolean(Configuration.SPELL_IGNOREMIXEDCASE, settings.getBoolean(Configuration.SPELL_IGNOREMIXEDCASE));
        spellChecker.getConfiguration().setBoolean(Configuration.SPELL_IGNOREUPPERCASE, settings.getBoolean(Configuration.SPELL_IGNOREUPPERCASE));
        spellChecker.getConfiguration().setBoolean(Configuration.SPELL_IGNOREDIGITWORDS, settings.getBoolean(Configuration.SPELL_IGNOREDIGITWORDS));
        spellChecker.getConfiguration().setBoolean(Configuration.SPELL_IGNOREINTERNETADDRESSES, settings.getBoolean(Configuration.SPELL_IGNOREINTERNETADDRESSES));
        spellChecker.getConfiguration().setInteger(PluginParameter.SPELL_THRESHOLD, settings.getInt(PluginParameter.SPELL_THRESHOLD));
    }

}
