package com.epam.sonarqube.spellcheck.plugin.sensor;

import static org.mockito.Matchers.contains;
import static org.mockito.Matchers.eq;
import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.*;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.LoggingEvent;
import ch.qos.logback.core.Appender;
import org.junit.Before;
import org.junit.Test;

import com.swabunga.spell.event.SpellCheckEvent;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class SpellCheckViolationTriggerTest {
    private static final String INVALID_WORD = "test";
    private static final int INVALID_WORD_POSITION = 100;
    private final SpellCheckEvent event = mock(SpellCheckEvent.class);
    private final SpellCheckIssuesWrapper lineWrapper = mock(SpellCheckIssuesWrapper.class);
    private final SpellCheckViolationTrigger testingInstance = new SpellCheckViolationTrigger(lineWrapper);

    private List<String> listSuggestions;

    @Mock
    SpellCheckEvent spellCheckEvent;

    @Mock
    SpellCheckIssuesWrapper spellCheckIssuesWrapper;

    @Mock
    private Appender mockAppender;
    //Captor is genericised with ch.qos.logback.classic.spi.LoggingEvent
    @Captor
    private ArgumentCaptor<LoggingEvent> captorLoggingEvent;

    @Before
    public void init(){
        MockitoAnnotations.initMocks(this);
        listSuggestions = new ArrayList<>();
        listSuggestions.add("String 1");
        listSuggestions.add("String 2");
        listSuggestions.add("String 3");
    }

    @Test
    public void shouldTriggerSpelling() {
        when(event.getInvalidWord()).thenReturn(INVALID_WORD);
        when(event.getWordContextPosition()).thenReturn(INVALID_WORD_POSITION);

        testingInstance.spellingError(event);

        verify(event, atLeastOnce()).getInvalidWord();
        verify(event, atLeastOnce()).getWordContextPosition();
        verify(lineWrapper).incident(contains(INVALID_WORD), eq(INVALID_WORD), eq(INVALID_WORD_POSITION));
    }

    private void initLogger(Level level){
        final Logger logger = (Logger) LoggerFactory.getLogger(SpellCheckViolationTrigger.class);
        logger.addAppender(mockAppender);
        logger.setLevel(level);

        when(event.getInvalidWord()).thenReturn(INVALID_WORD);
        when(event.getWordContextPosition()).thenReturn(INVALID_WORD_POSITION);
        when(event.getSuggestions()).thenReturn(listSuggestions);

        testingInstance.spellingError(event);
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


}
