package com.epam.sonarqube.spellcheck.plugin.sensor;

import static org.mockito.Matchers.contains;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Test;

import com.swabunga.spell.event.SpellCheckEvent;

public class SpellCheckViolationTriggerTest {
    private static final String INVALID_WORD = "test";
    private static final int INVALID_WORD_POSITION = 100;
    private final SpellCheckEvent event = mock(SpellCheckEvent.class);
    private final SpellCheckIssuesWrapper lineWrapper = mock(SpellCheckIssuesWrapper.class);
    private final SpellCheckViolationTrigger testingInstance = new SpellCheckViolationTrigger(lineWrapper);

    @Test
    public void shouldTriggerSpelling() {
        when(event.getInvalidWord()).thenReturn(INVALID_WORD);
        when(event.getWordContextPosition()).thenReturn(INVALID_WORD_POSITION);

        testingInstance.spellingError(event);

        verify(event, atLeastOnce()).getInvalidWord();
        verify(event, atLeastOnce()).getWordContextPosition();
        verify(lineWrapper).incident(contains(INVALID_WORD), eq(INVALID_WORD), eq(INVALID_WORD_POSITION));
    }

}
