package com.attendance.views;

import com.attendance.controllers.StudentController;
import com.attendance.models.Student;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Frame for managing students (CRUD operations)
 */
public class StudentFrame extends JFrame {
    private StudentController studentController;
    private JTable studentTable;
    private DefaultTableModel tableModel;
    private JTextField searchField;

    public StudentFrame() {
        studentController = new StudentController();
        initializeUI();
        loadStudents();
    }

    private void initializeUI() {
        setTitle("Student Management");
        setSize(1000, 600);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Top panel with search and buttons
        JPanel topPanel = new JPanel(new BorderLayout(10, 10));

        // Search panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.add(new JLabel("Search:"));
        searchField = new JTextField(20);
        searchPanel.add(searchField);
        JButton searchButton = new JButton("Search");
        searchButton.addActionListener(e -> searchStudents());
        searchPanel.add(searchButton);
        JButton refreshButton = new JButton("Refresh");
        refreshButton.addActionListener(e -> loadStudents());
        searchPanel.add(refreshButton);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton addButton = new JButton("Add Student");
        addButton.setBackground(new Color(70, 130, 180));
        addButton.setForeground(Color.WHITE);
        addButton.addActionListener(e -> showAddStudentDialog());

        JButton editButton = new JButton("Edit Student");
        editButton.addActionListener(e -> showEditStudentDialog());

        JButton deleteButton = new JButton("Delete Student");
        deleteButton.setBackground(new Color(220, 53, 69));
        deleteButton.setForeground(Color.WHITE);
        deleteButton.addActionListener(e -> deleteStudent());

        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);

        topPanel.add(searchPanel, BorderLayout.WEST);
        topPanel.add(buttonPanel, BorderLayout.EAST);

        // Table
        String[] columns = { "ID", "Roll Number", "First Name", "Last Name", "Email", "Phone", "Department",
                "Semester" };
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        studentTable = new JTable(tableModel);
        studentTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        studentTable.setRowHeight(25);
        studentTable.getTableHeader().setReorderingAllowed(false);

