package com.lostfound;

import com.lostfound.config.AppConfig;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AppConfigTest {

    @Test
    public void testMySQLUriConversion() {
        String inputUrl = "mysql://myuser:mypass@localhost:3306/lost_found_db";
        String jdbcUrl = AppConfig.toJdbcUrl(inputUrl);

        assertTrue(jdbcUrl.startsWith("jdbc:mysql://localhost:3306/lost_found_db"));
        assertTrue(jdbcUrl.contains("useSSL="));
    }

    @Test
    public void testPostgreSQLUriConversion() {
        String inputUrl = "postgresql://pguser:pgpass@dbhost:5432/lost_found_db";
        String jdbcUrl = AppConfig.toJdbcUrl(inputUrl);

        assertTrue(jdbcUrl.startsWith("jdbc:postgresql://dbhost:5432/lost_found_db"));
        assertTrue(jdbcUrl.contains("sslmode=require"));
    }

    @Test
    public void testDirectJdbcUrlPassthrough() {
        String mysqlJdbc = "jdbc:mysql://localhost:3306/lost_found_db?useSSL=false";
        assertEquals(mysqlJdbc, AppConfig.toJdbcUrl(mysqlJdbc));

        String pgJdbc = "jdbc:postgresql://localhost:5432/lost_found_db?sslmode=prefer";
        assertEquals(pgJdbc, AppConfig.toJdbcUrl(pgJdbc));
    }

    @Test
    public void testInvalidUrlThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> {
            AppConfig.toJdbcUrl("invalid://not-a-db-url");
        });
    }
}
