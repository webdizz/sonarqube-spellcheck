package com.epam.sonarqube.spellcheck.plugin.spellcheck.filter;

import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

@RunWith(Theories.class)
public class DateFormatDaysWordFilterTest {

    private DateFormatDaysWordFilter dateFormatDaysWordFilter = new DateFormatDaysWordFilter();

    @DataPoints
    public static Object[][] correctDateFormats = {
            {"yyyyMMdd", true},
            {"MMyyydd", true},
            {"dddMMMyy", true},
            {"MMMdddyyyy", true},
            {"ddMMyyyyG", true},
            {"ddMMyyyyGGG", true},
            {"EEDDFFwwWW", true},
            {"yyyy", true},
            {"MMM", true},
            {"dd", true},
    };

    @DataPoints
    public static Object[][] wrongOrNotAcceptedDateFormats = {
            {"yyyyMMddyy", false},
            {"yyyMonth", false},
            {"MMddayyyM", false},
            {"ABCyyyyMMdd", false},
            {"Tesst", false},
    };

    @Theory
    public void shouldCheckDateFormatsFromDataPoints(final Object...params) throws Exception {
        String line = (String) params[0];
        boolean expected = (Boolean)params[1];
        boolean actual = dateFormatDaysWordFilter.accept(line);

        assertThat(actual, is(expected));
    }
}
