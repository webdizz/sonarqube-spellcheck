package com.epam.sonarqube.spellcheck.plugin.sensor;

import static org.apache.commons.collections.CollectionUtils.isNotEmpty;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.epam.sonarqube.spellcheck.plugin.PluginParameter;
import com.swabunga.spell.event.SpellCheckEvent;
import com.swabunga.spell.event.SpellCheckListener;

/**
 * Class-trigger to react to spell violations
 */
class SpellCheckViolationTrigger implements SpellCheckListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(SpellCheckViolationTrigger.class);

    private final SpellCheckIssuesWrapper lineWrapper;

    public SpellCheckViolationTrigger(final SpellCheckIssuesWrapper lineWrapper) {
        this.lineWrapper = lineWrapper;
    }

    /**
     * Detected spelling error
     *
     * @param event spell-check-event
     */
    @Override
    public void spellingError(final SpellCheckEvent event) {
        final StringBuilder spellMessageBuilder = new StringBuilder(100);
        spellMessageBuilder
                .append("\'")
                .append(event.getInvalidWord())
                .append("\'")
                .append(PluginParameter.ERROR_DESCRIPTION);
        if (LOGGER.isDebugEnabled()) {
            final Object[] arguments = new Object[]{event.getInvalidWord(), event.getWordContextPosition(), lineWrapper.getLine()};
            LOGGER.debug("Detected invalid word \'{}\' at Col.{}\nin the line \"{}\"", arguments);
        }
        final List suggestions = event.getSuggestions();
        if (isNotEmpty(suggestions)) {
            spellMessageBuilder.append("\n  Suggestions: ");
            boolean first = true;
            for (final Object suggestion : suggestions) {
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("Adding suggestion: {}", suggestion);
                }
                if (first) {
                    spellMessageBuilder.append(suggestion);
                    first = false;
                } else {
                    spellMessageBuilder.append(", ").append(suggestion);
                }
            }
            spellMessageBuilder.append(";");
        }
        final String spellMessage = spellMessageBuilder.toString();
        if (LOGGER.isDebugEnabled()) {
            Object[] args;
            args = new Object[]{spellMessage, event.getWordContextPosition(), lineWrapper.getKey()};
            LOGGER.debug("{} at '{}' \n  in '{}'\n ", args);
        }
        lineWrapper.incident(spellMessage, event.getInvalidWord(), event.getWordContextPosition());
    }

}
