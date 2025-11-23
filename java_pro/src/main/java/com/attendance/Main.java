package com.attendance;

import com.attendance.views.LoginFrame;

import javax.swing.*;

/**
 * Main entry point for the Student Attendance Management System
 * 
 * @author Student Attendance System Team
 * @version 1.0
 */
public class Main {

    public static void main(String[] args) {
        // Set system look and feel for better UI appearance
        try {
            // Try to use Nimbus look and feel
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            // If Nimbus is not available, use system default
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ex) {
                System.err.println("Error setting look and feel: " + ex.getMessage());
            }
        }

        // Launch the application on the Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            try {
                // Create and display the login frame
                LoginFrame loginFrame = new LoginFrame();
                loginFrame.setVisible(true);

                System.out.println("===========================================");
                System.out.println("Student Attendance Management System");
                System.out.println("Version 1.0");
                System.out.println("===========================================");
                System.out.println("Application started successfully!");
                System.out.println("Default credentials:");
                System.out.println("  Username: admin");
                System.out.println("  Password: admin123");
                System.out.println("===========================================");

            } catch (Exception e) {
                System.err.println("Error starting application: " + e.getMessage());
                e.printStackTrace();
                JOptionPane.showMessageDialog(null,
                        "Error starting application:\n" + e.getMessage(),
                        "Application Error",
                        JOptionPane.ERROR_MESSAGE);
                System.exit(1);
            }
        });
    }
}
