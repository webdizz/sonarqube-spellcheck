package name.webdizz.sonar.grammar.spellcheck;

import com.swabunga.spell.engine.Configuration;
import com.swabunga.spell.event.SpellChecker;

public class SpellCheckerFactory {
    private SpellChecker spellChecker;

    public SpellCheckerFactory() {
        spellChecker = new SpellChecker();
        setSpellCheckerConfigs();
    }

    public SpellChecker getSpellChecker() {
        return spellChecker;
    }

    private void setSpellCheckerConfigs() {
        getSpellCheckerConfig().setBoolean(Configuration.SPELL_IGNOREMIXEDCASE, false);
        getSpellCheckerConfig().setBoolean(Configuration.SPELL_IGNOREUPPERCASE, true);
        getSpellCheckerConfig().setBoolean(Configuration.SPELL_IGNOREDIGITWORDS, false);
        getSpellCheckerConfig().setBoolean(Configuration.SPELL_IGNOREINTERNETADDRESSES, true);
    }

    private Configuration getSpellCheckerConfig() {
        return spellChecker.getConfiguration();
    }

}
