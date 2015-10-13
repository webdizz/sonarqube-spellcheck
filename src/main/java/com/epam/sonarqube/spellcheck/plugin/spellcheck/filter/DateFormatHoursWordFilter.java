package com.epam.sonarqube.spellcheck.plugin.spellcheck.filter;

import java.util.regex.Pattern;

/**
 * DateFormatHoursWordFilter class checks if given word is a correct dateFormat accordingly to {@link java.text.SimpleDateFormat},
 * that represents HOURS.
 * Checks only formats written without any separator-characters.
 * It is assumed that in one dateFormat string, several groups of the same symbols are not allowed.
 * For example, allowed ones: HHmmss, HHmm, hmmssa, etc.
 * Not allowed ones: HHmmssmm, HHmmaHH, aHHmma, etc.
 */
public class DateFormatHoursWordFilter implements WordFilter {

    private static final String HOURS_FORMAT_REGEX =
            "(?:" +
            //look for HOURS format pattern symbols in line; assume that further in processed line, already found symbols are not allowed
            "|(?:h{1,10})(?!.*h)" + //hour in am/pm (1-12)
            "|(?:H{1,10})(?!.*H)" + //hour in day (0-23)
            "|(?:m{1,10})(?!.*m)" + //minute in hour
            "|(?:s{1,10})(?!.*s)" + //second in minute
            "|(?:S{1,10})(?!.*S)" + //millisecond

            "|(?:a{1,10})(?!.*a)" + //am/pm marker
            "|(?:k{1,10})(?!.*k)" + //hour in day (1-24)
            "|(?:K{1,10})(?!.*K)" + //hour in am/pm (0-11)
            "|(?:z{1,10})(?!.*z)" + //time zone

            ")+";

    private Pattern dateFormatsPattern = Pattern.compile(HOURS_FORMAT_REGEX);

    @Override
    public boolean accept(String word) {
        return dateFormatsPattern.matcher(word).matches();
    }
}
