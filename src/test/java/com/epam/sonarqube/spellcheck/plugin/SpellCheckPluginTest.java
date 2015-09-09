package com.epam.sonarqube.spellcheck.plugin;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class SpellCheckPluginTest {

    private SpellCheckPlugin testingInstance = new SpellCheckPlugin();

    @Test
    public void shouldReturnExtensionsList(){
        assertNotNull(testingInstance.getExtensions());
    }

}


