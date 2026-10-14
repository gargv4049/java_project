package com.lostfound;

import com.lostfound.member1_auth.util.BCryptUtil;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class BCryptUtilTest {

    @Test
    public void testHashAndCheckPassword() {
        String rawPassword = "SecurePassword@2026";
        String hashed = BCryptUtil.hashPassword(rawPassword);

        assertNotNull(hashed);
        assertTrue(hashed.startsWith("$2a$") || hashed.startsWith("$2b$") || hashed.startsWith("$2y$"));
        assertTrue(BCryptUtil.checkPassword(rawPassword, hashed));
        assertFalse(BCryptUtil.checkPassword("WrongPassword", hashed));
    }

    @Test
    public void testSeedAccountsPasswordMatch() {
        // Test Admin@123 hash from schema
        String adminHash = "$2a$12$DVqPlxJjF5BYQhBschxxpe2FCcsvosDaiVTLZ9WxxfuWQIXSXL0c2";
        assertTrue(BCryptUtil.checkPassword("Admin@123", adminHash));

        // Test Student@123 hash from schema
        String studentHash = "$2a$12$XnPZP6EfWf1J87Qvix6W9O5nsItDmcI0co289V/T7O939Wqn3UsR.";
        assertTrue(BCryptUtil.checkPassword("Student@123", studentHash));

        // Test Faculty@123 hash from schema
        String facultyHash = "$2a$12$BwWzsxRGHuxVTMf4pH2k7uJSAQ3an6QGza6WL6dPEGbqDZYCEQk1a";
        assertTrue(BCryptUtil.checkPassword("Faculty@123", facultyHash));
    }

    @Test
    public void testInvalidPasswordInputs() {
        assertFalse(BCryptUtil.checkPassword(null, "hash"));
        assertFalse(BCryptUtil.checkPassword("password", null));
        assertFalse(BCryptUtil.checkPassword("password", ""));
        assertThrows(IllegalArgumentException.class, () -> BCryptUtil.hashPassword(null));
        assertThrows(IllegalArgumentException.class, () -> BCryptUtil.hashPassword(""));
    }
}
