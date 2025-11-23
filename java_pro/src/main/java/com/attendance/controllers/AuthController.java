package com.attendance.controllers;

import com.attendance.dao.UserDAO;
import com.attendance.models.User;
import com.attendance.utils.PasswordUtil;

/**
 * Controller for authentication operations
 * Manages user login, logout, and session
 */
public class AuthController {
    private UserDAO userDAO;
    private static User currentUser; // Current logged-in user

    public AuthController() {
        this.userDAO = new UserDAO();
    }

    /**
     * Authenticate user with username and password
     * 
     * @param username Username
     * @param password Plain text password
     * @return User object if authentication successful, null otherwise
     */
    public User login(String username, String password) {
        // Hash the password
        String passwordHash = PasswordUtil.hashPassword(password);

        // Validate user credentials
        User user = userDAO.validateUser(username, passwordHash);

        if (user != null) {
            currentUser = user;
            System.out.println("User logged in: " + user.getFullName() + " (" + user.getRole() + ")");
        }

        return user;
    }

    /**
     * Logout current user
     */
    public void logout() {
        if (currentUser != null) {
            System.out.println("User logged out: " + currentUser.getFullName());
            currentUser = null;
        }
    }

    /**
     * Get current logged-in user
     * 
     * @return Current user or null if not logged in
     */
    public static User getCurrentUser() {
        return currentUser;
    }

    /**
     * Check if a user is currently logged in
     * 
     * @return true if user is logged in, false otherwise
     */
    public static boolean isLoggedIn() {
        return currentUser != null;
    }

    /**
     * Check if current user is admin
     * 
     * @return true if current user is admin, false otherwise
     */
    public static boolean isAdmin() {
        return currentUser != null && currentUser.isAdmin();
    }

    /**
     * Check if current user is faculty
     * 
     * @return true if current user is faculty, false otherwise
     */
    public static boolean isFaculty() {
        return currentUser != null && currentUser.isFaculty();
    }
}
