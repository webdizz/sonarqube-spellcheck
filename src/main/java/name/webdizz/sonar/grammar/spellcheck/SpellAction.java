package name.webdizz.sonar.grammar.spellcheck;

import static org.apache.commons.collections.CollectionUtils.isNotEmpty;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.resources.JavaFile;
import org.sonar.api.rules.Rule;
import org.sonar.api.rules.Violation;

import com.swabunga.spell.event.SpellCheckEvent;
import com.swabunga.spell.event.SpellCheckListener;

class SpellAction {

    private final static Logger LOGGER = LoggerFactory.getLogger(SpellAction.class);

    private int lineNumber;
    private String sourceLine;
    private Rule rule;
    private JavaFile resource;
    private GrammarChecker grammarChecker;

    private SpellAction() {
        super();
    }

    public List<Violation> spellLine() {
        final List<Violation> violations = new ArrayList();

        SpellCheckListener spellCheckListener = new SpellCheckListener() {
            @Override
            public void spellingError(final SpellCheckEvent event) {
                String suggestionMessage = collectSuggestionMessage(event);
                final Violation violation = Violation.create(rule, resource);
                violation.setLineId(lineNumber);
                violation.setMessage(suggestionMessage);
                violations.add(violation);
            }
        };
        grammarChecker.checkSpelling(sourceLine, spellCheckListener);

        return violations;
    }

    private String collectSuggestionMessage(final SpellCheckEvent event) {
        String suggestionMessage = "Invalid word is: " + event.getInvalidWord();
        List suggestions = event.getSuggestions();
        LOGGER.info("MISSPELL WORD: {} at {}", event.getInvalidWord(), event.getWordContextPosition());
        if (isNotEmpty(suggestions)) {
            suggestionMessage += "  Suggestions: ";
            for (Object suggestion : suggestions) {
                suggestionMessage += suggestion.toString();
            }
        }
        return suggestionMessage;
    }

    public static class SpellActionBuilder {
        private int lineNumber;
        private String sourceLine;
        private Rule rule;
        private JavaFile resource;
        private GrammarChecker grammarChecker;

        public SpellActionBuilder setLineNumber(final int lineNumber) {
            this.lineNumber = lineNumber;
            return this;
        }

        public SpellActionBuilder setSourceLine(final String sourceLine) {
            this.sourceLine = sourceLine;
            return this;
        }

        public SpellActionBuilder setRule(final Rule rule) {
            this.rule = rule;
            return this;
        }

        public SpellActionBuilder setResource(final JavaFile resource) {
            this.resource = resource;
            return this;
        }

        public SpellActionBuilder setGrammarChecker(final GrammarChecker grammarChecker) {
            this.grammarChecker = grammarChecker;
            return this;
        }

        public SpellAction build() {
            SpellAction spellAction = new SpellAction();
            spellAction.lineNumber = lineNumber;
            spellAction.resource = resource;
            spellAction.rule = rule;
            spellAction.sourceLine = sourceLine;
            spellAction.grammarChecker = grammarChecker;
            return spellAction;
        }
    }
}
