package com.epam.sonarqube.spellcheck.plugin;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class SpellCheckMetricsTest {

    private SpellCheckMetrics testingInstance = new SpellCheckMetrics();

    @Test
    public void shouldReturnListOfMetrics() {
        assertNotNull(testingInstance.getMetrics());
    }
}
