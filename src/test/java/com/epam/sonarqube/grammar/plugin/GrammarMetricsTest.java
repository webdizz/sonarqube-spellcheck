package com.epam.sonarqube.grammar.plugin;

import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class GrammarMetricsTest {

    private GrammarMetrics testingInstance = new GrammarMetrics();

    @Test
    public void shouldReturnListOfMetrics() {
        assertNotNull(testingInstance.getMetrics());
    }
}
