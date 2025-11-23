package com.attendance.views;

import com.attendance.controllers.ReportController;
import com.attendance.dao.StudentDAO;
import com.attendance.dao.SubjectDAO;
import com.attendance.models.Student;
import com.attendance.models.Subject;
import com.attendance.models.User;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

/**
 * Frame for viewing and exporting attendance reports
 */
public class ReportFrame extends JFrame {
    private User currentUser;
    private ReportController reportController;
    private StudentDAO studentDAO;
    private SubjectDAO subjectDAO;

    private JComboBox<String> reportTypeCombo;
    private JPanel filterPanel;
    private JComboBox<String> studentCombo;
    private JComboBox<String> subjectCombo;
    private JSpinner dateSpinner;
    private JTable reportTable;
    private DefaultTableModel tableModel;

    public ReportFrame(User user) {
        this.currentUser = user;
        reportController = new ReportController();
        studentDAO = new StudentDAO();
        subjectDAO = new SubjectDAO();
        initializeUI();
    }

    private void initializeUI() {
        setTitle("Attendance Reports");
        setSize(1000, 650);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Top panel with report type selection
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));

        topPanel.add(new JLabel("Report Type:"));
        reportTypeCombo = new JComboBox<>(
                new String[] { "By Student", "By Subject", "By Date", "Low Attendance Alert" });
        reportTypeCombo.setPreferredSize(new Dimension(200, 25));
        reportTypeCombo.addActionListener(e -> updateFilterPanel());
        topPanel.add(reportTypeCombo);

        // Filter panel (dynamic based on report type)
        filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        filterPanel.setBorder(BorderFactory.createTitledBorder("Filters"));

        JButton generateButton = new JButton("Generate Report");
        generateButton.setBackground(new Color(70, 130, 180));
        generateButton.setForeground(Color.WHITE);
        generateButton.addActionListener(e -> generateReport());
        topPanel.add(generateButton);

        // Table for report display
        tableModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        reportTable = new JTable(tableModel);
        reportTable.setRowHeight(25);
        reportTable.setAutoCreateRowSorter(true);
        reportTable.getTableHeader().setReorderingAllowed(false);

        JScrollPane scrollPane = new JScrollPane(reportTable);

        // Bottom panel with export buttons
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        JButton exportCSVButton = new JButton("Export to CSV");
        exportCSVButton.addActionListener(e -> exportToCSV());

        JButton exportPDFButton = new JButton("Export to PDF");
        exportPDFButton.addActionListener(e -> exportToPDF());

        bottomPanel.add(exportCSVButton);
        bottomPanel.add(exportPDFButton);

        // Combine top panel and filter panel
        JPanel northPanel = new JPanel(new BorderLayout());
        northPanel.add(topPanel, BorderLayout.NORTH);
        northPanel.add(filterPanel, BorderLayout.CENTER);

        mainPanel.add(northPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        add(mainPanel);

        // Initialize filter panel
        updateFilterPanel();
    }

    private void updateFilterPanel() {
        filterPanel.removeAll();

        String reportType = (String) reportTypeCombo.getSelectedItem();

        switch (reportType) {
            case "By Student":
                filterPanel.add(new JLabel("Select Student:"));
                studentCombo = new JComboBox<>();
                loadStudents();
                studentCombo.setPreferredSize(new Dimension(300, 25));
                filterPanel.add(studentCombo);
                break;

            case "By Subject":
                filterPanel.add(new JLabel("Select Subject:"));
                subjectCombo = new JComboBox<>();
                loadSubjects();
                subjectCombo.setPreferredSize(new Dimension(300, 25));
                filterPanel.add(subjectCombo);
                break;

            case "By Date":
                filterPanel.add(new JLabel("Select Subject:"));
                subjectCombo = new JComboBox<>();
                loadSubjects();
                subjectCombo.setPreferredSize(new Dimension(250, 25));
                filterPanel.add(subjectCombo);

                filterPanel.add(new JLabel("Date:"));
                SpinnerDateModel dateModel = new SpinnerDateModel();
                dateSpinner = new JSpinner(dateModel);
                JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(dateSpinner, "yyyy-MM-dd");
                dateSpinner.setEditor(dateEditor);
                dateSpinner.setPreferredSize(new Dimension(120, 25));
                filterPanel.add(dateSpinner);
                break;

            case "Low Attendance Alert":
                filterPanel.add(new JLabel("Threshold: < 75%"));
                break;
        }

        filterPanel.revalidate();
        filterPanel.repaint();
    }

    private void loadStudents() {
        studentCombo.removeAllItems();
        List<Student> students = studentDAO.getAllStudents();
        for (Student student : students) {
            studentCombo.addItem(
                    student.getStudentId() + " - " + student.getFullName() + " (" + student.getRollNumber() + ")");
        }
    }

    private void loadSubjects() {
        subjectCombo.removeAllItems();
        List<Subject> subjects = subjectDAO.getAllSubjects();
        for (Subject subject : subjects) {
            subjectCombo.addItem(subject.getSubjectId() + " - " + subject.getSubjectName());
        }
    }

    private void generateReport() {
        String reportType = (String) reportTypeCombo.getSelectedItem();

        tableModel.setRowCount(0);
        tableModel.setColumnCount(0);

        List<String[]> reportData = null;

        switch (reportType) {
            case "By Student":
                if (studentCombo.getSelectedItem() == null) {
                    JOptionPane.showMessageDialog(this, "Please select a student", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                String selectedStudent = (String) studentCombo.getSelectedItem();
                int studentId = Integer.parseInt(selectedStudent.split(" - ")[0]);

                tableModel.addColumn("Subject Code");
                tableModel.addColumn("Subject Name");
                tableModel.addColumn("Attendance %");
                tableModel.addColumn("Status");

                reportData = reportController.generateStudentReport(studentId);
                break;

            case "By Subject":
                if (subjectCombo.getSelectedItem() == null) {
                    JOptionPane.showMessageDialog(this, "Please select a subject", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                String selectedSubject = (String) subjectCombo.getSelectedItem();
                int subjectId = Integer.parseInt(selectedSubject.split(" - ")[0]);

                tableModel.addColumn("Roll Number");
                tableModel.addColumn("Student Name");
                tableModel.addColumn("Department");
                tableModel.addColumn("Attendance %");
                tableModel.addColumn("Status");

                reportData = reportController.generateSubjectReport(subjectId);
                break;

            case "By Date":
                if (subjectCombo.getSelectedItem() == null) {
                    JOptionPane.showMessageDialog(this, "Please select a subject", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                String selectedSubj = (String) subjectCombo.getSelectedItem();
                int subjId = Integer.parseInt(selectedSubj.split(" - ")[0]);

                java.util.Date utilDate = (java.util.Date) dateSpinner.getValue();
                Date sqlDate = new Date(utilDate.getTime());

                tableModel.addColumn("Student Name");
                tableModel.addColumn("Status");
                tableModel.addColumn("Marked By");
                tableModel.addColumn("Marked At");

                reportData = reportController.generateDateWiseReport(subjId, sqlDate);
                break;

            case "Low Attendance Alert":
                tableModel.addColumn("Roll Number");
                tableModel.addColumn("Student Name");
                tableModel.addColumn("Subject Code");
                tableModel.addColumn("Subject Name");
                tableModel.addColumn("Attendance %");

                reportData = reportController.getLowAttendanceReport(75.0);
                break;
        }

        if (reportData != null) {
            for (String[] row : reportData) {
                tableModel.addRow(row);
            }

            // Highlight low attendance rows
            highlightLowAttendance();

            if (reportData.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No data found for the selected criteria", "Information",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }

    private void highlightLowAttendance() {
        reportTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                    boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                // Check if there's a "Status" column
                int statusColumn = -1;
                for (int i = 0; i < table.getColumnCount(); i++) {
                    if (table.getColumnName(i).equals("Status")) {
                        statusColumn = i;
                        break;
                    }
                }

                if (statusColumn != -1) {
                    String status = (String) table.getValueAt(row, statusColumn);
                    if ("Low".equals(status)) {
                        c.setBackground(new Color(255, 200, 200));
                    } else if (!isSelected) {
                        c.setBackground(Color.WHITE);
                    }
                } else if (!isSelected) {
                    c.setBackground(Color.WHITE);
                }

                return c;
            }
        });
    }

    private void exportToCSV() {
        if (tableModel.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "No data to export. Please generate a report first.", "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save CSV File");
        fileChooser.setFileFilter(new FileNameExtensionFilter("CSV Files", "csv"));

        if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            String filename = fileChooser.getSelectedFile().getAbsolutePath();
            if (!filename.endsWith(".csv")) {
                filename += ".csv";
            }

            String[] headers = new String[tableModel.getColumnCount()];
            for (int i = 0; i < headers.length; i++) {
                headers[i] = tableModel.getColumnName(i);
            }

            List<String[]> data = new java.util.ArrayList<>();
            for (int i = 0; i < tableModel.getRowCount(); i++) {
                String[] row = new String[tableModel.getColumnCount()];
                for (int j = 0; j < row.length; j++) {
                    row[j] = tableModel.getValueAt(i, j).toString();
                }
                data.add(row);
            }

            if (reportController.exportToCSV(headers, data, filename)) {
                JOptionPane.showMessageDialog(this, "Report exported successfully to " + filename);
            } else {
                JOptionPane.showMessageDialog(this, "Failed to export report", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void exportToPDF() {
        if (tableModel.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "No data to export. Please generate a report first.", "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save PDF File");
        fileChooser.setFileFilter(new FileNameExtensionFilter("PDF Files", "pdf"));

        if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            String filename = fileChooser.getSelectedFile().getAbsolutePath();
            if (!filename.endsWith(".pdf")) {
                filename += ".pdf";
            }

            String[] headers = new String[tableModel.getColumnCount()];
            for (int i = 0; i < headers.length; i++) {
                headers[i] = tableModel.getColumnName(i);
            }

            List<String[]> data = new java.util.ArrayList<>();
            for (int i = 0; i < tableModel.getRowCount(); i++) {
                String[] row = new String[tableModel.getColumnCount()];
                for (int j = 0; j < row.length; j++) {
                    row[j] = tableModel.getValueAt(i, j).toString();
                }
                data.add(row);
            }

            String title = "Attendance Report - " + reportTypeCombo.getSelectedItem();

            if (reportController.exportToPDF(headers, data, filename, title)) {
                JOptionPane.showMessageDialog(this, "Report exported successfully to " + filename);
            } else {
                JOptionPane.showMessageDialog(this, "Failed to export report", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
