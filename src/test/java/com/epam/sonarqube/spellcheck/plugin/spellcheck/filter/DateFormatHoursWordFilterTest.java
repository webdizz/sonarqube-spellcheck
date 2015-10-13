package com.epam.sonarqube.spellcheck.plugin.spellcheck.filter;

import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

@RunWith(Theories.class)
public class DateFormatHoursWordFilterTest {

    private DateFormatHoursWordFilter dateFormatHoursWordFilter = new DateFormatHoursWordFilter();

    @DataPoints
    public static Object[][] correctHourFormats = {
            {"HHmmss", true},
            {"Hms", true},
            {"hhmmssa", true},
            {"hhmmssaz", true},
            {"kkkmmss", true},
            {"KKmmss", true},
            {"KKmmssz", true},
            {"HH", true},
            {"mm", true},
            {"ss", true},
    };

    @DataPoints
    public static Object[][] wrongOrNotAcceptedHourFormats = {
            {"testHHmmss", false},
            {"HHmmssHH", false},
            {"HHmmamm", false},
            {"aHHmma", false},
            {"HHmmssTest", false},
    };

    @DataPoints
    public static Object[][] ambiguousHourFormatsWhichAreAlsoRegularWords = {
            {"sham", true},
            {"mass", true},
            {"mask", true},
            {"ham", true},
            {"hams", true},
            {"mash", true},
    };

    @Theory
    public void shouldCheckHourFormatsFromDataPoints(final Object...params) throws Exception {
        String line = (String) params[0];
        boolean expected = (Boolean)params[1];
        boolean actual = dateFormatHoursWordFilter.accept(line);

        assertThat(actual, is(expected));
    }

}