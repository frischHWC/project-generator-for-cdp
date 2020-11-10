package {{ package_name }}.config;

{% if logger is sameas true %}import org.apache.log4j.Logger;{% endif %}

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;


public class AppConfig {

    {% if logger is sameas true %}private static final Logger logger = Logger.getLogger(AppConfig.class);{% endif %}

    public static final Properties properties = loadProperties();

    private static Properties loadProperties() {
        // Load config file
        Properties properties = new Properties();
        try {
            FileInputStream fileInputStream = new FileInputStream("application.properties");
            properties.load(fileInputStream);
        } catch (IOException e) {
            {% if logger is sameas true %}logger.error("Property file not found !", e);
             {% else %}System.out.println("Property file not found !");{% endif %}
        }
        return properties;
    }

    public static String getProperty(String key) {
        String property = "null";
        try {
             property = properties.getProperty(key);
            if(property.substring(0,2).equalsIgnoreCase("${")) {
                property = AppConfig.getProperty(property.substring(2,property.length()-1));
            }
        } catch (Exception e) {
            {% if logger is sameas true %}logger.warn("Could not get property : " + key + " due to following error: ", e);
            {% else %}System.out.println("Could not get property : " + key + " due to following error: " + e);{% endif %}
        }
        return property;
    }
}
