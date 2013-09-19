package name.webdizz.sonar.grammar;


import org.sonar.api.measures.CoreMetrics;
import org.sonar.api.measures.Metric;
import org.sonar.api.measures.Metrics;

import java.util.Arrays;
import java.util.List;

public class GrammarMetrics implements Metrics {

    public static final Metric MISSPELLING = new Metric.Builder("sonar.grammar.misspelling", "Misspelling", Metric.ValueType.STRING)
            .setDescription("This is a metric to store a misspelled word in code")
            .setDirection(Metric.DIRECTION_WORST)
            .setQualitative(false)
            .setDomain(CoreMetrics.DOMAIN_GENERAL)
            .create();

    @Override
    public List<Metric> getMetrics() {
        return Arrays.asList(MISSPELLING);
    }
}
