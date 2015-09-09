package com.epam.sonarqube.spellcheck.plugin.issue.tracking;

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

import static com.epam.sonarqube.spellcheck.plugin.PluginParameter.ALTERNATIVE_DICTIONARY_PROPERTY_KEY;
import static com.epam.sonarqube.spellcheck.plugin.PluginParameter.ERROR_DESCRIPTION;
import static com.epam.sonarqube.spellcheck.plugin.PluginParameter.SEPARATOR_CHAR;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsMapContaining.hasEntry;
import static org.hamcrest.collection.IsMapWithSize.aMapWithSize;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AddWordFromIssueFunctionTest {

    private AddWordFromIssueFunction addWordFromIssueFunction;
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
        addWordFromIssueFunction = new AddWordFromIssueFunction(propertiesDao);
        when(context.issue()).thenReturn(issue);
        when(issue.message()).thenReturn(ERROR_DESCRIPTION + NEW_WORD + "'");
    }

    @Test
    public void shouldCreateandUpdateNewPropertyWhenMethodInvoke() {
        when(propertiesDao.selectGlobalProperty(ALTERNATIVE_DICTIONARY_PROPERTY_KEY)).thenReturn(null);
        addWordFromIssueFunction.execute(context);
        verifySaveOnePropertyWithProperlyValue();
    }

    @Test
    public void shouldUpdatePropertyWhenItExist() {
        propertyDto.setValue(FIRST_WORD);
        when(propertiesDao.selectGlobalProperty(ALTERNATIVE_DICTIONARY_PROPERTY_KEY)).thenReturn(propertyDto);
        addWordFromIssueFunction.execute(context);
        verify(propertiesDao).updateProperties(ALTERNATIVE_DICTIONARY_PROPERTY_KEY, FIRST_WORD, FIRST_WORD + SEPARATOR_CHAR + NEW_WORD);
    }

    @Test
    public void shouldSortPropertyWhenUpdateIt(){
        propertyDto.setValue(UNSORTED_WORD);
        when(propertiesDao.selectGlobalProperty(ALTERNATIVE_DICTIONARY_PROPERTY_KEY)).thenReturn(propertyDto);
        addWordFromIssueFunction.execute(context);
        verify(propertiesDao).updateProperties(ALTERNATIVE_DICTIONARY_PROPERTY_KEY, UNSORTED_WORD, NEW_WORD + SEPARATOR_CHAR + UNSORTED_WORD);
    }

    @Test
    public void shouldNotAddWordWhenWordIsNotUnique() {
        propertyDto.setValue(NEW_WORD);
        when(propertiesDao.selectGlobalProperty(ALTERNATIVE_DICTIONARY_PROPERTY_KEY)).thenReturn(propertyDto);
        addWordFromIssueFunction.execute(context);
        verify(propertiesDao).selectGlobalProperty(ALTERNATIVE_DICTIONARY_PROPERTY_KEY);
        verifyNoMoreInteractions(propertiesDao);
    }

    private void verifySaveOnePropertyWithProperlyValue() {
        ArgumentCaptor<Map> captor = ArgumentCaptor.forClass(Map.class);
        verify(propertiesDao).saveGlobalProperties(captor.capture());
        Map<String, String> properties = captor.getValue();
        assertThat(properties, hasEntry(ALTERNATIVE_DICTIONARY_PROPERTY_KEY, NEW_WORD));
        assertThat(properties, aMapWithSize(1));
    }

}