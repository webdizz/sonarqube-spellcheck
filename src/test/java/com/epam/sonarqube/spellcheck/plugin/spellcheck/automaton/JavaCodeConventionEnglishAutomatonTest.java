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
    public void testInit() throws Exception {
        assertEquals(-1, automaton.getWordStart());
        assertEquals(0, automaton.getWordEnd());
    }

    @Test
    public void testHasNextWordNoNextWord() throws Exception {
        assertFalse(automaton.hasNextWord());
    }

    @Test
    public void testHasNextWordNextWordWasFound() throws Exception {
        automaton.searchNextWord("word", 0);

        assertTrue(automaton.hasNextWord());
    }

    @Test
    public void testSearchNextWordIndexMoreThanTextLength() throws Exception {
        automaton.searchNextWord("word", 10);

        assertFalse(automaton.hasNextWord());
    }

    @Test
    public void testSearchNextWordOneWordOnlySmallLetters() throws Exception {
        String text = "variable";
        String[] expected = { "variable" };
        String[] actual = parseText(text);

        assertArrayEquals(expected, actual);
    }

    @Test
    public void testSearchNextWordOneWordFirstLetterCapitalOtherSmall()
            throws Exception {
        String text = "Variable";
        String[] expected = { "Variable" };
        String[] actual = parseText(text);

        assertArrayEquals(expected, actual);
    }

    @Test
    public void testSearchNextWordOneWordOnlyCapitalLetters() throws Exception {
        String text = "SQL";
        String[] expected = { "SQL" };
        String[] actual = parseText(text);

        assertArrayEquals(expected, actual);
    }

    @Test
    public void testSearchNextWordTwoWordsFirstCapitalLetterInEach()
            throws Exception {
        String text = "FinalState";
        String[] expected = { "Final", "State" };
        String[] actual = parseText(text);

        assertArrayEquals(expected, actual);
    }

    @Test
    public void testSearchNextWordTwoWordsCamelCase() throws Exception {
        String text = "finalState";
        String[] expected = { "final", "State" };
        String[] actual = parseText(text);

        assertArrayEquals(expected, actual);
    }

    @Test
    public void testSearchNextWordPackageName() throws Exception {
        String text = "foo.bar.spellcheck.automaton";
        String[] expected = { "foo", "bar", "spellcheck", "automaton" };
        String[] actual = parseText(text);

        assertArrayEquals(expected, actual);
    }

    @Test
    public void testSearchNextWordAnnotation() throws Exception {
        String text = "@RetentionPolicy";
        String[] expected = { "Retention", "Policy" };
        String[] actual = parseText(text);

        assertArrayEquals(expected, actual);
    }

    @Test
    public void testSearchNextWordGeneric() throws Exception {
        String text = "List<UserProfile> userProfiles;";
        String[] expected = { "List", "User", "Profile", "user", "Profiles" };
        String[] actual = parseText(text);

        assertArrayEquals(expected, actual);
    }

    @Test
    public void testSearchNextWordAbbreviationInClassName() throws Exception {
        String text = "JPAUserRepository";
        String[] expected = { "JPA", "User", "Repository" };
        String[] actual = parseText(text);

        assertArrayEquals(expected, actual);
    }

    @Test
    public void testSearchNextWordAbbreviationInClassNameInMiddle()
            throws Exception {
        String text = "UserJPARepository";
        String[] expected = { "User", "JPA", "Repository" };
        String[] actual = parseText(text);

        assertArrayEquals(expected, actual);
    }

    @Test
    public void testSearchNextWordAbbreviationOneLetterInClassNameInMiddle()
            throws Exception {
        String text = "UserJRepository";
        String[] expected = { "User", "J", "Repository" };
        String[] actual = parseText(text);

        assertArrayEquals(expected, actual);
    }

    @Test
    public void testSearchNextWordConstant() throws Exception {
        String text = "MAX_INT_THRESHOLD";
        String[] expected = { "MAX", "INT", "THRESHOLD" };
        String[] actual = parseText(text);

        assertArrayEquals(expected, actual);
    }

    @Test
    public void testSearchNextWordMixed1() throws Exception {
        String text = " _first7Very222Long#@^:$=-'Variable   ";
        String[] expected = { "first", "Very", "Long", "Variable" };
        String[] actual = parseText(text);

        assertArrayEquals(expected, actual);
    }

    @Test
    public void testSearchNextWordMixed2() throws Exception {
        String text = "	Line with tabs 		and     several       spaces";
        String[] expected = { "Line", "with", "tabs", "and", "several",
                "spaces" };
        String[] actual = parseText(text);

        assertArrayEquals(expected, actual);
    }

    @Test
    public void testSearchNextWordComment() throws Exception {
        String text = "/* comment for the method drawCircle() */";
        String[] expected = { "comment", "for", "the", "method", "draw",
                "Circle" };
        String[] actual = parseText(text);

        assertArrayEquals(expected, actual);
    }

    @Test
    public void testSearchNextWordLineComment() throws Exception {
        String text = "//line comment";
        String[] expected = { "line", "comment" };
        String[] actual = parseText(text);

        assertArrayEquals(expected, actual);
    }

    @Test
    public void testSearchNextWordUrl() throws Exception {
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
    public void testGetWordStart() throws Exception {
        automaton.searchNextWord("testSingleVariable", 4);

        assertEquals(4, automaton.getWordStart());
    }

    @Test
    public void testGetWordEnd() throws Exception {
        automaton.searchNextWord("testSingleVariable", 4);

        assertEquals(10, automaton.getWordEnd());
    }

}
