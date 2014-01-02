package name.webdizz.sonar.grammar.spellcheck;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.sonar.api.config.Settings;
import name.webdizz.sonar.grammar.GrammarPlugin;

@RunWith(MockitoJUnitRunner.class)
public class GrammarDictionaryLoaderTest {

    @Mock
    private Settings settings;

    private GrammarDictionaryLoader testingInstance;

    @Before
    public void setUp() {
        testingInstance = new GrammarDictionaryLoader(settings);
    }

    @Ignore
    @Test
    public void shouldNotLoadDictionary() {
        assertNull("Dictionary was loaded", testingInstance.load());
    }

    @Test
    public void shouldGetDictionaryPathFromSettings() {
        testingInstance.load();
        verify(settings).getString(GrammarPlugin.DICTIONARY);
    }

    @Test
    public void shouldLoadDictionary() {
        when(settings.getString(GrammarPlugin.DICTIONARY)).thenReturn("dict/english.0");
        assertNotNull("Dictionary was not loaded", testingInstance.load());
    }

    @Ignore
    @Test(expected = GrammarDictionaryLoader.UnableToLoadDictionary.class)
    public void shouldHandleIOExceptionOnDictionaryLoad() {
        when(settings.getString(GrammarPlugin.DICTIONARY)).thenReturn("wrong_path");
        testingInstance.load();
    }
}
