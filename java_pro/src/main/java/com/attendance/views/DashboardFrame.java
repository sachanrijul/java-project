package com.attendance.views;

import com.attendance.controllers.AuthController;
import com.attendance.models.User;

import javax.swing.*;
import java.awt.*;

/**
 * Main dashboard frame after login
 * Provides navigation to different modules based on user role
 */
public class DashboardFrame extends JFrame {
    private User currentUser;
    private JLabel welcomeLabel;

    public DashboardFrame(User user) {
        this.currentUser = user;
        initializeUI();
    }

    private void initializeUI() {
        setTitle("Attendance System - Dashboard");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Create menu bar
        createMenuBar();

        // Main panel
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(245, 245, 250));

        // Welcome panel
        JPanel welcomePanel = new JPanel();
        welcomePanel.setBackground(new Color(70, 130, 180));
        welcomePanel.setPreferredSize(new Dimension(900, 100));
        welcomePanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        welcomeLabel = new JLabel("Welcome, " + currentUser.getFullName() + " (" + currentUser.getRole() + ")");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 24));
        welcomeLabel.setForeground(Color.WHITE);
        welcomePanel.add(welcomeLabel);

        // Content panel with buttons
        JPanel contentPanel = new JPanel(new GridBagLayout());
        contentPanel.setBackground(new Color(245, 245, 250));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;

        // Create buttons based on role
        if (currentUser.isAdmin()) {
            // Admin buttons
            gbc.gridx = 0;
            gbc.gridy = 0;
            contentPanel.add(
                    createDashboardButton("Manage Students", "icons/students.png", e -> openStudentManagement()), gbc);

            gbc.gridx = 1;
            contentPanel.add(
                    createDashboardButton("Manage Subjects", "icons/subjects.png", e -> openSubjectManagement()), gbc);

            gbc.gridx = 0;
            gbc.gridy = 1;
            contentPanel.add(createDashboardButton("View Reports", "icons/reports.png", e -> openReports()), gbc);

            gbc.gridx = 1;
            contentPanel.add(createDashboardButton("Manage Users", "icons/users.png", e -> showComingSoon()), gbc);
        } else {
            // Faculty buttons
            gbc.gridx = 0;
            gbc.gridy = 0;
            contentPanel.add(
                    createDashboardButton("Mark Attendance", "icons/attendance.png", e -> openAttendanceMarking()),
                    gbc);

            gbc.gridx = 1;
            contentPanel.add(createDashboardButton("View Reports", "icons/reports.png", e -> openReports()), gbc);

            gbc.gridx = 0;
            gbc.gridy = 1;
            contentPanel.add(createDashboardButton("My Subjects", "icons/subjects.png", e -> showMySubjects()), gbc);
        }

        mainPanel.add(welcomePanel, BorderLayout.NORTH);
        mainPanel.add(contentPanel, BorderLayout.CENTER);

        add(mainPanel);
    }

    private void createMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        // File menu
        JMenu fileMenu = new JMenu("File");

        if (currentUser.isAdmin()) {
            JMenuItem studentsItem = new JMenuItem("Manage Students");
            studentsItem.addActionListener(e -> openStudentManagement());
            fileMenu.add(studentsItem);

            JMenuItem subjectsItem = new JMenuItem("Manage Subjects");
            subjectsItem.addActionListener(e -> openSubjectManagement());
            fileMenu.add(subjectsItem);
        } else {
            JMenuItem attendanceItem = new JMenuItem("Mark Attendance");
            attendanceItem.addActionListener(e -> openAttendanceMarking());
            fileMenu.add(attendanceItem);
        }

        fileMenu.addSeparator();

        JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.addActionListener(e -> System.exit(0));
        fileMenu.add(exitItem);

        // Reports menu
        JMenu reportsMenu = new JMenu("Reports");
        JMenuItem viewReportsItem = new JMenuItem("View Reports");
        viewReportsItem.addActionListener(e -> openReports());
        reportsMenu.add(viewReportsItem);

        // Help menu
        JMenu helpMenu = new JMenu("Help");
        JMenuItem aboutItem = new JMenuItem("About");
        aboutItem.addActionListener(e -> showAbout());
        helpMenu.add(aboutItem);

        // Logout
        JMenu accountMenu = new JMenu("Account");
        JMenuItem logoutItem = new JMenuItem("Logout");
        logoutItem.addActionListener(e -> logout());
        accountMenu.add(logoutItem);

        menuBar.add(fileMenu);
        menuBar.add(reportsMenu);
        menuBar.add(helpMenu);
        menuBar.add(accountMenu);

        setJMenuBar(menuBar);
    }

    private JButton createDashboardButton(String text, String iconPath, java.awt.event.ActionListener listener) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setPreferredSize(new Dimension(250, 120));
        button.setBackground(Color.WHITE);
        button.setForeground(new Color(51, 51, 51));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 2),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.addActionListener(listener);

        // Hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(70, 130, 180));
                button.setForeground(Color.WHITE);
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(Color.WHITE);
                button.setForeground(new Color(51, 51, 51));
            }
        });

        return button;
    }

    private void openStudentManagement() {
        new StudentFrame().setVisible(true);
    }

    private void openSubjectManagement() {
        new SubjectFrame().setVisible(true);
    }

    private void openAttendanceMarking() {
        new AttendanceFrame(currentUser).setVisible(true);
    }

    private void openReports() {
        new ReportFrame(currentUser).setVisible(true);
    }

    private void showMySubjects() {
        JOptionPane.showMessageDialog(this,
                "My Subjects feature - Coming soon!",
                "Information",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private void showComingSoon() {
        JOptionPane.showMessageDialog(this,
                "This feature is coming soon!",
                "Information",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private void showAbout() {
        JOptionPane.showMessageDialog(this,
                "Student Attendance Management System\n" +
                        "Version 1.0\n\n" +
                        "A comprehensive system for managing student attendance,\n" +
                        "generating reports, and tracking attendance percentages.",
                "About",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private void logout() {
        int choice = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to logout?",
                "Confirm Logout",
                JOptionPane.YES_NO_OPTION);

        if (choice == JOptionPane.YES_OPTION) {
            AuthController authController = new AuthController();
            authController.logout();
            new LoginFrame().setVisible(true);
            dispose();
        }
    }
}
