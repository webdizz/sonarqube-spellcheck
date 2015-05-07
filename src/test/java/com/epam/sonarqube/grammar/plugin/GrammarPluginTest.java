package com.epam.sonarqube.grammar.plugin;

import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class GrammarPluginTest {

    private GrammarPlugin testingInstance = new GrammarPlugin();

    @Test
    public void shouldReturnExtensionsList(){
        assertNotNull(testingInstance.getExtensions());
    }

}


