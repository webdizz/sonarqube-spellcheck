package com.epam.sonarqube.spellcheck.plugin.spellcheck.filter;

import java.util.regex.Pattern;

/**
 * DateFormatDaysWordFilter class checks if given word is a correct dateFormat accordingly to {@link java.text.SimpleDateFormat},
 * that represents DAYS.
 * Checks only formats written without any separator-characters.
 * It is assumed that in one dateFormat string, several groups of the same symbols are not allowed.
 * For example, allowed ones: MMddyyyy, yyMMdd, yyyyMMMdd, etc.
 * Not allowed ones: MMddyyyyMM, yyyyMMddyy, MMddMM, etc.
 */
public class DateFormatDaysWordFilter implements WordFilter {

    private static final String DAYS_FORMAT_REGEX =
            "(?:" +
            //look for DAYS format pattern symbols in line; assume that further in processed line, already found symbols are not allowed
            "(?:G{1,10})(?!.*G)" + //era designator
            "|(?:y{1,10})(?!.*y)" + //year
            "|(?:M{1,10})(?!.*M)" + //month in year
            "|(?:d{1,10})(?!.*d)" + //day in month

            "|(?:E{1,10})(?!.*E)" + //day in week
            "|(?:D{1,10})(?!.*D)" + //day in year
            "|(?:F{1,10})(?!.*F)" + //day of week in month
            "|(?:w{1,10})(?!.*w)" + //week in year
            "|(?:W{1,10})(?!.*W)" + //week in month

            "|(?:z{1,10})(?!.*z)" + //time zone

            ")+";

    private Pattern dateFormatsPattern = Pattern.compile(DAYS_FORMAT_REGEX);

    @Override
    public boolean accept(String word) {
        return dateFormatsPattern.matcher(word).matches();
    }
}
