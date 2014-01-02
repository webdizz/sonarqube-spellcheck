package name.webdizz.sonar.grammar.rule;

import org.sonar.api.rules.Rule;
import org.sonar.api.rules.RuleFinder;
import org.sonar.api.rules.RuleQuery;

public class RuleFinderHelper {

    public static Rule findGrammarRule(final RuleFinder ruleFinder) {
        RuleQuery ruleQuery = RuleQuery.create()
                .withRepositoryKey(GrammarRuleRepository.REPOSITORY_KEY)
                .withConfigKey(GrammarRuleRepository.SONAR_GRAMMAR_RULE);
        return ruleFinder.find(ruleQuery);
    }
}
