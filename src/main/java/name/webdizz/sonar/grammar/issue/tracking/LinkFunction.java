package name.webdizz.sonar.grammar.issue.tracking;

import com.google.common.base.Preconditions;
import name.webdizz.sonar.grammar.GrammarPlugin;
import name.webdizz.sonar.grammar.PluginParameter;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.ServerExtension;
import org.sonar.api.config.Settings;
import org.sonar.api.issue.action.Function;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.apache.commons.lang.StringUtils.stripToEmpty;

public class LinkFunction implements Function, ServerExtension {

    private static final Logger LOGGER = LoggerFactory.getLogger(LinkFunction.class);

    @Override
    public void execute(Context context) {
        String word = getMistakeWord(context.issue().message(), PluginParameter.ERROR_DESCRIPTION);
        LOGGER.info("try to write:" + word);
        Settings settings = context.projectSettings();
        String dict = stripToEmpty(settings.getString(PluginParameter.MANUAL_DICT));
        saveProperties(dict + "," + word, PluginParameter.MANUAL_DICT, context);
    }

    private String getMistakeWord(String message, String errDescription) {
        String res = message.replaceFirst(errDescription, "");
        return res.substring(0, res.length() - 1);
    }

    private void saveProperties(String word, String dictName, Context context) {
        try {
            String hostPort = checkNotNull(getProperty(context, GrammarPlugin.HOST_PORT));
            String credentials = checkNotNull(getProperty(context, GrammarPlugin.CREDENTIAL));

            String[] location = hostPort.split(":");
            Preconditions.checkArgument(location.length == 2, "Illegal sonar location format in grammar settings");

            HttpURLConnection connection = getConnection(word, dictName, credentials, location);
            InputStream content = connection.getInputStream();
            BufferedReader in = new BufferedReader(new InputStreamReader(content));
            LOGGER.info("Saving word response:" + in.readLine());
        } catch (IOException e) {
            LOGGER.error("Cannot connect to Sonar for updating dictionary." + e.getMessage());
            throw new RuntimeException("Cannot save word to dictionary");
        }
    }

    private HttpURLConnection getConnection(String word, String dictName, String credentials, String[] location) throws IOException {
        String urlString = String.format("http://%s:%s/api/properties?id=%s&value=%s", location[0], location[1], dictName, word);
        LOGGER.info("URL:" + urlString);
        URL url = new URL(urlString);

        Base64 b = new Base64();
        String encoding = b.encodeAsString(credentials.getBytes());

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);
        connection.setRequestProperty("Authorization", "Basic " + encoding);
        return connection;
    }

    private String getProperty(Context context, String propertyName) {
        Settings settings = context.projectSettings();
        return settings.getString(propertyName);
    }
}