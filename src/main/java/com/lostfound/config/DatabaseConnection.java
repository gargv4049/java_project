package com.lostfound.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
<<<<<<< HEAD
 * MySQL JDBC connection manager with automatic table bootstrapping.
=======
 * PostgreSQL JDBC connection manager with automatic table bootstrapping.
>>>>>>> 67eb56a (Convert application database to PostgreSQL)
 */
public class DatabaseConnection {

    private static final Logger LOGGER = Logger.getLogger(DatabaseConnection.class.getName());

    static {
        try {
<<<<<<< HEAD
            Class.forName("com.mysql.cj.jdbc.Driver");
            LOGGER.info("MySQL JDBC Driver registered.");
        } catch (ClassNotFoundException e) {
            LOGGER.warning("MySQL driver not found in classpath.");
=======
            Class.forName("org.postgresql.Driver");
            LOGGER.info("PostgreSQL JDBC Driver registered.");
        } catch (ClassNotFoundException e) {
            throw new ExceptionInInitializerError(e);
>>>>>>> 67eb56a (Convert application database to PostgreSQL)
        }
    }

    private DatabaseConnection() {
        // Utility class
    }

    /**
<<<<<<< HEAD
     * Opens the configured MySQL database. Credentials must be supplied outside source control.
=======
     * Opens the configured PostgreSQL database. Credentials must be supplied outside source control.
>>>>>>> 67eb56a (Convert application database to PostgreSQL)
     */
    public static Connection getConnection() throws SQLException {
        String url = AppConfig.getDbUrl();
        String username = AppConfig.getDbUsername();
        String password = AppConfig.getDbPassword();
        if (username.isBlank() || password.isBlank()) {
            throw new SQLException("Database credentials are not configured. Set DB_USERNAME and DB_PASSWORD outside the project.");
        }

<<<<<<< HEAD
        try {
            Connection conn = DriverManager.getConnection(url, username, password);
            try {
                DatabaseInitializer.initializeIfNeeded(conn);
                return conn;
            } catch (RuntimeException e) {
                closeQuietly(conn);
                throw new SQLException("Could not initialize the configured database.", e);
            }
        } catch (SQLException e) {
            String errorMsg = e.getMessage() != null ? e.getMessage().toLowerCase() : "";

            // If database name is missing, attempt to create it on MySQL server
            if (errorMsg.contains("unknown database") && url.contains("/lost_found_db")) {
                try {
                    String baseUrl = url.substring(0, url.indexOf("/lost_found_db")) + "/?useSSL=false&allowPublicKeyRetrieval=true";
                    try (Connection serverConn = DriverManager.getConnection(baseUrl, username, password);
                         java.sql.Statement stmt = serverConn.createStatement()) {
                        stmt.executeUpdate("CREATE DATABASE IF NOT EXISTS lost_found_db CHARACTER SET utf8mb4");
                        LOGGER.info("Auto-created MySQL database 'lost_found_db'.");
                    }
                    Connection conn = DriverManager.getConnection(url, username, password);
                    try {
                        DatabaseInitializer.initializeIfNeeded(conn);
                        return conn;
                    } catch (RuntimeException initException) {
                        closeQuietly(conn);
                        throw new SQLException("Could not initialize the configured database.", initException);
                    }
                } catch (Exception ex) {
                    LOGGER.log(Level.WARNING, "Could not auto-create the configured MySQL database.", ex);
                }
            }

            LOGGER.log(Level.SEVERE, "Unable to connect to the configured MySQL database.", e);
            throw e;
=======
        Connection conn = DriverManager.getConnection(url, username, password);
        try {
            DatabaseInitializer.initializeIfNeeded(conn);
            return conn;
        } catch (RuntimeException e) {
            closeQuietly(conn);
            throw new SQLException("Could not initialize the configured PostgreSQL database.", e);
>>>>>>> 67eb56a (Convert application database to PostgreSQL)
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
