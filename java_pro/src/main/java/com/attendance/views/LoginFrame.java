package com.attendance.views;

import com.attendance.controllers.AuthController;
import com.attendance.models.User;

import javax.swing.*;
import java.awt.*;

/**
 * Login frame for user authentication
 */
public class LoginFrame extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private AuthController authController;

    public LoginFrame() {
        authController = new AuthController();
        initializeUI();
    }

    private void initializeUI() {
        setTitle("Student Attendance Management System - Login");
        setSize(450, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // Main panel with padding
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        mainPanel.setBackground(new Color(240, 240, 245));

        // Title panel
        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(new Color(240, 240, 245));
        JLabel titleLabel = new JLabel("Attendance Management System");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(new Color(51, 51, 51));
        titlePanel.add(titleLabel);

        // Form panel
        JPanel formPanel = new JPanel(new GridLayout(3, 2, 10, 15));
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)));

        // Username
        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        usernameField = new JTextField();
        usernameField.setFont(new Font("Arial", Font.PLAIN, 14));

        // Password
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        passwordField = new JPasswordField();
        passwordField.setFont(new Font("Arial", Font.PLAIN, 14));

        // Login button
        loginButton = new JButton("Login");
        loginButton.setFont(new Font("Arial", Font.BOLD, 14));
        loginButton.setBackground(new Color(70, 130, 180));
        loginButton.setForeground(Color.WHITE);
        loginButton.setFocusPainted(false);
        loginButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        formPanel.add(usernameLabel);
        formPanel.add(usernameField);
        formPanel.add(passwordLabel);
        formPanel.add(passwordField);
        formPanel.add(new JLabel()); // Empty cell
        formPanel.add(loginButton);

        // Info panel
        JPanel infoPanel = new JPanel();
        infoPanel.setBackground(new Color(240, 240, 245));
        JLabel infoLabel = new JLabel("Default: admin / admin123");
        infoLabel.setFont(new Font("Arial", Font.ITALIC, 11));
        infoLabel.setForeground(Color.GRAY);
        infoPanel.add(infoLabel);

        // Add panels to main panel
        mainPanel.add(titlePanel, BorderLayout.NORTH);
        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(infoPanel, BorderLayout.SOUTH);

        add(mainPanel);

        // Event listeners
        loginButton.addActionListener(e -> handleLogin());
        passwordField.addActionListener(e -> handleLogin()); // Enter key in password field

        // Set focus to username field
        SwingUtilities.invokeLater(() -> usernameField.requestFocus());
    }

    private void handleLogin() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please enter both username and password",
                    "Validation Error",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Attempt login
        User user = authController.login(username, password);

        if (user != null) {
            JOptionPane.showMessageDialog(this,
                    "Welcome, " + user.getFullName() + "!",
                    "Login Successful",
                    JOptionPane.INFORMATION_MESSAGE);

            // Open dashboard
            new DashboardFrame(user).setVisible(true);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this,
                    "Invalid username or password",
                    "Login Failed",
                    JOptionPane.ERROR_MESSAGE);
            passwordField.setText("");
            passwordField.requestFocus();
        }
    }

    public static void main(String[] args) {
        // Set look and feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            new LoginFrame().setVisible(true);
        });
    }
}
