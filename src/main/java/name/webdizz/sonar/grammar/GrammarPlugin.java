package name.webdizz.sonar.grammar;

import name.webdizz.sonar.grammar.profile.GrammarProfileDefinition;
import name.webdizz.sonar.grammar.rule.GrammarRulesDefinition;
import name.webdizz.sonar.grammar.sensor.GrammarIssuesSensor;
import org.sonar.api.SonarPlugin;

import java.util.Arrays;
import java.util.List;


public class GrammarPlugin extends SonarPlugin {

    @Override
    public List getExtensions() {
        return Arrays
                .asList(// Definitions
                        GrammarRulesDefinition.class,
                        GrammarProfileDefinition.class,
                        // Metrics
                        GrammarMetrics.class,
                        // Sensor
                        GrammarIssuesSensor.class
                );
    }
}
