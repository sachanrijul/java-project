package com.attendance.models;

import java.sql.Timestamp;

/**
 * User model representing admin and faculty users in the system
 * Supports role-based access control
 */
public class User {
    private int userId;
    private String username;
    private String passwordHash;
    private String role; // "Admin" or "Faculty"
    private String fullName;
    private String email;
    private Timestamp createdAt;

    // Default constructor
    public User() {
    }

    // Constructor without userId (for new users)
    public User(String username, String passwordHash, String role, String fullName, String email) {
        this.username = username;
        this.passwordHash = passwordHash;
        this.role = role;
        this.fullName = fullName;
        this.email = email;
    }

    // Full constructor
    public User(int userId, String username, String passwordHash, String role, 
                String fullName, String email, Timestamp createdAt) {
        this.userId = userId;
        this.username = username;
        this.passwordHash = passwordHash;
        this.role = role;
        this.fullName = fullName;
        this.email = email;
        this.createdAt = createdAt;
    }

    // Getters and Setters
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    // Helper methods
    public boolean isAdmin() {
        return "Admin".equals(role);
    }

    public boolean isFaculty() {
        return "Faculty".equals(role);
    }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", username='" + username + '\'' +
                ", role='" + role + '\'' +
                ", fullName='" + fullName + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
