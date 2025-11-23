package com.attendance.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Singleton class for managing database connections
 * Provides a single connection instance throughout the application
 */
public class DatabaseConnection {
    private static DatabaseConnection instance;
    private Connection connection;

    // Database configuration
    private static final String URL = "jdbc:mysql://localhost:3306/attendance_db";
    private static final String USER = "root";
    private static final String PASSWORD = "abcd1234";

    /**
     * Private constructor to prevent instantiation
     * Establishes connection to MySQL database
     */
    private DatabaseConnection() throws SQLException {
        try {
            // Load MySQL JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");
            this.connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Database connection established successfully.");
        } catch (ClassNotFoundException e) {
            throw new SQLException("MySQL JDBC Driver not found.", e);
        }
    }

    /**
     * Get singleton instance of DatabaseConnection
     * 
     * @return DatabaseConnection instance
     * @throws SQLException if connection fails
     */
    public static DatabaseConnection getInstance() throws SQLException {
        if (instance == null || instance.getConnection().isClosed()) {
            instance = new DatabaseConnection();
        }
        return instance;
    }

    /**
     * Get the database connection
     * 
     * @return Connection object
     */
    public Connection getConnection() {
        return connection;
    }

    /**
     * Close the database connection
     */
    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Database connection closed.");
            }
        } catch (SQLException e) {
            System.err.println("Error closing database connection: " + e.getMessage());
        }
    }
}
