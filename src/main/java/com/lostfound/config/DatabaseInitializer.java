package com.lostfound.config;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Database Initializer and Bootstrapper.
 * Automatically checks and initializes required tables, seed data,
 * and schema upgrades on first startup.
 */
public class DatabaseInitializer {

    private static final Logger LOGGER = Logger.getLogger(DatabaseInitializer.class.getName());
    private static volatile boolean initialized = false;

    private DatabaseInitializer() {
        // Utility class
    }

    public static synchronized void initializeIfNeeded(Connection conn) {
        if (initialized || conn == null) {
            return;
        }

        try {
            boolean tablesExist = checkUsersTableExists(conn);
            if (!tablesExist) {
                if (hasApplicationTables(conn)) {
                    throw new SQLException("Application tables already exist but users is missing; refusing to initialize a partial schema.");
                }
                LOGGER.info("Users table not detected. Initializing database schema and seed data...");
                executeSchemaScript(conn);
                LOGGER.info("Database schema and seed records initialized successfully.");
            } else {
                LOGGER.info("Database tables verified.");
            }

            // Keep legacy account tables usable while removing obsolete external-auth data.
            runSchemaMigrations(conn);

            initialized = true;
            LOGGER.info("Database ready with verified schema.");

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed during automatic database schema initialization", e);
            throw new IllegalStateException("Database schema initialization failed.", e);
        }
    }

    private static boolean checkUsersTableExists(Connection conn) throws Exception {
        return tableExists(conn, "users");
    }

    private static boolean hasApplicationTables(Connection conn) throws Exception {
        for (String tableName : new String[]{"items", "claims", "categories", "reports", "notifications", "audit_logs"}) {
            if (tableExists(conn, tableName)) {
                return true;
            }
        }
        return false;
    }

    private static void runSchemaMigrations(Connection conn) throws Exception {
        if (!columnExists(conn, "users", "student_id")) {
            try (Statement st = conn.createStatement()) {
                st.execute("ALTER TABLE users ADD COLUMN student_id VARCHAR(50) DEFAULT NULL");
            }
        }

        try (Statement st = conn.createStatement();
             ResultSet duplicates = st.executeQuery(
                     "SELECT LOWER(email) FROM users GROUP BY LOWER(email) HAVING COUNT(*) > 1 LIMIT 1")) {
            if (duplicates.next()) {
                throw new SQLException("Duplicate login identifiers differ only by letter case. Resolve these rows before startup.");
            }
        }
        try (Statement st = conn.createStatement()) {
            st.execute("CREATE UNIQUE INDEX IF NOT EXISTS users_email_lower_uq ON users (LOWER(email))");
        }

        for (String obsoleteColumn : new String[]{"google_id", "profile_image", "email_verified", "auth_provider"}) {
            if (columnExists(conn, "users", obsoleteColumn)) {
                try (Statement st = conn.createStatement()) {
                    st.execute("ALTER TABLE users DROP COLUMN " + obsoleteColumn);
                }
            }
        }

        if (tableExists(conn, "otps")) {
            try (Statement st = conn.createStatement()) {
                st.execute("DROP TABLE otps");
            }
        }
    }

    private static boolean columnExists(Connection conn, String tableName, String columnName) throws Exception {
        try (ResultSet columns = conn.getMetaData().getColumns(conn.getCatalog(), conn.getSchema(), tableName, null)) {
            while (columns.next()) {
                if (columnName.equalsIgnoreCase(columns.getString("COLUMN_NAME"))) {
                    return true;
                }
            }
        }
        return false;
    }

    private static boolean tableExists(Connection conn, String tableName) throws Exception {
        try (ResultSet tables = conn.getMetaData().getTables(conn.getCatalog(), conn.getSchema(), tableName, new String[]{"TABLE"})) {
            while (tables.next()) {
                if (tableName.equalsIgnoreCase(tables.getString("TABLE_NAME"))) {
                    return true;
                }
            }
        }
        return false;
    }

    private static void executeSchemaScript(Connection conn) throws Exception {
        try (InputStream in = DatabaseInitializer.class.getClassLoader().getResourceAsStream("schema.sql")) {
            if (in == null) {
                throw new SQLException("schema.sql was not found on the application classpath.");
            }

            StringBuilder sb = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String trimmed = line.trim();
                    if (trimmed.startsWith("--") || trimmed.startsWith("//") || trimmed.isEmpty()) {
                        continue;
                    }
                    sb.append(line).append("\n");
                }
            }

            String[] statements = sb.toString().split(";");
            boolean originalAutoCommit = conn.getAutoCommit();
            conn.setAutoCommit(false);
            try {
                try (Statement stmt = conn.createStatement()) {
                    for (String rawSql : statements) {
                        String sql = rawSql.trim();
                        if (!sql.isEmpty()) {
                            stmt.execute(sql);
                        }
                    }
                }
                conn.commit();
            } catch (Exception e) {
                try {
                    conn.rollback();
                } catch (SQLException rollbackError) {
                    e.addSuppressed(rollbackError);
                }
                throw e;
            } finally {
                conn.setAutoCommit(originalAutoCommit);
            }
        }
    }
}