        JScrollPane scrollPane = new JScrollPane(studentTable);

        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        add(mainPanel);
    }

    private void loadStudents() {
        tableModel.setRowCount(0);
        List<Student> students = studentController.getAllStudents();
        for (Student student : students) {
            Object[] row = {
                    student.getStudentId(),
                    student.getRollNumber(),
                    student.getFirstName(),
                    student.getLastName(),
                    student.getEmail(),
                    student.getPhone(),
                    student.getDepartment(),
                    student.getSemester()
            };
            tableModel.addRow(row);
        }
    }

    private void searchStudents() {
        String query = searchField.getText().trim();
        if (query.isEmpty()) {
            loadStudents();
            return;
        }

        tableModel.setRowCount(0);
        List<Student> students = studentController.searchStudents(query);
        for (Student student : students) {
            Object[] row = {
                    student.getStudentId(),
                    student.getRollNumber(),
                    student.getFirstName(),
                    student.getLastName(),
                    student.getEmail(),
                    student.getPhone(),
                    student.getDepartment(),
                    student.getSemester()
            };
            tableModel.addRow(row);
        }
    }

    private void showAddStudentDialog() {
        JDialog dialog = new JDialog(this, "Add Student", true);
        dialog.setSize(400, 400);
        dialog.setLocationRelativeTo(this);

        JPanel panel = new JPanel(new GridLayout(8, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JTextField rollField = new JTextField();
        JTextField firstNameField = new JTextField();
        JTextField lastNameField = new JTextField();
        JTextField emailField = new JTextField();
        JTextField phoneField = new JTextField();
        JTextField deptField = new JTextField();
        JSpinner semesterSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 8, 1));

        panel.add(new JLabel("Roll Number:"));
        panel.add(rollField);
        panel.add(new JLabel("First Name:"));
        panel.add(firstNameField);
        panel.add(new JLabel("Last Name:"));
        panel.add(lastNameField);
        panel.add(new JLabel("Email:"));
        panel.add(emailField);
        panel.add(new JLabel("Phone:"));
        panel.add(phoneField);
        panel.add(new JLabel("Department:"));
        panel.add(deptField);
        panel.add(new JLabel("Semester:"));
        panel.add(semesterSpinner);

        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(e -> {
            try {
                Student student = new Student(
                        rollField.getText().trim(),
                        firstNameField.getText().trim(),
                        lastNameField.getText().trim(),
                        emailField.getText().trim(),
                        phoneField.getText().trim(),
                        deptField.getText().trim(),
                        (Integer) semesterSpinner.getValue());

                if (studentController.addStudent(student)) {
                    JOptionPane.showMessageDialog(dialog, "Student added successfully!");
                    loadStudents();
                    dialog.dispose();
                } else {
                    JOptionPane.showMessageDialog(dialog, "Failed to add student!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(dialog, ex.getMessage(), "Validation Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        panel.add(new JLabel());
        panel.add(saveButton);

        dialog.add(panel);
        dialog.setVisible(true);
    }

    private void showEditStudentDialog() {
        int selectedRow = studentTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a student to edit", "No Selection",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        int studentId = (Integer) tableModel.getValueAt(selectedRow, 0);
        Student student = studentController.getStudentById(studentId);

        if (student == null) {
            JOptionPane.showMessageDialog(this, "Student not found!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        JDialog dialog = new JDialog(this, "Edit Student", true);
        dialog.setSize(400, 400);
        dialog.setLocationRelativeTo(this);

        JPanel panel = new JPanel(new GridLayout(8, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JTextField rollField = new JTextField(student.getRollNumber());
        JTextField firstNameField = new JTextField(student.getFirstName());
        JTextField lastNameField = new JTextField(student.getLastName());
        JTextField emailField = new JTextField(student.getEmail());
        JTextField phoneField = new JTextField(student.getPhone());
        JTextField deptField = new JTextField(student.getDepartment());
        JSpinner semesterSpinner = new JSpinner(new SpinnerNumberModel(student.getSemester(), 1, 8, 1));

        panel.add(new JLabel("Roll Number:"));
        panel.add(rollField);
        panel.add(new JLabel("First Name:"));
        panel.add(firstNameField);
        panel.add(new JLabel("Last Name:"));
        panel.add(lastNameField);
        panel.add(new JLabel("Email:"));
        panel.add(emailField);
        panel.add(new JLabel("Phone:"));
        panel.add(phoneField);
        panel.add(new JLabel("Department:"));
        panel.add(deptField);
        panel.add(new JLabel("Semester:"));
        panel.add(semesterSpinner);

        JButton saveButton = new JButton("Update");
        saveButton.addActionListener(e -> {
            try {
                student.setRollNumber(rollField.getText().trim());
                student.setFirstName(firstNameField.getText().trim());
                student.setLastName(lastNameField.getText().trim());
                student.setEmail(emailField.getText().trim());
                student.setPhone(phoneField.getText().trim());
                student.setDepartment(deptField.getText().trim());
                student.setSemester((Integer) semesterSpinner.getValue());

                if (studentController.updateStudent(student)) {
                    JOptionPane.showMessageDialog(dialog, "Student updated successfully!");
                    loadStudents();
                    dialog.dispose();
                } else {
                    JOptionPane.showMessageDialog(dialog, "Failed to update student!", "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(dialog, ex.getMessage(), "Validation Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        panel.add(new JLabel());
        panel.add(saveButton);

        dialog.add(panel);
        dialog.setVisible(true);
    }

    private void deleteStudent() {
        int selectedRow = studentTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a student to delete", "No Selection",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        int choice = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete this student?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION);

        if (choice == JOptionPane.YES_OPTION) {
            int studentId = (Integer) tableModel.getValueAt(selectedRow, 0);
            if (studentController.deleteStudent(studentId)) {
                JOptionPane.showMessageDialog(this, "Student deleted successfully!");
                loadStudents();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to delete student!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
