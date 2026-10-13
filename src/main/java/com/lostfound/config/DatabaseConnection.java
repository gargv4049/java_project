package com.lostfound.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * PostgreSQL JDBC connection manager with automatic table bootstrapping.
 */
public class DatabaseConnection {

    private static final Logger LOGGER = Logger.getLogger(DatabaseConnection.class.getName());

    static {
        try {
            Class.forName("org.postgresql.Driver");
            LOGGER.info("PostgreSQL JDBC Driver registered.");
        } catch (ClassNotFoundException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    private DatabaseConnection() {
        // Utility class
    }

    /**
     * Opens the configured PostgreSQL database. Credentials must be supplied outside source control.
     */
    public static Connection getConnection() throws SQLException {
        String url = AppConfig.getDbUrl();
        String username = AppConfig.getDbUsername();
        String password = AppConfig.getDbPassword();
        if (username.isBlank() || password.isBlank()) {
            throw new SQLException("PostgreSQL credentials are missing. Configure DATABASE_URL, DB_USER/DB_PASSWORD, or DB_USERNAME/DB_PASSWORD.");
        }

        Connection conn = DriverManager.getConnection(url, username, password);
        try {
            DatabaseInitializer.initializeIfNeeded(conn);
            return conn;
        } catch (RuntimeException e) {
            closeQuietly(conn);
            throw new SQLException("Could not initialize the configured PostgreSQL database.", e);
        }
    }

    /**
     * Safely closes an AutoCloseable resource (Connection, Statement, ResultSet).
     */
    public static void closeQuietly(AutoCloseable resource) {
        if (resource != null) {
            try {
                resource.close();
            } catch (Exception e) {
                LOGGER.log(Level.WARNING, "Error closing database resource", e);
            }
        }
    }
}
