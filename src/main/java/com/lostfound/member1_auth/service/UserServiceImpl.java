package com.lostfound.member1_auth.service;

import com.lostfound.member1_auth.dao.UserDAO;
import com.lostfound.member1_auth.dao.UserDAOImpl;
import com.lostfound.member1_auth.util.BCryptUtil;
import com.lostfound.member1_auth.util.ValidationUtil;
import com.lostfound.model.User;

/**
 * Implementation of UserService with robust business rules,
 * validations, and security checks.
 */
public class UserServiceImpl implements UserService {

    private final UserDAO userDAO;
    public UserServiceImpl() {
        this.userDAO = new UserDAOImpl();
    }

    public UserServiceImpl(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @Override
    public User register(User user, String confirmPassword) throws Exception {
        if (user == null) {
            throw new IllegalArgumentException("User details cannot be empty.");
        }

        // Validate name
        if (!ValidationUtil.isNonEmpty(user.getName()) || user.getName().trim().length() < 2) {
            throw new IllegalArgumentException("Full Name is required and must be at least 2 characters.");
        }
        user.setName(user.getName().trim());

        String identifier = user.getEmail() == null ? "" : user.getEmail().trim().toLowerCase(java.util.Locale.ROOT);
        if (!ValidationUtil.isValidIdentifier(identifier)) {
            throw new IllegalArgumentException("Username or email must be 3-150 characters and use only letters, numbers, '.', '_', '-', '+', or '@'.");
        }
        user.setEmail(identifier);

        // The unique users.email index is also the authoritative username uniqueness check.
        User existing = userDAO.findByEmail(user.getEmail());
        if (existing != null) {
            throw new IllegalArgumentException("An account with this username or email already exists.");
        }

        // Validate password
        if (!ValidationUtil.isValidPassword(user.getPassword())) {
            throw new IllegalArgumentException("Password must be at least 8 characters long and contain both letters and numbers/symbols.");
        }

        // Validate confirm password
        if (confirmPassword == null || !user.getPassword().equals(confirmPassword)) {
            throw new IllegalArgumentException("Password confirmation does not match.");
        }

        // Security rule: NEVER allow self-registration as ADMIN
        user.setRole("STUDENT");

        // Set default status
        user.setStatus("ACTIVE");

        // Hash password securely with BCrypt
        String hashedPassword = BCryptUtil.hashPassword(user.getPassword());
        user.setPassword(hashedPassword);

        // The unique database index is authoritative if concurrent registrations race.
        try {
            return userDAO.register(user);
        } catch (java.sql.SQLException e) {
            for (java.sql.SQLException cause = e; cause != null; cause = cause.getNextException()) {
                if ("23505".equals(cause.getSQLState()) || cause.getErrorCode() == 1062 || "23000".equals(cause.getSQLState())) {
                    throw new IllegalArgumentException("An account with this username or email already exists.");
                }
            }
            throw e;
        }
    }

    @Override
    public User login(String identifier, String password) throws Exception {
        if (!ValidationUtil.isNonEmpty(identifier) || !ValidationUtil.isNonEmpty(password)) {
            throw new IllegalArgumentException("Username or email and password are required.");
        }

        String normalizedIdentifier = identifier.trim().toLowerCase(java.util.Locale.ROOT);
        if (!ValidationUtil.isValidIdentifier(normalizedIdentifier)) {
            throw new IllegalArgumentException("Invalid username/email or password.");
        }
        User user = userDAO.findByEmail(normalizedIdentifier);
        if (user == null) {
            throw new IllegalArgumentException("Invalid username/email or password.");
        }

        // Check account status
        if (!user.isActive()) {
            throw new IllegalStateException("Your account has been deactivated or blocked by an administrator.");
        }

        // Verify BCrypt password
        boolean matches = BCryptUtil.checkPassword(password, user.getPassword());
        if (!matches) {
            throw new IllegalArgumentException("Invalid username/email or password.");
        }

        user.setPassword(null);
        return user;
    }

    @Override
    public User getProfile(Long userId) throws Exception {
        if (userId == null) {
            throw new IllegalArgumentException("User ID is required.");
        }
        User user = userDAO.findById(userId);
        if (user == null) {
            throw new IllegalArgumentException("User not found.");
        }
        return user;
    }

    @Override
    public boolean updateProfile(Long userId, String name, String phone, String department) throws Exception {
        User user = getProfile(userId);
        if (!ValidationUtil.isNonEmpty(name) || name.trim().length() < 2) {
            throw new IllegalArgumentException("Full Name must be at least 2 characters.");
        }
        if (!ValidationUtil.isValidPhone(phone)) {
            throw new IllegalArgumentException("Please enter a valid phone number.");
        }

        user.setName(name.trim());
        user.setPhone(phone != null ? phone.trim() : null);
        user.setDepartment(department != null ? department.trim() : null);

        return userDAO.updateProfile(user);
    }

    @Override
    public boolean changePassword(Long userId, String currentPassword, String newPassword, String confirmPassword) throws Exception {
        User user = getProfile(userId);

        if (!BCryptUtil.checkPassword(currentPassword, user.getPassword())) {
            throw new IllegalArgumentException("Current password is incorrect.");
        }

        if (!ValidationUtil.isValidPassword(newPassword)) {
            throw new IllegalArgumentException("New password must be at least 8 characters long and contain both letters and numbers/symbols.");
        }

        if (confirmPassword == null || !newPassword.equals(confirmPassword)) {
            throw new IllegalArgumentException("New password confirmation does not match.");
        }

        if (currentPassword.equals(newPassword)) {
            throw new IllegalArgumentException("New password cannot be the same as the current password.");
        }

        String hashedNewPassword = BCryptUtil.hashPassword(newPassword);
        return userDAO.updatePassword(userId, hashedNewPassword);
    }

}
