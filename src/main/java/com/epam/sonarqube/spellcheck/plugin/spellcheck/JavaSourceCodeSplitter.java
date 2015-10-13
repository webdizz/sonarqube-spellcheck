package com.epam.sonarqube.spellcheck.plugin.spellcheck;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import com.epam.sonarqube.spellcheck.plugin.spellcheck.filter.WordFilter;
import org.apache.commons.lang.StringUtils;

import com.google.common.collect.ImmutableList;

public class JavaSourceCodeSplitter {

    private List<WordFilter> wordFilters = new LinkedList<>();

    public void addWordFilter(WordFilter wordFilter) {
        wordFilters.add(wordFilter);
    }

    public List<String> split(String text) {
        List<String> list = new LinkedList<String>();

        String[] words = text.split("[^a-zA-Z]");
        for (String word : words) {
            if (isWordFiltered(word)) { //skip words that are accepted by filter
                continue;
            }

            String[] wordsSplitByCamelCase = StringUtils
                    .splitByCharacterTypeCamelCase(word);

            list.addAll(Arrays.asList(wordsSplitByCamelCase));
        }

        return ImmutableList.<String> builder().addAll(list).build();
    }

    private boolean isWordFiltered(String word) {
        for (WordFilter wordFilter : wordFilters) {
            if (wordFilter.accept(word)) {
                return true;
            }
        }
        return false;
    }

}
