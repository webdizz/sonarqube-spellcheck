package com.epam.sonarqube.spellcheck.plugin.issue.tracking;

import org.sonar.api.ServerExtension;
import org.sonar.api.issue.Issue;
import org.sonar.api.issue.action.Actions;
import org.sonar.api.issue.condition.HasIssuePropertyCondition;
import org.sonar.api.issue.condition.HasStatus;
import org.sonar.api.issue.condition.NotCondition;

import com.epam.sonarqube.spellcheck.plugin.PluginParameter;


public final class SpellCheckActionDefinition implements ServerExtension {

    private final Actions actions;
    private final AddWordFromIssueFunction addWordFromIssueFunction;

    public SpellCheckActionDefinition(Actions actions, AddWordFromIssueFunction addWordFromIssueFunction) {
        this.actions = actions;
        this.addWordFromIssueFunction = addWordFromIssueFunction;
    }

    public void start() {
        actions.add(PluginParameter.ADD_TO_DICT)
                .setConditions(
                        new HasIssuePropertyCondition(PluginParameter.SONAR_SPELL_CHECK_RULE_ATTRIBUTE),
                        new NotCondition(new HasStatus(Issue.STATUS_RESOLVED, Issue.STATUS_CLOSED))
                )
                .setFunctions(addWordFromIssueFunction);
    }
}



