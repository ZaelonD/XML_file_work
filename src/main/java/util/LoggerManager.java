package util;

import errors.LoggerPropertiesLoadingException;

import java.util.logging.LogManager;

import static log.MyLogger.LOGGER;

public final class LoggerManager {

    static {
        loadLoggerProperties();
    }

    private static void loadLoggerProperties() {
        try {
            LogManager.getLogManager().readConfiguration(
                    LoggerManager.class.getResourceAsStream("log4j.properties"));
            LOGGER.error("log4j.properties file loaded successfully");
        } catch (Exception e) {
            LOGGER.error("Error loading logger properties", e);
            throw new LoggerPropertiesLoadingException("Error loading logger properties", e);
        }
    }
}
