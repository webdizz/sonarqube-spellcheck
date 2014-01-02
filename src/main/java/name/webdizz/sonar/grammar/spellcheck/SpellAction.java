package name.webdizz.sonar.grammar.spellcheck;

import static com.google.common.base.Preconditions.checkState;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.resources.JavaFile;
import org.sonar.api.rules.Rule;
import org.sonar.api.rules.Violation;

import com.google.common.base.Strings;
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

    public List<Violation> spell() {
        final List<Violation> violations = new ArrayList<Violation>();
        SpellCheckListener spellCheckListener = createSpellCheckListener(violations);
        grammarChecker.checkSpelling(sourceLine, spellCheckListener);
        spellCheckListener = null;
        return violations;
    }

    private SpellCheckListener createSpellCheckListener(final List<Violation> violations) {
        GrammarSpellCheckListener.Builder checkListenerBuilder = new GrammarSpellCheckListener.Builder();
        checkListenerBuilder.setLineNumber(lineNumber);
        checkListenerBuilder.setResource(resource);
        checkListenerBuilder.setViolations(violations);
        checkListenerBuilder.setRule(rule);
        return checkListenerBuilder.build();
    }

    public static class Builder {
        private int lineNumber;
        private String sourceLine;
        private Rule rule;
        private JavaFile resource;
        private GrammarChecker grammarChecker;

        public Builder setLineNumber(final int lineNumber) {
            this.lineNumber = lineNumber;
            return this;
        }

        public Builder setSourceLine(final String sourceLine) {
            this.sourceLine = sourceLine;
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

        public Builder setGrammarChecker(final GrammarChecker grammarChecker) {
            this.grammarChecker = grammarChecker;
            return this;
        }

        public SpellAction build() {
            validate();

            SpellAction spellAction = new SpellAction();
            spellAction.lineNumber = lineNumber;
            spellAction.resource = resource;
            spellAction.rule = rule;
            spellAction.sourceLine = sourceLine;
            spellAction.grammarChecker = grammarChecker;
            return spellAction;
        }

        private void validate() {
            checkState(grammarChecker != null, "GrammarChecker is required.");
            checkState(lineNumber > 0, "LineNumber is required.");
            checkState(rule != null, "Rule is required.");
            checkState(!Strings.isNullOrEmpty(sourceLine), "SourceLine is required.");
            checkState(resource != null, "Resource is required.");
        }
    }

}
