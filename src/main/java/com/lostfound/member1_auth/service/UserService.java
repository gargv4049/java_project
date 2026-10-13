package com.lostfound.member1_auth.service;

import com.lostfound.model.User;

/**
 * User Service Interface.
 * Handles business logic for authentication, registration, profile, and security.
 */
public interface UserService {

    /**
     * Registers a new student or faculty user.
     * Validates input, verifies uniqueness of email, hashes password with BCrypt, saves to DB.
     */
    User register(User user, String confirmPassword) throws Exception;

    /**
     * Authenticates a user using email and password.
     * Checks account status (must be ACTIVE) and verifies BCrypt hash.
     */
    User login(String email, String password) throws Exception;

    /**
     * Retrieves user profile details by ID.
     */
    User getProfile(Long userId) throws Exception;

    /**
     * Updates editable profile fields (name, phone, department).
     */
    boolean updateProfile(Long userId, String name, String phone, String department) throws Exception;

    /**
     * Changes password after verifying the current password and new password complexity.
     */
    boolean changePassword(Long userId, String currentPassword, String newPassword, String confirmPassword) throws Exception;

}
