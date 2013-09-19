package name.webdizz.sonar.grammar;

import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class GrammarPluginTest {

    private GrammarPlugin testingInstance = new GrammarPlugin();

    @Test
    public void shouldReturnExtensionsList(){
        assertNotNull(testingInstance.getExtensions());
    }

}


