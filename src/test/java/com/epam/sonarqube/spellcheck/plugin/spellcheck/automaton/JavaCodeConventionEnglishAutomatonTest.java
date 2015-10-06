package com.epam.sonarqube.spellcheck.plugin.spellcheck.automaton;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.LinkedList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class JavaCodeConventionEnglishAutomatonTest {

    private JavaCodeConventionEnglishAutomaton automaton;

    @Before
    public void init() {
        automaton = new JavaCodeConventionEnglishAutomaton();
        automaton.init();
    }

    @Test
    public void shouldCheckAutomatonAfterInitAndMustHaveDefaultWordBoundsValues() throws Exception {
        int expectedWordStart = 0;
        int expectedWordEnd = 0;
        
        assertEquals(expectedWordStart, automaton.getWordStart());
        assertEquals(expectedWordEnd, automaton.getWordEnd());
    }

    @Test
    public void shouldCheckAutomatonAfterInitAndMustHaveNoNextWord() throws Exception {
        assertFalse(automaton.hasNextWord());
    }

    @Test
    public void shouldSearchNextWordInTextAndMustFindNextWord() throws Exception {
        String lineToSearchWord = "variable";
        int beginIndex = 0;
        automaton.searchNextWord(lineToSearchWord, beginIndex);

        assertTrue(automaton.hasNextWord());
    }

    @Test
    public void shouldSearchNextWordWithIndexMoreThanTextLengthAndMustNotFindNextWord() throws Exception {
        String lineToSearchWord = "variable";
        int beginIndexGreaterThanLineLength = 15;
        automaton.searchNextWord(lineToSearchWord, beginIndexGreaterThanLineLength);

        assertFalse(automaton.hasNextWord());
    }

    @Test
    public void shouldFindOneWordOneWordOnlySmallLetters() throws Exception {
        String text = "variable";
        String[] expected = { "variable" };
        String[] actual = parseText(text);

        assertArrayEquals(expected, actual);
    }

    @Test
    public void shouldFindOneWordFirstLetterCapitalOtherSmall()
            throws Exception {
        String text = "Variable";
        String[] expected = { "Variable" };
        String[] actual = parseText(text);

        assertArrayEquals(expected, actual);
    }

    @Test
    public void shouldFindOneWordOnlyCapitalLetters() throws Exception {
        String text = "SQL";
        String[] expected = { "SQL" };
        String[] actual = parseText(text);

        assertArrayEquals(expected, actual);
    }

    @Test
    public void shouldFindTwoWordsFirstCapitalLetterInEach()
            throws Exception {
        String text = "FinalState";
        String[] expected = { "Final", "State" };
        String[] actual = parseText(text);

        assertArrayEquals(expected, actual);
    }

    @Test
    public void shouldFindTwoWordsCamelCase() throws Exception {
        String text = "finalState";
        String[] expected = { "final", "State" };
        String[] actual = parseText(text);

        assertArrayEquals(expected, actual);
    }

    @Test
    public void shouldFindFourWordsInPackageName() throws Exception {
        String text = "foo.bar.spellcheck.automaton";
        String[] expected = { "foo", "bar", "spellcheck", "automaton" };
        String[] actual = parseText(text);

        assertArrayEquals(expected, actual);
    }

    @Test
    public void shouldFindTwoWordsInAnnotation() throws Exception {
        String text = "@RetentionPolicy";
        String[] expected = { "Retention", "Policy" };
        String[] actual = parseText(text);

        assertArrayEquals(expected, actual);
    }
    
    @Test
    public void shouldFindFourWordsInAnnotationWithParam() throws Exception {
        String text = "@SuppressWarnings(\"squid:S1161\")";
        String[] expected = { "Suppress", "Warnings", "squid", "S" };
        String[] actual = parseText(text);
        
        assertArrayEquals(expected, actual);
    }

    @Test
    public void shouldFindFiveWordsInGeneric() throws Exception {
        String text = "List<UserProfile> userProfiles;";
        String[] expected = { "List", "User", "Profile", "user", "Profiles" };
        String[] actual = parseText(text);

        assertArrayEquals(expected, actual);
    }

    @Test
    public void shouldFindThreeWordsWithAbbreviationInClassName() throws Exception {
        String text = "JPAUserRepository";
        String[] expected = { "JPA", "User", "Repository" };
        String[] actual = parseText(text);

        assertArrayEquals(expected, actual);
    }

    @Test
    public void shouldFindThreeWordsWithAbbreviationInClassNameInMiddle()
            throws Exception {
        String text = "UserJPARepository";
        String[] expected = { "User", "JPA", "Repository" };
        String[] actual = parseText(text);

        assertArrayEquals(expected, actual);
    }

    @Test
    public void shouldFindOneLetterAbbreviationInClassNameInMiddle()
            throws Exception {
        String text = "UserJRepository";
        String[] expected = { "User", "J", "Repository" };
        String[] actual = parseText(text);

        assertArrayEquals(expected, actual);
    }

    @Test
    public void shouldFindThreeWordsInConstant() throws Exception {
        String text = "MAX_INT_THRESHOLD";
        String[] expected = { "MAX", "INT", "THRESHOLD" };
        String[] actual = parseText(text);

        assertArrayEquals(expected, actual);
    }

    @Test
    public void shouldFindFourWordsInMixedVariableWithNonEnglishLettersSymbols() throws Exception {
        String text = " _first7Very222Long#@^:$=-'Variable   ";
        String[] expected = { "first", "Very", "Long", "Variable" };
        String[] actual = parseText(text);

        assertArrayEquals(expected, actual);
    }

    @Test
    public void shouldFindFiveWordsInMixedVariableWithSpaces() throws Exception {
        String text = "	Line with tabs 		and     several       spaces";
        String[] expected = { "Line", "with", "tabs", "and", "several",
                "spaces" };
        String[] actual = parseText(text);

        assertArrayEquals(expected, actual);
    }

    @Test
    public void shouldFindFiveWordsInComment() throws Exception {
        String text = "/* comment for the method drawCircle() */";
        String[] expected = { "comment", "for", "the", "method", "draw",
                "Circle" };
        String[] actual = parseText(text);

        assertArrayEquals(expected, actual);
    }

    @Test
    public void shouldFindTwoWordsInLineComment() throws Exception {
        String text = "//line comment";
        String[] expected = { "line", "comment" };
        String[] actual = parseText(text);

        assertArrayEquals(expected, actual);
    }

    @Test
    public void shouldFindFourWordsInUrl() throws Exception {
        String text = "http://www.google.com";
        String[] expected = { "http", "www", "google", "com" };
        String[] actual = parseText(text);

        assertArrayEquals(expected, actual);
    }

    /**
     * Helper method to break text into array of words
     */
    private String[] parseText(String text) {
        List<String> list = new LinkedList<String>();

        automaton.searchNextWord(text, 0);
        while (automaton.hasNextWord()) {
            list.add(text.substring(automaton.getWordStart(),
                    automaton.getWordEnd()));
            int beginIndex = automaton.getWordEnd();
            automaton.searchNextWord(text, beginIndex);
        }

        return list.toArray(new String[0]);
    }

    @Test
    public void shouldSearchForNextWordFromStartOfTextAndMustProvideCorrectWordStartBoundary() throws Exception {
        String lineToSearchWord = "testSingleVariable";
        int beginIndex = 0;
        int expectedWordStart = 0;
        automaton.searchNextWord(lineToSearchWord, beginIndex);

        assertEquals(expectedWordStart, automaton.getWordStart());
    }
    
    @Test
    public void shouldSearchForNextWordFromMiddleOfTextAndMustProvideCorrectWordStartBoundary() throws Exception {
        String lineToSearchWord = "testSingleVariable";
        int beginIndex = 4;
        int expectedWordStart = 4;
        automaton.searchNextWord(lineToSearchWord, beginIndex);

        assertEquals(expectedWordStart, automaton.getWordStart());
    }
    
    @Test
    public void shouldSearchForNextWordFromStartOfTextAndMustProvideCorrectWordEndBoundary() throws Exception {
        String lineToSearchWord = "testSingleVariable";
        int beginIndex = 0;
        int expectedWordEnd = 4;
        automaton.searchNextWord(lineToSearchWord, beginIndex);

        assertEquals(expectedWordEnd, automaton.getWordEnd());
    }
    
    @Test
    public void shouldSearchForNextWordFromMiddleOfTextAndMustProvideCorrectWordEndBoundary() throws Exception {
        String lineToSearchWord = "testSingleVariable";
        int beginIndex = 4;
        int expectedWordEnd = 10;
        automaton.searchNextWord(lineToSearchWord, beginIndex);

        assertEquals(expectedWordEnd, automaton.getWordEnd());
    }

}
