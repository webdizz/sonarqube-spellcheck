package name.webdizz.sonar.grammar.issue.tracking;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.sonar.api.issue.Issue;
import org.sonar.api.issue.action.Function;
import org.sonar.core.properties.PropertiesDao;
import org.sonar.core.properties.PropertyDto;

import java.util.Map;

import static name.webdizz.sonar.grammar.PluginParameter.ALTERNATIVE_DICTIONARY_PROPERTY_KEY;
import static name.webdizz.sonar.grammar.PluginParameter.ERROR_DESCRIPTION;
import static name.webdizz.sonar.grammar.PluginParameter.SEPARATOR_CHAR;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class LinkFunctionTest {

    private LinkFunction linkFunction;
    private PropertyDto propertyDto = new PropertyDto();
    @Mock
    private PropertiesDao propertiesDao;
    @Mock
    private Function.Context context;
    @Mock
    private Issue issue;

    private static final String FIRST_WORD = "existWord";
    private static final String UNSORTED_WORD = "zoo";
    private static final String NEW_WORD = "newWord";

    @Before
    public void init() {
        linkFunction = new LinkFunction(propertiesDao);
        when(context.issue()).thenReturn(issue);
        when(issue.message()).thenReturn(ERROR_DESCRIPTION + NEW_WORD + "'");
    }

    @Test
    public void shouldCreateandUpdateNewPropertyWhenMethodInvoke() {
        //given
        when(propertiesDao.selectGlobalProperty(ALTERNATIVE_DICTIONARY_PROPERTY_KEY)).thenReturn(null);
        //when
        linkFunction.execute(context);
        //then
        verifySaveOnePropertyWithProperlyValue();

    }

    @Test
    public void shouldUpdatePropertyWhenItExist() {
        //given
        propertyDto.setValue(FIRST_WORD);
        when(propertiesDao.selectGlobalProperty(ALTERNATIVE_DICTIONARY_PROPERTY_KEY)).thenReturn(propertyDto);
        //when
        linkFunction.execute(context);
        //then
        verify(propertiesDao).updateProperties(ALTERNATIVE_DICTIONARY_PROPERTY_KEY, FIRST_WORD, FIRST_WORD + SEPARATOR_CHAR + NEW_WORD);
    }

    @Test
    public void shouldSortPropertyWhenUpdatIt() {
        //given
        propertyDto.setValue(UNSORTED_WORD);
        when(propertiesDao.selectGlobalProperty(ALTERNATIVE_DICTIONARY_PROPERTY_KEY)).thenReturn(propertyDto);
        //when
        linkFunction.execute(context);
        //then
        verify(propertiesDao).updateProperties(ALTERNATIVE_DICTIONARY_PROPERTY_KEY, UNSORTED_WORD, NEW_WORD + SEPARATOR_CHAR + UNSORTED_WORD);
    }

    @Test
    public void shouldNotAddWordWhenWordIsNotUnique() {
        //given
        propertyDto.setValue(NEW_WORD);
        when(propertiesDao.selectGlobalProperty(ALTERNATIVE_DICTIONARY_PROPERTY_KEY)).thenReturn(propertyDto);
        //when
        linkFunction.execute(context);
        //then
        verify(propertiesDao).selectGlobalProperty(ALTERNATIVE_DICTIONARY_PROPERTY_KEY);
        verifyNoMoreInteractions(propertiesDao);
    }

    private void verifySaveOnePropertyWithProperlyValue() {
        ArgumentCaptor<Map> property = ArgumentCaptor.forClass(Map.class);
        verify(propertiesDao).saveGlobalProperties(property.capture());
        assertTrue(property.getValue().containsKey(ALTERNATIVE_DICTIONARY_PROPERTY_KEY));
        assertEquals(NEW_WORD, property.getValue().get(ALTERNATIVE_DICTIONARY_PROPERTY_KEY));
        int propertyCount = property.getValue().size();
        assertEquals(1, propertyCount);
    }


}