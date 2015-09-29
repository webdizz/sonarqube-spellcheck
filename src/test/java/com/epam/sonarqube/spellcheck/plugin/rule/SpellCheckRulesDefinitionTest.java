package com.epam.sonarqube.spellcheck.plugin.rule;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.LoggingEvent;
import ch.qos.logback.core.Appender;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.rule.RuleStatus;
import org.sonar.api.rule.Severity;
import org.sonar.api.server.rule.RulesDefinition;

import com.epam.sonarqube.spellcheck.plugin.PluginParameter;

import static org.hamcrest.CoreMatchers.any;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

public class SpellCheckRulesDefinitionTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(SpellCheckRulesDefinitionTest.class);

    private final RulesDefinition.Context context = mock(RulesDefinition.Context.class);
    private final RulesDefinition.NewRepository repository = prepareMockedRulesRepository();

    private final SpellCheckRulesDefinition instance = new SpellCheckRulesDefinition();

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
     * Test of define method, of class GrammarRulesDefinition.
     */
    @Test
    public void shoulTestBehaviorOfMethodDefine() {
        LOGGER.info("Testing define");

        instance.define(context);

        // check behavior
        verify(context).createRepository(PluginParameter.REPOSITORY_KEY, PluginParameter.PROFILE_LANGUAGE);
        verify(repository).setName(PluginParameter.REPOSITORY_NAME);
        verify(repository).createRule(PluginParameter.SONAR_SPELL_CHECK_RULE_KEY);
        verify(repository).done();

        LOGGER.info("Done.");
    }

    private void initLogger(Level level){
        final ch.qos.logback.classic.Logger logger = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(SpellCheckRulesDefinition.class);
        logger.addAppender(mockAppender);

        instance.define(context);
    }

    @Test
    public void shouldTestThatLoggerUsedWhenDefineRulesAndLevelIsDebug() {
        Level level = Level.DEBUG;
        initLogger(level);

        //Now verify our logging interactions
        verify(mockAppender, atLeast(1)).doAppend(captorLoggingEvent.capture());
        //Having a genricised captor means we don't need to cast
        final LoggingEvent loggingEvent = captorLoggingEvent.getValue();
        //Check log level is correct
        assertThat(loggingEvent.getLevel(), is(level));
    }

    @Test
    public void shouldTestThatLoggerNeverUsedWhenDefineRulesAndLevelIsNotDebug() {
        Level level = Level.INFO;
        initLogger(level);

        //Now verify our logging interactions
        verify(mockAppender, times(0)).doAppend(any(LoggingEvent.class));
    }

    private RulesDefinition.NewRepository prepareMockedRulesRepository() {
        final RulesDefinition.NewRepository mockedRulesRepository = mock(RulesDefinition.NewRepository.class);
        when(context.createRepository(PluginParameter.REPOSITORY_KEY, PluginParameter.PROFILE_LANGUAGE)).thenReturn(mockedRulesRepository);
        final RulesDefinition.NewRule rule = mock(RulesDefinition.NewRule.class);
        when(mockedRulesRepository.createRule(PluginParameter.SONAR_SPELL_CHECK_RULE_KEY)).thenReturn(rule);
        when(mockedRulesRepository.setName(PluginParameter.REPOSITORY_NAME)).thenReturn(mockedRulesRepository);
        when(rule.setInternalKey(PluginParameter.SONAR_SPELL_CHECK_RULE_KEY)).thenReturn(rule);
        when(rule.setName(anyString())).thenReturn(rule);
        when(rule.setHtmlDescription(PluginParameter.SONAR_SPELL_CHECK_RULE_DESCRIPTION)).thenReturn(rule);
        when(rule.setTags(PluginParameter.REPOSITORY_KEY)).thenReturn(rule);
        when(rule.setStatus(RuleStatus.READY)).thenReturn(rule);
        when(rule.setSeverity(Severity.INFO)).thenReturn(rule);
        return mockedRulesRepository;
    }

}
