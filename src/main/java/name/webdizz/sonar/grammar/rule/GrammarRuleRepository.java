package name.webdizz.sonar.grammar.rule;

import java.util.ArrayList;
import java.util.List;

import org.sonar.api.resources.Java;
import org.sonar.api.rules.Rule;
import org.sonar.api.rules.RulePriority;
import org.sonar.api.rules.RuleRepository;

public class GrammarRuleRepository extends RuleRepository {

    public static final String REPOSITORY_NAME = "SonarGrammar";
    public static final String REPOSITORY_KEY = "snrgrm";

    public GrammarRuleRepository() {
        super(REPOSITORY_KEY, Java.KEY);
        setName(REPOSITORY_NAME);
    }

    @Override
    public List<Rule> createRules() {
        List<Rule> rules = new ArrayList<Rule>();
        Rule rule = Rule.create(getKey(), "first_sonar_grammar_rule_key", "first_sonar_grammar_rule_name");
        rule.setLanguage(getLanguage());
        rule.setSeverity(RulePriority.BLOCKER);
        rule.setDescription("grammar rule description");
        rule.setStatus(Rule.STATUS_READY);
        rules.add(rule);
        return rules;
    }
}
