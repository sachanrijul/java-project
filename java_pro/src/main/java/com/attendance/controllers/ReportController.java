package com.attendance.controllers;

import com.attendance.dao.AttendanceDAO;
import com.attendance.dao.StudentDAO;
import com.attendance.dao.SubjectDAO;
import com.attendance.models.Attendance;
import com.attendance.models.Student;
import com.attendance.models.Subject;
import com.attendance.utils.ExportUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Controller for generating and exporting attendance reports
 */
public class ReportController {
    private AttendanceDAO attendanceDAO;
    private StudentDAO studentDAO;
    private SubjectDAO subjectDAO;

    public ReportController() {
        this.attendanceDAO = new AttendanceDAO();
        this.studentDAO = new StudentDAO();
        this.subjectDAO = new SubjectDAO();
    }

    /**
     * Generate attendance report for a student (all subjects)
     * 
     * @param studentId Student ID
     * @return List of attendance data with percentages
     */
    public List<String[]> generateStudentReport(int studentId) {
        List<String[]> reportData = new ArrayList<>();
        Student student = studentDAO.getStudentById(studentId);

        if (student == null) {
            return reportData;
        }

        // Get all subjects (we'll filter by enrollment in the query)
        List<Subject> allSubjects = subjectDAO.getAllSubjects();

        for (Subject subject : allSubjects) {
            double percentage = attendanceDAO.calculateAttendancePercentage(studentId, subject.getSubjectId());
            if (percentage > 0) { // Only include subjects where student has attendance
                String[] row = {
                        subject.getSubjectCode(),
                        subject.getSubjectName(),
                        String.format("%.2f%%", percentage),
                        percentage < 75.0 ? "Low" : "Good"
                };
                reportData.add(row);
            }
        }

        return reportData;
    }

    /**
     * Generate attendance report for a subject (all students)
     * 
     * @param subjectId Subject ID
     * @return List of attendance data with percentages
     */
    public List<String[]> generateSubjectReport(int subjectId) {
        List<String[]> reportData = new ArrayList<>();
        List<Student> students = studentDAO.getStudentsBySubject(subjectId);

        for (Student student : students) {
            double percentage = attendanceDAO.calculateAttendancePercentage(student.getStudentId(), subjectId);
            String[] row = {
                    student.getRollNumber(),
                    student.getFullName(),
                    student.getDepartment(),
                    String.format("%.2f%%", percentage),
                    percentage < 75.0 ? "Low" : "Good"
            };
            reportData.add(row);
        }

        return reportData;
    }

    /**
     * Generate date-wise attendance report
     * 
     * @param subjectId Subject ID
     * @param date      Date
     * @return List of attendance data
     */
    public List<String[]> generateDateWiseReport(int subjectId, java.sql.Date date) {
        List<String[]> reportData = new ArrayList<>();
        List<Attendance> attendanceList = attendanceDAO.getAttendanceBySubjectAndDate(subjectId, date);

        for (Attendance attendance : attendanceList) {
            String[] row = {
                    attendance.getStudentName(),
                    attendance.getStatus(),
                    attendance.getMarkedByName(),
                    attendance.getMarkedAt().toString()
            };
            reportData.add(row);
        }

        return reportData;
    }

    /**
     * Export report to CSV
     * 
     * @param headers  Column headers
     * @param data     Report data
     * @param filename Output filename
     * @return true if successful, false otherwise
     */
    public boolean exportToCSV(String[] headers, List<String[]> data, String filename) {
        return ExportUtil.exportToCSV(headers, data, filename);
    }

    /**
     * Export report to PDF
     * 
     * @param headers  Column headers
     * @param data     Report data
     * @param filename Output filename
     * @param title    Report title
     * @return true if successful, false otherwise
     */
    public boolean exportToPDF(String[] headers, List<String[]> data, String filename, String title) {
        return ExportUtil.exportToPDF(headers, data, filename, title);
    }

    /**
     * Get low attendance alert report
     * 
     * @param threshold Threshold percentage
     * @return List of students with low attendance across all subjects
     */
    public List<String[]> getLowAttendanceReport(double threshold) {
        List<String[]> reportData = new ArrayList<>();
        List<Student> allStudents = studentDAO.getAllStudents();
        List<Subject> allSubjects = subjectDAO.getAllSubjects();

        for (Student student : allStudents) {
            for (Subject subject : allSubjects) {
                double percentage = attendanceDAO.calculateAttendancePercentage(student.getStudentId(),
                        subject.getSubjectId());
                if (percentage > 0 && percentage < threshold) {
                    String[] row = {
                            student.getRollNumber(),
                            student.getFullName(),
                            subject.getSubjectCode(),
                            subject.getSubjectName(),
                            String.format("%.2f%%", percentage)
                    };
                    reportData.add(row);
                }
            }
        }

        return reportData;
    }
}
