package com.lostfound.config;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.net.URI;
import java.net.URLDecoder;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Enterprise Application Configuration Manager.
 * Loads configuration from:
 * 1. Java System Properties (-Dkey=value)
 * 2. Operating System Environment Variables
 * 3. Local .env file (root / current dir)
 * 4. Classpath db.properties
 */
public class AppConfig {

    private static final Logger LOGGER = Logger.getLogger(AppConfig.class.getName());
    private static final Properties properties = new Properties();

    static {
        // 1. Load from classpath db.properties
        try (InputStream input = AppConfig.class.getClassLoader().getResourceAsStream("db.properties")) {
            if (input != null) {
                properties.load(input);
                LOGGER.info("db.properties loaded from classpath.");
            }
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Failed to load db.properties", e);
        }

        // 2. Load from .env file if available
        loadDotEnvFile();
    }

    private static void loadDotEnvFile() {
        String[] potentialPaths = {
                ".env",
                "../.env",
                System.getProperty("user.dir") + "/.env",
                System.getProperty("user.dir") + "/Campus/.env",
                System.getProperty("catalina.base", "") + "/.env"
        };

        for (String path : potentialPaths) {
            if (path == null || path.isBlank()) continue;
            File envFile = new File(path);
            if (envFile.exists() && envFile.isFile()) {
                try (BufferedReader reader = new BufferedReader(new FileReader(envFile, StandardCharsets.UTF_8))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        line = line.trim();
                        if (line.isEmpty() || line.startsWith("#")) {
                            continue;
                        }
                        int eqIdx = line.indexOf('=');
                        if (eqIdx > 0) {
                            String k = line.substring(0, eqIdx).trim();
                            String v = line.substring(eqIdx + 1).trim();
                            // Strip outer quotes if any
                            if ((v.startsWith("\"") && v.endsWith("\"")) || (v.startsWith("'") && v.endsWith("'"))) {
                                v = v.substring(1, v.length() - 1);
                            }
                            if (!k.isEmpty()) {
                                properties.setProperty(k, v);
                                // Also store with dot notation for versatility
                                properties.setProperty(k.toLowerCase().replace('_', '.'), v);
                            }
                        }
                    }
                    LOGGER.info("Successfully loaded environment variables from: " + envFile.getAbsolutePath());
                    return;
                } catch (Exception e) {
                    LOGGER.log(Level.WARNING, "Error reading .env at " + path, e);
                }
            }
        }
    }

    public static String getProperty(String key, String defaultValue) {
        String sysProp = System.getProperty(key);
        if (sysProp != null && !sysProp.isBlank()) {
            return sysProp;
        }

        String envKey = key.replace('.', '_').toUpperCase();
        String envVar = System.getenv(envKey);
        if (envVar != null && !envVar.isBlank()) {
            return envVar;
        }

        // Direct lookup or uppercase env-style lookup in properties
        String propVal = properties.getProperty(key);
        if (propVal != null && !propVal.isBlank()) {
            return propVal;
        }

        String envPropVal = properties.getProperty(envKey);
        if (envPropVal != null && !envPropVal.isBlank()) {
            return envPropVal;
        }

        return defaultValue;
    }

    public static String getDbUrl() {
        String databaseUrl = getProperty("DATABASE_URL", "");
        if (!databaseUrl.isBlank()) {
            return toJdbcUrl(databaseUrl);
        }

        String host = getProperty("DB_HOST", "");
        if (!host.isBlank()) {
            String dbType = getProperty("DB_TYPE", "").toLowerCase();
            String port = getProperty("DB_PORT", "");
            boolean isPostgres = "postgres".equals(dbType) || "postgresql".equals(dbType) || "5432".equals(port);

            String database = getProperty("DB_NAME", getProperty("DB_DATABASE", "lost_found_db"));
            if (isPostgres) {
                String pgPort = port.isBlank() ? "5432" : port;
                String sslMode = getProperty("DB_SSLMODE", "prefer");
                return "jdbc:postgresql://" + host + ":" + pgPort + "/" + database + "?sslmode=" + sslMode;
            } else {
                String mysqlPort = port.isBlank() ? "3306" : port;
                String ssl = "true".equalsIgnoreCase(getProperty("DB_SSL", "false")) || "require".equalsIgnoreCase(getProperty("DB_SSLMODE", "")) ? "true" : "false";
                return "jdbc:mysql://" + host + ":" + mysqlPort + "/" + database
                        + "?useSSL=" + ssl + "&allowPublicKeyRetrieval=true&serverTimezone=UTC&characterEncoding=UTF-8";
            }
        }

        String configuredUrl = getProperty("DB_URL", getProperty("db.url", ""));
        if (!configuredUrl.isBlank()) {
            return toJdbcUrl(configuredUrl);
        }

        // Default to local PostgreSQL; MySQL remains available through explicit configuration.
        return "jdbc:postgresql://localhost:5432/lost_found_db?sslmode=prefer";
    }

    public static String getDbUsername() {
        String configuredUsername = firstNonBlank(
                getProperty("DB_USER", ""),
                getProperty("DB_USERNAME", ""),
                databaseUrlCredential(true),
                getProperty("db.username", ""));
        if (!configuredUsername.isBlank()) {
            return configuredUsername;
        }
        String url = getDbUrl().toLowerCase();
        if (url.contains("mysql")) {
            return "root";
        }
        if (url.contains("postgresql")) {
            return "postgres";
        }
        return "";
    }

    public static String getDbPassword() {
        return firstNonBlank(
                getProperty("DB_PASSWORD", ""),
                databaseUrlCredential(false),
                getProperty("db.password", ""));
    }

    public static String toJdbcUrl(String configuredUrl) {
        String normalizedUrl = normalizeDatabaseUrl(configuredUrl);

        // 1. Direct JDBC URLs
        if (normalizedUrl.regionMatches(true, 0, "jdbc:mysql:", 0, "jdbc:mysql:".length())) {
            return normalizedUrl;
        }
        if (normalizedUrl.regionMatches(true, 0, "jdbc:postgresql:", 0, "jdbc:postgresql:".length())) {
            return "jdbc:postgresql:" + normalizedUrl.substring("jdbc:postgresql:".length());
        }

        // 2. MySQL URI format: mysql://user:pass@host:port/database
        if (normalizedUrl.regionMatches(true, 0, "mysql://", 0, "mysql://".length())) {
            URI uri = parseDatabaseUri(normalizedUrl);
            String database = uri.getRawPath();
            if (uri.getHost() == null || database == null || database.length() < 2) {
                throw new IllegalArgumentException("DATABASE_URL must include a database host and database name.");
            }
            int port = uri.getPort() > 0 ? uri.getPort() : 3306;
            String query = uri.getRawQuery();
            String queryParams;
            if (query != null && !query.isBlank()) {
                queryParams = "?" + query;
            } else {
                queryParams = "?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC&characterEncoding=UTF-8";
            }
            return "jdbc:mysql://" + uri.getHost() + ":" + port + database + queryParams;
        }

        // 3. PostgreSQL URI format: postgres:// or postgresql://
        if (normalizedUrl.regionMatches(true, 0, "postgres://", 0, "postgres://".length())
                || normalizedUrl.regionMatches(true, 0, "postgresql://", 0, "postgresql://".length())) {
            URI uri = parseDatabaseUri(normalizedUrl);
            String database = uri.getRawPath();
            if (uri.getHost() == null || database == null || database.length() < 2) {
                throw new IllegalArgumentException("DATABASE_URL must include a database host and database name.");
            }

            String query = uri.getRawQuery();
            if (query == null || !query.toLowerCase().contains("sslmode=")) {
                query = (query == null || query.isBlank() ? "" : query + "&") + "sslmode=require";
            }
            return "jdbc:postgresql://" + uri.getHost() + (uri.getPort() < 0 ? "" : ":" + uri.getPort())
                    + database + (query.isBlank() ? "" : "?" + query);
        }

        throw new IllegalArgumentException("Database URL configuration must use jdbc:mysql://, mysql://, "
                + "jdbc:postgresql://, postgres://, or postgresql://. Check DATABASE_URL or DB_URL.");
    }

    private static String databaseUrlCredential(boolean username) {
        String databaseUrl = normalizeDatabaseUrl(getProperty("DATABASE_URL", ""));
        if (databaseUrl.isBlank() || databaseUrl.regionMatches(true, 0, "jdbc:", 0, "jdbc:".length())) {
            return "";
        }

        String userInfo = parseDatabaseUri(databaseUrl).getRawUserInfo();
        if (userInfo == null) {
            return "";
        }
        int separator = userInfo.indexOf(':');
        String value = username
                ? (separator < 0 ? userInfo : userInfo.substring(0, separator))
                : (separator < 0 ? "" : userInfo.substring(separator + 1));
        return URLDecoder.decode(value.replace("+", "%2B"), StandardCharsets.UTF_8);
    }

    private static String normalizeDatabaseUrl(String configuredUrl) {
        String normalizedUrl = configuredUrl.trim();
        if (normalizedUrl.length() >= 2) {
            char first = normalizedUrl.charAt(0);
            char last = normalizedUrl.charAt(normalizedUrl.length() - 1);
            if ((first == '"' && last == '"') || (first == '\'' && last == '\'')) {
                normalizedUrl = normalizedUrl.substring(1, normalizedUrl.length() - 1).trim();
            }
        }
        return normalizedUrl;
    }

    private static URI parseDatabaseUri(String databaseUrl) {
        try {
            return URI.create(databaseUrl);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("DATABASE_URL is not a valid database connection URL.");
        }
    }

    private static String firstNonBlank(String... values) {
        for (String value : values) {
            if (value != null && !value.isBlank()) {
                return value;
            }
        }
        return "";
    }

    public static String getUploadDir() {
        return getProperty("upload.dir", getProperty("UPLOAD_DIR", "uploads/items"));
    }

    public static int getOtpExpiryMinutes() {
        return Integer.parseInt(getProperty("otp.expiry.minutes", getProperty("OTP_EXPIRY_MINUTES", "5")));
    }

    public static int getSessionTimeoutMinutes() {
        return Integer.parseInt(getProperty("session.timeout.minutes", getProperty("SESSION_TIMEOUT_MINUTES", "30")));
    }

}
