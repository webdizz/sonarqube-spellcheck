package com.epam.sonarqube.spellcheck.plugin.issue.tracking;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.ServerExtension;
import org.sonar.api.issue.Issue;
import org.sonar.api.issue.action.Function;
import org.sonar.api.issue.internal.DefaultIssue;
import org.sonar.api.issue.internal.IssueChangeContext;
import org.sonar.core.issue.IssueUpdater;
import org.sonar.core.properties.PropertiesDao;
import org.sonar.core.properties.PropertyDto;

import com.epam.sonarqube.spellcheck.plugin.PluginParameter;
import com.google.common.collect.ImmutableMap;

public class AddWordFromIssueFunction implements Function, ServerExtension {

    private static final Logger LOGGER = LoggerFactory.getLogger(AddWordFromIssueFunction.class);
    private final PropertiesDao propertiesDao;
    private final IssueUpdater issueUpdater;

    public AddWordFromIssueFunction(PropertiesDao propertiesDao, IssueUpdater issueUpdater) {
        this.propertiesDao = propertiesDao;
        this.issueUpdater = issueUpdater;
    }

    @Override
    public void execute(Context context) {
        Issue issue = context.issue();
        final String misspelledWord = issue.attribute(PluginParameter.SONAR_SPELL_CHECK_RULE_MISSPELLED_WORD);
        LOGGER.info("Here is an issue for which we need to add misspelledWord to dict: {}", issue);
        rememberNewWord(misspelledWord);
        final String resolvedNote = "Resolved by adding word to dictionary";
        context.addComment(resolvedNote);
        final IssueChangeContext issueChangeContext = IssueChangeContext.createScan(new Date());
        issueUpdater.setStatus((DefaultIssue) issue, Issue.STATUS_RESOLVED, issueChangeContext);
    }

    private void rememberNewWord(final String misspelledWord) {
        PropertyDto propertyDto = propertiesDao.selectGlobalProperty(PluginParameter.ALTERNATIVE_DICTIONARY_PROPERTY_KEY);
        if (propertyDto == null) {
            LOGGER.info("Creating new Dictionary. Adding misspelledWord '{}' to it.", misspelledWord);
            final Map<String, String> pluginProperties;
            pluginProperties = ImmutableMap.<String, String>builder().put(PluginParameter.ALTERNATIVE_DICTIONARY_PROPERTY_KEY, misspelledWord).build();
            propertiesDao.saveGlobalProperties(pluginProperties);
        } else {
            String dictionary = propertyDto.getValue();
            String[] wordArr = dictionary.split(PluginParameter.SEPARATOR_CHAR);
            ArrayList<String> wordList = new ArrayList<>(wordArr.length);
            for(String word : wordArr) {
                wordList.add(word.trim());
            }
            
            writeSortedIfUnique(misspelledWord, dictionary, wordList);
        }
    }

    private void writeSortedIfUnique(String misspelledWord, String dictionary, ArrayList<String> wordList) {
        if (wordList.contains(misspelledWord)) {
            LOGGER.info("Don't add. Word  '{}' is already in dictionary.", misspelledWord);
        } else {
            wordList.add(misspelledWord);
            Collections.sort(wordList);
            String sortedDictionary = StringUtils.join(wordList, PluginParameter.EXTENDED_SEPARATOR);
            propertiesDao.updateProperties(PluginParameter.ALTERNATIVE_DICTIONARY_PROPERTY_KEY, dictionary, sortedDictionary);
            LOGGER.info("Added misspelledWord '{}' to dictionary.", misspelledWord);
        }
    }

}