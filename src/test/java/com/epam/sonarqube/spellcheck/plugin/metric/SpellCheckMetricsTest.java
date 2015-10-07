package com.epam.sonarqube.spellcheck.plugin.metric;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.epam.sonarqube.spellcheck.plugin.metric.SpellCheckMetrics;

public class SpellCheckMetricsTest {

    private SpellCheckMetrics testingInstance = new SpellCheckMetrics();

    @Test
    public void shouldReturnListOfMetrics() {
        assertNotNull(testingInstance.getMetrics());
    }
}
