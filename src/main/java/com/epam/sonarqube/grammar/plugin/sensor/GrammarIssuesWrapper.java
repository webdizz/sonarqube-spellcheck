package com.epam.sonarqube.grammar.plugin.sensor;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.component.ResourcePerspectives;
import org.sonar.api.issue.Issuable;
import org.sonar.api.issue.Issue;
import org.sonar.api.rule.RuleKey;

import static com.google.common.base.Preconditions.checkState;

/**
 * Wrapper for one code line to check
 *
 */
public class GrammarIssuesWrapper {

    private final String COLUMN_ATTRIBUTE = "Col.";

    private static final Logger LOGGER = LoggerFactory.getLogger(GrammarIssuesWrapper.class);

    private String line;
    private int lineNumber;
    private ResourcePerspectives perspectives;
    private InputFile inputFile;
    private RuleKey ruleKey;

    private GrammarIssuesWrapper() {
    }

    public String getLine() {
        return line;
    }

    public int getLineNumber() {
        return lineNumber;
    }

    public String getKey() {
        return inputFile.absolutePath();
    }

    /**
     * Something wrong with wrapped line detected.<BR/>
     * Appropriate issue will be created.
     *
     * @param message Incident's explanation
     * @param column column number in wrapped line
     */
    public void incident(final String message, final int column) {
        if (LOGGER.isDebugEnabled()) {
            final Object[] arguments = new Object[]{message, COLUMN_ATTRIBUTE, column};
            LOGGER.debug("Reported about incident \"{}\" {}{}", arguments);
        }
        final Issuable issuable = perspectives.as(Issuable.class, inputFile);
        final Issue issue = issuable.newIssueBuilder()
                .message(message)
                .line(new Integer(lineNumber))
                .ruleKey(ruleKey)
                .attribute(COLUMN_ATTRIBUTE, Integer.toString(column))
                .build();
        final boolean success = issuable.addIssue(issue);
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Issue about incident added {}", success);
        }
    }

    /**
     * Something wrong with wrapped line detected.<BR/>
     * Appropriate issue will be created.
     *
     * @param message Incident's explanation
     */
    public void incident(final String message) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Reported about incident \"{}\"", message);
        }
        final Issuable issuable = perspectives.as(Issuable.class, inputFile);
        final Issue issue = issuable.newIssueBuilder()
                .message(message)
                .line(new Integer(lineNumber))
                .ruleKey(ruleKey)
                .build();
        final boolean success = issuable.addIssue(issue);
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Issue about incident has added {}", success);
        }
    }

    @Override
    public String toString() {
        return "GrammarIssuesWrapper{" + "line=\"" + line + "\", lineNumber=" + lineNumber + ", ruleKey=" + ruleKey + '}';
    }

    // private methods
    /**
     * To get the builder of wrapper
     *
     * @return the instance
     */
    public static Builder builder() {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Empty builder creating.");
        }
        return new Builder();
    }

    /**
     * to get the builder for make copy of wrapper
     *
     * @param wrapper to copy
     * @return the instance
     */
    public static Builder builder(final GrammarIssuesWrapper wrapper) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("The builder from {} creating.", wrapper);
        }
        return new Builder(wrapper);
    }

    // inner classes
    /**
     * Class-builder of wrapper
     */
    public static final class Builder {

        private String line;
        private int lineNumber;
        private ResourcePerspectives perspectives;
        private InputFile inputFile;
        private RuleKey ruleKey;

        private Builder() {
        }

        private Builder(final GrammarIssuesWrapper wrapper) {
            this.line = wrapper.line;
            this.lineNumber = wrapper.lineNumber;
            this.perspectives = wrapper.perspectives;
            this.inputFile = wrapper.inputFile;
            this.ruleKey = wrapper.ruleKey;
        }

        public Builder setLine(final String line) {
            this.line = line;
            return this;
        }

        public Builder setLineNumber(final int lineNumber) {
            this.lineNumber = lineNumber;
            return this;
        }

        public Builder setPerspectives(final ResourcePerspectives perspectives) {
            this.perspectives = perspectives;
            return this;
        }

        public Builder setInputFile(final InputFile inputFile) {
            this.inputFile = inputFile;
            return this;
        }

        public Builder setRuleKey(final RuleKey ruleKey) {
            this.ruleKey = ruleKey;
            return this;
        }

        /**
         * Builder of configured wrapper instance
         *
         * @return configured instance
         */
        public GrammarIssuesWrapper build() {
            validate();

            final GrammarIssuesWrapper wrapper = new GrammarIssuesWrapper();
            wrapper.line = line;
            wrapper.lineNumber = lineNumber;
            wrapper.perspectives = perspectives;
            wrapper.inputFile = inputFile;
            wrapper.ruleKey = ruleKey;
            return wrapper;
        }

        // private methods
        private void validate() {
            checkState(!StringUtils.isEmpty(line), "Code line can't be empty.");
            checkState(lineNumber > 0, "LineNumber is required.");
            checkState(inputFile != null, "Resource is required.");
            checkState(perspectives != null, "ResourcePerspectives is required.");
            checkState(ruleKey != null, "RuleKey is required.");
        }

    }
}
