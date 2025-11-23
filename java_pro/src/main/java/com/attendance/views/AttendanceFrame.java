package com.attendance.views;

import com.attendance.controllers.AttendanceController;
import com.attendance.controllers.SubjectController;
import com.attendance.dao.StudentDAO;
import com.attendance.models.Attendance;
import com.attendance.models.Student;
import com.attendance.models.Subject;
import com.attendance.models.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Frame for marking attendance
 */
public class AttendanceFrame extends JFrame {
    private User currentUser;
    private AttendanceController attendanceController;
    private SubjectController subjectController;
    private StudentDAO studentDAO;

    private JComboBox<String> subjectCombo;
    private JTable attendanceTable;
    private DefaultTableModel tableModel;
    private JSpinner dateSpinner;

    public AttendanceFrame(User user) {
        this.currentUser = user;
        attendanceController = new AttendanceController();
        subjectController = new SubjectController();
        studentDAO = new StudentDAO();
        initializeUI();
    }

    private void initializeUI() {
        setTitle("Mark Attendance");
        setSize(900, 600);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Top panel with subject selection and date
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));

        topPanel.add(new JLabel("Subject:"));
        subjectCombo = new JComboBox<>();
        subjectCombo.setPreferredSize(new Dimension(300, 25));
        loadSubjects();
        subjectCombo.addActionListener(e -> loadStudentsForAttendance());
        topPanel.add(subjectCombo);

        topPanel.add(new JLabel("Date:"));
        SpinnerDateModel dateModel = new SpinnerDateModel();
        dateSpinner = new JSpinner(dateModel);
        JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(dateSpinner, "yyyy-MM-dd");
        dateSpinner.setEditor(dateEditor);
        dateSpinner.setPreferredSize(new Dimension(120, 25));
        topPanel.add(dateSpinner);

        JButton loadButton = new JButton("Load Attendance");
        loadButton.addActionListener(e -> loadExistingAttendance());
        topPanel.add(loadButton);

        // Table for attendance
        String[] columns = { "Student ID", "Roll Number", "Name", "Present", "Absent", "Late" };
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public Class<?> getColumnClass(int column) {
                return column >= 3 ? Boolean.class : String.class;
            }

            @Override
            public boolean isCellEditable(int row, int column) {
                return column >= 3; // Only radio button columns are editable
            }
        };
        attendanceTable = new JTable(tableModel);
        attendanceTable.setRowHeight(30);
        attendanceTable.getTableHeader().setReorderingAllowed(false);

        JScrollPane scrollPane = new JScrollPane(attendanceTable);

        // Bottom panel with buttons
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        JButton markAllPresentButton = new JButton("Mark All Present");
        markAllPresentButton.addActionListener(e -> markAllPresent());

        JButton submitButton = new JButton("Submit Attendance");
        submitButton.setBackground(new Color(70, 130, 180));
        submitButton.setForeground(Color.WHITE);
        submitButton.addActionListener(e -> submitAttendance());

        bottomPanel.add(markAllPresentButton);
        bottomPanel.add(submitButton);

        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private void loadSubjects() {
        subjectCombo.removeAllItems();
        List<Subject> subjects;

        if (currentUser.isAdmin()) {
            subjects = subjectController.getAllSubjects();
        } else {
            subjects = subjectController.getSubjectsByFaculty(currentUser.getUserId());
        }

        for (Subject subject : subjects) {
            subjectCombo.addItem(subject.getSubjectId() + " - " + subject.getSubjectName());
        }
    }

    private void loadStudentsForAttendance() {
        if (subjectCombo.getSelectedItem() == null) {
            return;
        }

        String selected = (String) subjectCombo.getSelectedItem();
        int subjectId = Integer.parseInt(selected.split(" - ")[0]);

        tableModel.setRowCount(0);
        List<Student> students = studentDAO.getStudentsBySubject(subjectId);

        for (Student student : students) {
            Object[] row = {
                    student.getStudentId(),
                    student.getRollNumber(),
                    student.getFullName(),
                    true, // Present by default
                    false,
                    false
            };
            tableModel.addRow(row);
        }
    }

    private void loadExistingAttendance() {
        if (subjectCombo.getSelectedItem() == null) {
            return;
        }

        String selected = (String) subjectCombo.getSelectedItem();
        int subjectId = Integer.parseInt(selected.split(" - ")[0]);

        java.util.Date utilDate = (java.util.Date) dateSpinner.getValue();
        Date sqlDate = new Date(utilDate.getTime());

        List<Attendance> existingAttendance = attendanceController.getAttendanceBySubjectAndDate(subjectId, sqlDate);

        if (existingAttendance.isEmpty()) {
            loadStudentsForAttendance();
            JOptionPane.showMessageDialog(this, "No attendance records found for this date. Showing all students.");
            return;
        }

        // Load students and mark their attendance
        loadStudentsForAttendance();

        for (int i = 0; i < tableModel.getRowCount(); i++) {
            int studentId = (Integer) tableModel.getValueAt(i, 0);

            for (Attendance att : existingAttendance) {
                if (att.getStudentId() == studentId) {
                    tableModel.setValueAt(att.getStatus().equals("Present"), i, 3);
                    tableModel.setValueAt(att.getStatus().equals("Absent"), i, 4);
                    tableModel.setValueAt(att.getStatus().equals("Late"), i, 5);
                    break;
                }
            }
        }

        JOptionPane.showMessageDialog(this, "Loaded existing attendance for " + sqlDate);
    }

    private void markAllPresent() {
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            tableModel.setValueAt(true, i, 3); // Present
            tableModel.setValueAt(false, i, 4); // Absent
            tableModel.setValueAt(false, i, 5); // Late
        }
    }

    private void submitAttendance() {
        if (subjectCombo.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Please select a subject", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String selected = (String) subjectCombo.getSelectedItem();
        int subjectId = Integer.parseInt(selected.split(" - ")[0]);

        java.util.Date utilDate = (java.util.Date) dateSpinner.getValue();
        Date sqlDate = new Date(utilDate.getTime());

        List<Attendance> attendanceList = new ArrayList<>();

        for (int i = 0; i < tableModel.getRowCount(); i++) {
            int studentId = (Integer) tableModel.getValueAt(i, 0);
            boolean present = (Boolean) tableModel.getValueAt(i, 3);
            boolean absent = (Boolean) tableModel.getValueAt(i, 4);
            boolean late = (Boolean) tableModel.getValueAt(i, 5);

            String status;
            if (present) {
                status = "Present";
            } else if (absent) {
                status = "Absent";
            } else if (late) {
                status = "Late";
            } else {
                status = "Absent"; // Default to absent if nothing selected
            }

            Attendance attendance = new Attendance(studentId, subjectId, sqlDate, status, currentUser.getUserId());
            attendanceList.add(attendance);
        }

        if (attendanceController.markBulkAttendance(attendanceList)) {
            JOptionPane.showMessageDialog(this, "Attendance marked successfully!");
        } else {
            JOptionPane.showMessageDialog(this, "Some attendance records failed to save", "Warning",
                    JOptionPane.WARNING_MESSAGE);
        }
    }
}
