package name.webdizz.sonar.grammar.issue.tracking;

import name.webdizz.sonar.grammar.PluginParameter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.ServerExtension;
import org.sonar.api.issue.action.Function;
import org.sonar.core.properties.PropertiesDao;
import org.sonar.core.properties.PropertyDto;

import java.util.HashMap;

import static name.webdizz.sonar.grammar.PluginParameter.ALTERNATIVE_DICTIONARY_PROPERTY_KEY;

public class LinkFunction implements Function, ServerExtension {

    private static final Logger LOGGER = LoggerFactory.getLogger(LinkFunction.class);
    private PropertiesDao propertiesDao;

    public LinkFunction(PropertiesDao propertiesDao) {
        this.propertiesDao = propertiesDao;
    }

    @Override
    public void execute(Context context) {
        final String word = getMistakeWord(context.issue().message(), PluginParameter.ERROR_DESCRIPTION);
            PropertyDto propertyDto = propertiesDao.selectGlobalProperty(ALTERNATIVE_DICTIONARY_PROPERTY_KEY);
            if (propertyDto == null){
                LOGGER.info("Creating new Dictionary. Adding word '{}' to it.", word);
                propertiesDao.saveGlobalProperties(new HashMap<String, String>(){{put(ALTERNATIVE_DICTIONARY_PROPERTY_KEY, word);}});
            } else{
                LOGGER.info("Adding word '{}' to dictionary.", word);
                String dictionary  = propertyDto.getValue();
                propertiesDao.updateProperties(ALTERNATIVE_DICTIONARY_PROPERTY_KEY, dictionary , dictionary + "," + word);
            }
    }

    private String getMistakeWord(String message, String errDescription) {
        String res = message.replaceFirst(errDescription, "");
        return res.substring(0, res.length() - 1);
    }
}