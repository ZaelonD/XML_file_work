package util;

import errors.ConnectionErrorException;
import errors.DriverLoadingException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static log.MyLogger.LOGGER;

public final class ConnectionManager {
    public static final String URL_KEY = "db.url";
    public static final String USERNAME_KEY = "db.username";
    public static final String PASSWORD_KEY = "db.password";


    static {
        loadDriver();
    }

    public static Connection open() {
        try {
            Connection connection = DriverManager.getConnection(PropertiesUtil.getKey(URL_KEY),
                    PropertiesUtil.getKey(USERNAME_KEY),
                    PropertiesUtil.getKey(PASSWORD_KEY));
            LOGGER.info("Database connection completed successfully");
            return connection;
        } catch (SQLException e) {
            LOGGER.error("Failed to connect to database {}", e.getMessage());
            throw new ConnectionErrorException("Failed to connect to database", e);
        }
    }

    private static void loadDriver() {
        try {
            Class.forName("org.postgresql.Driver");
            LOGGER.info("JDBC driver loaded successfully");
        } catch (ClassNotFoundException e) {
            LOGGER.error("Failed to load JDBC driver {}", e.getMessage());
            throw new DriverLoadingException("Failed to load JDBC driver", e);
        }
    }
}
