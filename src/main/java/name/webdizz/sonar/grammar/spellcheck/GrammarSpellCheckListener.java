package name.webdizz.sonar.grammar.spellcheck;

import static com.google.common.base.Preconditions.checkState;
import static org.apache.commons.collections.CollectionUtils.isNotEmpty;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.resources.JavaFile;
import org.sonar.api.rules.Rule;
import org.sonar.api.rules.Violation;

import com.swabunga.spell.event.SpellCheckEvent;
import com.swabunga.spell.event.SpellCheckListener;

class GrammarSpellCheckListener implements SpellCheckListener {

    private final static Logger LOGGER = LoggerFactory.getLogger(GrammarSpellCheckListener.class);

    private List<Violation> violations;
    private int lineNumber;
    private Rule rule;
    private JavaFile resource;

    private GrammarSpellCheckListener() {
        super();
    }

    @Override
    public void spellingError(final SpellCheckEvent event) {
        String suggestionMessage = collectSuggestionMessage(event);
        final Violation violation = Violation.create(rule, resource);
        violation.setLineId(lineNumber);
        violation.setMessage(suggestionMessage);
        violations.add(violation);
    }

    private String collectSuggestionMessage(final SpellCheckEvent event) {
        String spellMessage = "Invalid word is: '" + event.getInvalidWord() + "'";
        List suggestions = event.getSuggestions();
        if (isNotEmpty(suggestions)) {
            StringBuilder suggestionsMessage = new StringBuilder("\n  Suggestions: ");
            for (Object suggestion : suggestions) {
                suggestionsMessage.append(suggestion.toString()).append(", ");
            }
            spellMessage += suggestionsMessage.substring(0, suggestionsMessage.length() - 2) + ";";
        }
        if (LOGGER.isInfoEnabled()) {
            Object[] args;
            args = new Object[]{spellMessage, event.getWordContextPosition(), resource.getKey()};
            LOGGER.info("{} at '{}' \n  in '{}'\n ", args);
        }
        return spellMessage;
    }

    public static class Builder {
        private List<Violation> violations;
        private int lineNumber;
        private Rule rule;
        private JavaFile resource;

        public Builder setViolations(final List<Violation> violations) {
            this.violations = violations;
            return this;
        }

        public Builder setLineNumber(final int lineNumber) {
            this.lineNumber = lineNumber;
            return this;
        }

        public Builder setRule(final Rule rule) {
            this.rule = rule;
            return this;
        }

        public Builder setResource(final JavaFile resource) {
            this.resource = resource;
            return this;
        }

        public GrammarSpellCheckListener build() {
            validate();

            GrammarSpellCheckListener spellCheckListener = new GrammarSpellCheckListener();
            spellCheckListener.lineNumber = lineNumber;
            spellCheckListener.resource = resource;
            spellCheckListener.rule = rule;
            spellCheckListener.violations = violations;
            return spellCheckListener;
        }

        private void validate() {
            checkState(lineNumber > 0, "LineNumber is required.");
            checkState(rule != null, "Rule is required.");
            checkState(resource != null, "Resource is required.");
            checkState(violations != null, "Violations empty list is required.");
        }
    }
}
