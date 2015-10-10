package com.epam.sonarqube.spellcheck.plugin.spellcheck;

import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.util.List;

import org.hamcrest.collection.IsIterableContainingInOrder;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

@RunWith(Theories.class)
public class JavaSourceCodeSplitterTest {

    private JavaSourceCodeSplitter javaSourceCodeSplitter;
    
    @DataPoints
    public static Object[][] params = {
            {"myCamelTestVariable", new String[]{"my", "Camel", "Test", "Variable"}},
            {"line with spaces", new String[]{"line", "with", "spaces"}},
            {"    a  lot  of   spaces everywhere   ", new String[]{"a", "lot", "of", "spaces", "everywhere"}},
            {"variableWithACRONYMInMiddle", new String[]{"variable", "With", "ACRONYM", "In", "Middle"}},
            {"ACRONYM", new String[]{"ACRONYM"}},
            {"ACRONYMAtTheBeginning", new String[]{"ACRONYM", "At", "The", "Beginning"}},
            {"$#*_ different non letter symbols %^$ ( in text *&&<>", new String[]{"different", "non", "letter", "symbols", "in", "text"}},
            {"CAPITAL_LETTERS_JAVA_CONSTANT", new String[]{"CAPITAL", "LETTERS", "JAVA", "CONSTANT"}},
            {"//inline comments", new String[]{"inline", "comments"}},
            {"my.test.package", new String[]{"my", "test", "package"}},
            {"MyClassName", new String[]{"My", "Class", "Name"}},
            {"@RequestMapping(value=\"/departments/{departmentId}\")", new String[]{"Request", "Mapping", "value", "departments", "department", "Id"}},
            {"List<UserProfile> userProfiles;", new String[]{"List", "User", "Profile", "user", "Profiles"}},
            {"variableWith345Digits12Everywhere234", new String[]{"variable", "With", "Digits", "Everywhere"}},
            {"/* comment for the method drawCircle() */", new String[]{"comment", "for", "the", "method", "draw", "Circle"}},
            {"http://www.google.com", new String[]{"http", "www", "google", "com"}},
    };
    
    @Before
    public void init() {
        javaSourceCodeSplitter = new JavaSourceCodeSplitter();
    }
    
    @Theory
    public void shouldCorrectlySplitLinesIntoWords(final Object...params) {
        String lineToSplit = (String)params[0];
        String[] expectedSplitWords = (String[])params[1]; 
        
        List<String> actualSplitWords = javaSourceCodeSplitter.split(lineToSplit);        
        
        assertThat(actualSplitWords, IsIterableContainingInOrder.contains(expectedSplitWords));
    }
    
    @Test
    public void shouldReturnEmptyListWhenSplitEmptyLine() {
        String lineToSplit = "";
        
        List<String> actualSplitWords = javaSourceCodeSplitter.split(lineToSplit);
        
        assertThat(actualSplitWords, is(empty()));
    }
    
}
