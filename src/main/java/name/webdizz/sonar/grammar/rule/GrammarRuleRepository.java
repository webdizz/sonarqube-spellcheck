package name.webdizz.sonar.grammar.rule;

import java.util.ArrayList;
import java.util.List;

import org.sonar.api.resources.Java;
import org.sonar.api.rules.Rule;
import org.sonar.api.rules.RulePriority;
import org.sonar.api.rules.RuleRepository;

public class GrammarRuleRepository extends RuleRepository {

    public static final String REPOSITORY_NAME = "Sonar Grammar";
    public static final String REPOSITORY_KEY = "snrgrm";

    public GrammarRuleRepository() {
        super(REPOSITORY_KEY, Java.KEY);
        setName(REPOSITORY_NAME);
    }

    @Override
    public List<Rule> createRules() {
        List<Rule> rules = new ArrayList<Rule>(1);
        Rule rule = Rule.create(getKey(), "sonar_grammar_rule", "Sonar Grammar");
        rule.setLanguage(getLanguage());
        rule.setSeverity(RulePriority.BLOCKER);
        rule.setDescription("Analyses source code for english grammar issues.");
        rule.setStatus(Rule.STATUS_READY);
        rules.add(rule);
        return rules;
    }
}
