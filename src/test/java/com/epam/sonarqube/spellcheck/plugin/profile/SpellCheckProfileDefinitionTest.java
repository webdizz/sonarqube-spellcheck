package com.epam.sonarqube.spellcheck.plugin.profile;

import static org.hamcrest.CoreMatchers.any;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.*;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.core.Appender;
import ch.qos.logback.classic.spi.LoggingEvent;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.LoggerFactory;
import org.sonar.api.profiles.RulesProfile;
import org.sonar.api.rules.Rule;
import org.sonar.api.rules.RuleFinder;
import org.sonar.api.utils.ValidationMessages;

import com.epam.sonarqube.spellcheck.plugin.PluginParameter;

public class SpellCheckProfileDefinitionTest {


    private final RuleFinder ruleFinder = mock(RuleFinder.class);
    private final SpellCheckProfileDefinition instance = new SpellCheckProfileDefinition(ruleFinder);
    private final Rule rule = prepareMockedRule();

    @Mock
    private Appender mockAppender;
    //Captor is genericised with ch.qos.logback.classic.spi.LoggingEvent
    @Captor
    private ArgumentCaptor<LoggingEvent> captorLoggingEvent;

    @Before
    public void init(){
        MockitoAnnotations.initMocks(this);
    }


    /**
     * Test of createProfile method, of class GrammarProfileDefinition.
     */
    @Test
    public void shouldCreateProfile() {

        RulesProfile expected = RulesProfile.create(PluginParameter.PROFILE_NAME, PluginParameter.PROFILE_LANGUAGE);
        RulesProfile result = instance.createProfile(ValidationMessages.create());

        assertEquals(expected, result);
    }

    @Test
    public void shouldCreateActiveRules(){

        RulesProfile result = instance.createProfile(ValidationMessages.create());

        assertEquals(rule, result.getActiveRules().get(0).getRule());
        assertEquals(rule, result.getActiveRule(rule).getRule());
    }

    @Test
    public void shouldGetActiveRuleWhenProfileCreatedAndActiveRulesNotEmpty(){

        RulesProfile result = instance.createProfile(ValidationMessages.create());

        assertTrue(!result.getActiveRules().isEmpty());
        assertEquals(rule, result.getActiveRule(PluginParameter.REPOSITORY_KEY, PluginParameter.SONAR_SPELL_CHECK_RULE_KEY).getRule());
    }

    private void initLogger(Level level){
        final Logger logger = (Logger) LoggerFactory.getLogger(SpellCheckProfileDefinition.class);
        logger.addAppender(mockAppender);

        instance.createProfile(ValidationMessages.create());
    }

    @Test
    public void shouldTestThatLoggerUsedWhenCreateProfileAndLevelIsDebug() {
        Level level = Level.DEBUG;
        initLogger(Level.DEBUG);

        //Now verify our logging interactions
        verify(mockAppender, atLeast(1)).doAppend(captorLoggingEvent.capture());
        //Having a genricised captor means we don't need to cast
        final LoggingEvent loggingEvent = captorLoggingEvent.getValue();
        //Check log level is correct
        assertThat(loggingEvent.getLevel(), is(level));
    }

    @Test
    public void shouldTestThatLoggerNeverUsedWhenCreateProfileAndLevelIsNotDebug() {
        Level level = Level.INFO;
        initLogger(level);

        //Now verify our logging interactions
        verify(mockAppender, times(0)).doAppend(any(LoggingEvent.class));
    }

    private Rule prepareMockedRule() {
        final Rule mockedRule = mock(Rule.class);
        when(mockedRule.getRepositoryKey()).thenReturn(PluginParameter.REPOSITORY_KEY);
        when(mockedRule.getKey()).thenReturn(PluginParameter.SONAR_SPELL_CHECK_RULE_KEY);
        when(mockedRule.isEnabled()).thenReturn(Boolean.TRUE);
        when(ruleFinder.findByKey(PluginParameter.REPOSITORY_KEY, PluginParameter.SONAR_SPELL_CHECK_RULE_KEY)).thenReturn(mockedRule);
        return mockedRule;
    }

}
