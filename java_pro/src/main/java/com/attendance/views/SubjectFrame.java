package com.attendance.views;

import com.attendance.controllers.SubjectController;
import com.attendance.dao.UserDAO;
import com.attendance.models.Subject;
import com.attendance.models.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Frame for managing subjects (CRUD operations)
 */
public class SubjectFrame extends JFrame {
    private SubjectController subjectController;
    private UserDAO userDAO;
    private JTable subjectTable;
    private DefaultTableModel tableModel;

    public SubjectFrame() {
        subjectController = new SubjectController();
        userDAO = new UserDAO();
        initializeUI();
        loadSubjects();
    }

    private void initializeUI() {
        setTitle("Subject Management");
        setSize(1000, 600);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Top panel with buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        JButton addButton = new JButton("Add Subject");
        addButton.setBackground(new Color(70, 130, 180));
        addButton.setForeground(Color.WHITE);
        addButton.addActionListener(e -> showAddSubjectDialog());

        JButton editButton = new JButton("Edit Subject");
        editButton.addActionListener(e -> showEditSubjectDialog());

        JButton deleteButton = new JButton("Delete Subject");
        deleteButton.setBackground(new Color(220, 53, 69));
        deleteButton.setForeground(Color.WHITE);
        deleteButton.addActionListener(e -> deleteSubject());

        JButton refreshButton = new JButton("Refresh");
        refreshButton.addActionListener(e -> loadSubjects());

        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(refreshButton);

        // Table
        String[] columns = { "ID", "Subject Code", "Subject Name", "Department", "Semester", "Faculty" };
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        subjectTable = new JTable(tableModel);
        subjectTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        subjectTable.setRowHeight(25);
        subjectTable.getTableHeader().setReorderingAllowed(false);

        JScrollPane scrollPane = new JScrollPane(subjectTable);

        mainPanel.add(buttonPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        add(mainPanel);
    }

    private void loadSubjects() {
        tableModel.setRowCount(0);
        List<Subject> subjects = subjectController.getAllSubjects();
        for (Subject subject : subjects) {
            Object[] row = {
                    subject.getSubjectId(),
                    subject.getSubjectCode(),
                    subject.getSubjectName(),
                    subject.getDepartment(),
                    subject.getSemester(),
                    subject.getFacultyName() != null ? subject.getFacultyName() : "Not Assigned"
            };
            tableModel.addRow(row);
        }
    }

    private void showAddSubjectDialog() {
        JDialog dialog = new JDialog(this, "Add Subject", true);
        dialog.setSize(400, 350);
        dialog.setLocationRelativeTo(this);

        JPanel panel = new JPanel(new GridLayout(6, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JTextField codeField = new JTextField();
        JTextField nameField = new JTextField();
        JTextField deptField = new JTextField();
        JSpinner semesterSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 8, 1));

        // Faculty dropdown
        JComboBox<String> facultyCombo = new JComboBox<>();
        List<User> facultyList = userDAO.getAllFaculty();
        for (User faculty : facultyList) {
            facultyCombo.addItem(faculty.getUserId() + " - " + faculty.getFullName());
        }

        panel.add(new JLabel("Subject Code:"));
        panel.add(codeField);
        panel.add(new JLabel("Subject Name:"));
        panel.add(nameField);
        panel.add(new JLabel("Department:"));
        panel.add(deptField);
        panel.add(new JLabel("Semester:"));
        panel.add(semesterSpinner);
        panel.add(new JLabel("Faculty:"));
        panel.add(facultyCombo);

        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(e -> {
            try {
                String selectedFaculty = (String) facultyCombo.getSelectedItem();
                int facultyId = Integer.parseInt(selectedFaculty.split(" - ")[0]);

                Subject subject = new Subject(
                        codeField.getText().trim(),
                        nameField.getText().trim(),
                        deptField.getText().trim(),
                        (Integer) semesterSpinner.getValue(),
                        facultyId);

                if (subjectController.addSubject(subject)) {
                    JOptionPane.showMessageDialog(dialog, "Subject added successfully!");
                    loadSubjects();
                    dialog.dispose();
                } else {
                    JOptionPane.showMessageDialog(dialog, "Failed to add subject!", "Error", JOptionPane.ERROR_MESSAGE);
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

    private void showEditSubjectDialog() {
        int selectedRow = subjectTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a subject to edit", "No Selection",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        int subjectId = (Integer) tableModel.getValueAt(selectedRow, 0);
        Subject subject = subjectController.getSubjectById(subjectId);

        if (subject == null) {
            JOptionPane.showMessageDialog(this, "Subject not found!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        JDialog dialog = new JDialog(this, "Edit Subject", true);
        dialog.setSize(400, 350);
        dialog.setLocationRelativeTo(this);

        JPanel panel = new JPanel(new GridLayout(6, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JTextField codeField = new JTextField(subject.getSubjectCode());
        JTextField nameField = new JTextField(subject.getSubjectName());
        JTextField deptField = new JTextField(subject.getDepartment());
        JSpinner semesterSpinner = new JSpinner(new SpinnerNumberModel(subject.getSemester(), 1, 8, 1));

        JComboBox<String> facultyCombo = new JComboBox<>();
        List<User> facultyList = userDAO.getAllFaculty();
        int selectedIndex = 0;
        for (int i = 0; i < facultyList.size(); i++) {
            User faculty = facultyList.get(i);
            facultyCombo.addItem(faculty.getUserId() + " - " + faculty.getFullName());
            if (faculty.getUserId() == subject.getFacultyId()) {
                selectedIndex = i;
            }
        }
        facultyCombo.setSelectedIndex(selectedIndex);

        panel.add(new JLabel("Subject Code:"));
        panel.add(codeField);
        panel.add(new JLabel("Subject Name:"));
        panel.add(nameField);
        panel.add(new JLabel("Department:"));
        panel.add(deptField);
        panel.add(new JLabel("Semester:"));
        panel.add(semesterSpinner);
        panel.add(new JLabel("Faculty:"));
        panel.add(facultyCombo);

        JButton saveButton = new JButton("Update");
        saveButton.addActionListener(e -> {
            try {
                String selectedFaculty = (String) facultyCombo.getSelectedItem();
                int facultyId = Integer.parseInt(selectedFaculty.split(" - ")[0]);

                subject.setSubjectCode(codeField.getText().trim());
                subject.setSubjectName(nameField.getText().trim());
                subject.setDepartment(deptField.getText().trim());
                subject.setSemester((Integer) semesterSpinner.getValue());
                subject.setFacultyId(facultyId);

                if (subjectController.updateSubject(subject)) {
                    JOptionPane.showMessageDialog(dialog, "Subject updated successfully!");
                    loadSubjects();
                    dialog.dispose();
                } else {
                    JOptionPane.showMessageDialog(dialog, "Failed to update subject!", "Error",
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

    private void deleteSubject() {
        int selectedRow = subjectTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a subject to delete", "No Selection",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        int choice = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete this subject?\nThis will also delete all related attendance records.",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (choice == JOptionPane.YES_OPTION) {
            int subjectId = (Integer) tableModel.getValueAt(selectedRow, 0);
            if (subjectController.deleteSubject(subjectId)) {
                JOptionPane.showMessageDialog(this, "Subject deleted successfully!");
                loadSubjects();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to delete subject!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
