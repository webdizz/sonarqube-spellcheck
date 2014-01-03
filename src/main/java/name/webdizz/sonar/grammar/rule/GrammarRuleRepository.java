package name.webdizz.sonar.grammar.rule;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.resources.Java;
import org.sonar.api.rules.Rule;
import org.sonar.api.rules.RulePriority;
import org.sonar.api.rules.RuleRepository;

public class GrammarRuleRepository extends RuleRepository {

    public static final String REPOSITORY_NAME = "Sonar Grammar";
    public static final String REPOSITORY_KEY = "snrgrm";
    public static final String SONAR_GRAMMAR_RULE = "sonar_grammar_rule";

    private final static Logger LOGGER = LoggerFactory.getLogger(GrammarRuleRepository.class);

    public GrammarRuleRepository() {
        super(REPOSITORY_KEY, Java.KEY);
        setName(REPOSITORY_NAME);
    }

    @Override
    public List<Rule> createRules() {
        LOGGER.info("Is about to create rules for '{}' plugin.", REPOSITORY_NAME);
        List<Rule> rules = new ArrayList<Rule>(1);
        Rule rule = Rule.create(getKey(), SONAR_GRAMMAR_RULE, REPOSITORY_NAME);
        rule.setLanguage(getLanguage());
        rule.setSeverity(RulePriority.INFO);
        rule.setDescription("Analyses source code for english grammar issues.");
        rule.setStatus(Rule.STATUS_READY);
        rules.add(rule);
        return rules;
    }
}
