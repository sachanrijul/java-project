package com.attendance.dao;

import com.attendance.models.Subject;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for Subject operations
 */
public class SubjectDAO {

    /**
     * Add a new subject
     * 
     * @param subject Subject object
     * @return true if successful, false otherwise
     */
    public boolean addSubject(Subject subject) {
        String sql = "INSERT INTO subjects (subject_code, subject_name, department, semester, faculty_id) " +
                "VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, subject.getSubjectCode());
            stmt.setString(2, subject.getSubjectName());
            stmt.setString(3, subject.getDepartment());
            stmt.setInt(4, subject.getSemester());
            stmt.setInt(5, subject.getFacultyId());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error adding subject: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Get all subjects
     * 
     * @return List of all subjects
     */
    public List<Subject> getAllSubjects() {
        List<Subject> subjects = new ArrayList<>();
        String sql = "SELECT s.*, u.full_name as faculty_name FROM subjects s " +
                "LEFT JOIN users u ON s.faculty_id = u.user_id " +
                "ORDER BY s.subject_code";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                subjects.add(extractSubjectFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error getting all subjects: " + e.getMessage());
            e.printStackTrace();
        }
        return subjects;
    }

    /**
     * Get subject by ID
     * 
     * @param subjectId Subject ID
     * @return Subject object if found, null otherwise
     */
    public Subject getSubjectById(int subjectId) {
        String sql = "SELECT s.*, u.full_name as faculty_name FROM subjects s " +
                "LEFT JOIN users u ON s.faculty_id = u.user_id " +
                "WHERE s.subject_id = ?";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, subjectId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return extractSubjectFromResultSet(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error getting subject by ID: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Get subjects assigned to a specific faculty
     * 
     * @param facultyId Faculty ID
     * @return List of subjects assigned to the faculty
     */
    public List<Subject> getSubjectsByFaculty(int facultyId) {
        List<Subject> subjects = new ArrayList<>();
        String sql = "SELECT s.*, u.full_name as faculty_name FROM subjects s " +
                "LEFT JOIN users u ON s.faculty_id = u.user_id " +
                "WHERE s.faculty_id = ? ORDER BY s.subject_code";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, facultyId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                subjects.add(extractSubjectFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error getting subjects by faculty: " + e.getMessage());
            e.printStackTrace();
        }
        return subjects;
    }

    /**
     * Update subject information
     * 
     * @param subject Subject object with updated information
     * @return true if successful, false otherwise
     */
    public boolean updateSubject(Subject subject) {
        String sql = "UPDATE subjects SET subject_code = ?, subject_name = ?, department = ?, " +
                "semester = ?, faculty_id = ? WHERE subject_id = ?";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, subject.getSubjectCode());
            stmt.setString(2, subject.getSubjectName());
            stmt.setString(3, subject.getDepartment());
            stmt.setInt(4, subject.getSemester());
            stmt.setInt(5, subject.getFacultyId());
            stmt.setInt(6, subject.getSubjectId());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error updating subject: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Delete subject
     * 
     * @param subjectId Subject ID
     * @return true if successful, false otherwise
     */
    public boolean deleteSubject(int subjectId) {
        String sql = "DELETE FROM subjects WHERE subject_id = ?";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, subjectId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting subject: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Enroll a student in a subject
     * 
     * @param studentId Student ID
     * @param subjectId Subject ID
     * @return true if successful, false otherwise
     */
    public boolean enrollStudent(int studentId, int subjectId) {
        String sql = "INSERT INTO student_subjects (student_id, subject_id) VALUES (?, ?)";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, studentId);
            stmt.setInt(2, subjectId);

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error enrolling student: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Unenroll a student from a subject
     * 
     * @param studentId Student ID
     * @param subjectId Subject ID
     * @return true if successful, false otherwise
     */
    public boolean unenrollStudent(int studentId, int subjectId) {
        String sql = "DELETE FROM student_subjects WHERE student_id = ? AND subject_id = ?";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, studentId);
            stmt.setInt(2, subjectId);

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error unenrolling student: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Extract Subject object from ResultSet
     * 
     * @param rs ResultSet
     * @return Subject object
     * @throws SQLException if error occurs
     */
    private Subject extractSubjectFromResultSet(ResultSet rs) throws SQLException {
        Subject subject = new Subject();
        subject.setSubjectId(rs.getInt("subject_id"));
        subject.setSubjectCode(rs.getString("subject_code"));
        subject.setSubjectName(rs.getString("subject_name"));
        subject.setDepartment(rs.getString("department"));
        subject.setSemester(rs.getInt("semester"));
        subject.setFacultyId(rs.getInt("faculty_id"));
        subject.setFacultyName(rs.getString("faculty_name"));
        return subject;
    }
}
