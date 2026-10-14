package com.lostfound.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Enterprise JDBC Connection Manager supporting both MySQL and PostgreSQL.
 * Automatically bootstraps required tables and handles dialect detection.
 */
public class DatabaseConnection {

    private static final Logger LOGGER = Logger.getLogger(DatabaseConnection.class.getName());

    static {
        // 1. Try registering MySQL Driver
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            LOGGER.info("MySQL JDBC Driver registered successfully.");
        } catch (ClassNotFoundException e) {
            LOGGER.fine("MySQL JDBC Driver not found on classpath.");
        }

        // 2. Try registering PostgreSQL Driver
        try {
            Class.forName("org.postgresql.Driver");
            LOGGER.info("PostgreSQL JDBC Driver registered successfully.");
        } catch (ClassNotFoundException e) {
            LOGGER.fine("PostgreSQL JDBC Driver not found on classpath.");
        }
    }

    private DatabaseConnection() {
        // Utility class
    }

    /**
     * Opens a connection to the configured database (MySQL or PostgreSQL).
     */
    public static Connection getConnection() throws SQLException {
        String url = AppConfig.getDbUrl();
        String username = AppConfig.getDbUsername();
        String password = AppConfig.getDbPassword();

        if (url == null || url.isBlank()) {
            throw new SQLException("Database URL is not configured. Set DATABASE_URL, DB_URL, or DB_HOST in .env or environment variables.");
        }

        Connection conn = DriverManager.getConnection(url, username, password);
        try {
            DatabaseInitializer.initializeIfNeeded(conn);
            return conn;
        } catch (RuntimeException e) {
            closeQuietly(conn);
            throw new SQLException("Could not initialize the configured database: " + e.getMessage(), e);
        }
    }

    /**
     * Returns true if the active database connection is MySQL or MariaDB.
     */
    public static boolean isMySQL(Connection conn) {
        if (conn == null) return false;
        try {
            String productName = conn.getMetaData().getDatabaseProductName();
            return productName != null && (productName.toLowerCase().contains("mysql") || productName.toLowerCase().contains("mariadb"));
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Returns true if the active database connection is PostgreSQL.
     */
    public static boolean isPostgreSQL(Connection conn) {
        if (conn == null) return false;
        try {
            String productName = conn.getMetaData().getDatabaseProductName();
            return productName != null && productName.toLowerCase().contains("postgresql");
        } catch (Exception e) {
            return false;
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
