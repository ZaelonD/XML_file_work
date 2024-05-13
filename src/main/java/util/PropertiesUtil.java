package util;

import errors.IncorrectFileNameException;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import static log.MyLogger.LOGGER;

public final class PropertiesUtil {
    public static final Properties PROPERTIES = new Properties();

    static {
        loadProperties();
    }

    public static String getKey(String key) {
        return PROPERTIES.getProperty(key);
    }

    private static void loadProperties() {
        try (InputStream stream = PropertiesUtil.class.getClassLoader().getResourceAsStream("application.properties")) {
            PROPERTIES.load(stream);
            LOGGER.info("application.properties file uploaded successfully");
        } catch (IOException e) {
            LOGGER.error("Error loading properties file {}", e.getMessage());
            throw new IncorrectFileNameException("Error loading properties file", e);
        }
    }
}
