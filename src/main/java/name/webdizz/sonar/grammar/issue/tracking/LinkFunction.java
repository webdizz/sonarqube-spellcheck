package name.webdizz.sonar.grammar.issue.tracking;

import name.webdizz.sonar.grammar.PluginParameter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.ServerExtension;
import org.sonar.api.issue.action.Function;
import org.sonar.core.properties.PropertiesDao;

import static name.webdizz.sonar.grammar.PluginParameter.MANUAL_DICT;

public class LinkFunction implements Function, ServerExtension {

    private static final Logger LOGGER = LoggerFactory.getLogger(LinkFunction.class);
    private PropertiesDao propertiesDao;

    public LinkFunction(PropertiesDao propertiesDao) {
        this.propertiesDao = propertiesDao;
    }

    @Override
    public void execute(Context context) {
        String word = getMistakeWord(context.issue().message(), PluginParameter.ERROR_DESCRIPTION);
        try {
            String dictionary = propertiesDao.selectGlobalProperty(MANUAL_DICT).getValue();
            LOGGER.info("Adding word {} to dictionary",  word);
            propertiesDao.updateProperties(MANUAL_DICT, dictionary, dictionary + "," + word);
        } catch (Exception e) {
            throw new IllegalStateException("Can't save word to dictionary" + e.getMessage());
        }
    }

    private String getMistakeWord(String message, String errDescription) {
        String res = message.replaceFirst(errDescription, "");
        return res.substring(0, res.length() - 1);
    }
}