package com.epam.sonarqube.spellcheck.plugin.spellcheck;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.google.common.collect.ImmutableList;

public class JavaSourceCodeSplitter {

    public List<String> split(String text) {
        List<String> list = new LinkedList<String>();

        String[] words = text.split("[^a-zA-Z]");
        for (String word : words) {
            String[] wordsSplitByCamelCase = StringUtils
                    .splitByCharacterTypeCamelCase(word);

            list.addAll(Arrays.asList(wordsSplitByCamelCase));
        }

        return ImmutableList.<String> builder().addAll(list).build();
    }

}
