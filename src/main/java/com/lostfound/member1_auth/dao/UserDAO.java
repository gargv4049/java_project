package com.lostfound.member1_auth.dao;

import com.lostfound.model.User;
import java.sql.SQLException;
import java.util.List;

/**
 * User Data Access Object Interface.
 * Handles database operations for User entity.
 */
public interface UserDAO {

    /**
     * Registers a new user into the database.
     * @return User with generated userId.
     */
    User register(User user) throws SQLException;

    /** Finds an account by its unique, normalized login identifier. */
    User findByEmail(String email) throws SQLException;

    /**
     * Finds a user by their primary key userId.
     */
    User findById(Long userId) throws SQLException;

    /**
     * Updates profile details (name, phone, department, studentId).
     */
    boolean updateProfile(User user) throws SQLException;

    /**
     * Updates user's hashed password.
     */
    boolean updatePassword(Long userId, String newHashedPassword) throws SQLException;

    /**
     * Blocks a user (status = 'BLOCKED').
     */
    boolean blockUser(Long userId) throws SQLException;

    /**
     * Unblocks a user (status = 'ACTIVE').
     */
    boolean unblockUser(Long userId) throws SQLException;

    /**
     * Updates user role (STUDENT, FACULTY, ADMIN).
     */
    boolean updateRole(Long userId, String newRole) throws SQLException;

    /**
     * Lists all users ordered by creation date descending.
     */
    List<User> findAll() throws SQLException;

    /**
     * Searches users by name, email, or department.
     */
    List<User> searchUsers(String keyword) throws SQLException;

    /**
     * Counts total number of users.
     */
    int countTotalUsers() throws SQLException;

    /**
     * Counts active users.
     */
    int countActiveUsers() throws SQLException;

    /**
     * Counts blocked users.
     */
    int countBlockedUsers() throws SQLException;

    /**
     * Counts users with role ADMIN (to prevent removing the last admin).
     */
    int countAdmins() throws SQLException;
}
