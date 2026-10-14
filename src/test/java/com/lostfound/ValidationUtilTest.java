package com.lostfound;

import com.lostfound.member1_auth.util.ValidationUtil;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ValidationUtilTest {

    @Test
    public void testEmailValidation() {
        assertTrue(ValidationUtil.isValidEmail("student@college.com"));
        assertTrue(ValidationUtil.isValidEmail("admin.test@domain.org"));
        assertFalse(ValidationUtil.isValidEmail("invalid-email"));
        assertFalse(ValidationUtil.isValidEmail("missing@domain"));
        assertFalse(ValidationUtil.isValidEmail(""));
        assertFalse(ValidationUtil.isValidEmail(null));
    }

    @Test
    public void testIdentifierValidation() {
        assertTrue(ValidationUtil.isValidIdentifier("student@college.com"));
        assertTrue(ValidationUtil.isValidIdentifier("aarav_sharma"));
        assertTrue(ValidationUtil.isValidIdentifier("user-123"));
        assertFalse(ValidationUtil.isValidIdentifier("ab")); // Too short (< 3)
        assertFalse(ValidationUtil.isValidIdentifier("user with spaces"));
        assertFalse(ValidationUtil.isValidIdentifier(null));
    }

    @Test
    public void testPasswordValidation() {
        assertTrue(ValidationUtil.isValidPassword("Admin@123"));
        assertTrue(ValidationUtil.isValidPassword("password1"));
        assertFalse(ValidationUtil.isValidPassword("short1")); // < 8 characters
        assertFalse(ValidationUtil.isValidPassword("lettersalllowercase")); // No digits/symbols
        assertFalse(ValidationUtil.isValidPassword("123456789")); // No letters
        assertFalse(ValidationUtil.isValidPassword(null));
    }

    @Test
    public void testPhoneValidation() {
        assertTrue(ValidationUtil.isValidPhone("+919876543210"));
        assertTrue(ValidationUtil.isValidPhone("9876543210"));
        assertTrue(ValidationUtil.isValidPhone(null)); // Optional field
        assertTrue(ValidationUtil.isValidPhone(""));   // Optional field
        assertFalse(ValidationUtil.isValidPhone("abc-not-phone"));
    }

    @Test
    public void testSanitize() {
        String unsafe = "<script>alert('xss');</script>";
        String safe = ValidationUtil.sanitize(unsafe);
        assertFalse(safe.contains("<script>"));
        assertTrue(safe.contains("&lt;script&gt;"));
    }
}
