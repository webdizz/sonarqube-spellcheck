package com.epam.sonarqube.spellcheck.plugin.metric;


import org.sonar.api.measures.CoreMetrics;
import org.sonar.api.measures.Metric;
import org.sonar.api.measures.Metrics;

import java.util.Arrays;
import java.util.List;

public class SpellCheckMetrics implements Metrics {

    public static final Metric<Integer> MISSPELLING = new Metric.Builder("sonar.spellcheck.misspelling", "Misspelling", Metric.ValueType.INT)
            .setDescription("Count of misspelled words")
            .setDirection(Metric.DIRECTION_WORST)
            .setQualitative(false)
            .setDomain(CoreMetrics.DOMAIN_GENERAL)
            .create();

    @Override
    public List<Metric> getMetrics() {
        return Arrays.<Metric>asList(MISSPELLING);
    }
}
