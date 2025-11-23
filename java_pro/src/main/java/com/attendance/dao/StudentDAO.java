package com.attendance.dao;

import com.attendance.models.Student;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for Student operations
 */
public class StudentDAO {

    /**
     * Add a new student
     * 
     * @param student Student object
     * @return true if successful, false otherwise
     */
    public boolean addStudent(Student student) {
        String sql = "INSERT INTO students (roll_number, first_name, last_name, email, phone, department, semester) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, student.getRollNumber());
            stmt.setString(2, student.getFirstName());
            stmt.setString(3, student.getLastName());
            stmt.setString(4, student.getEmail());
            stmt.setString(5, student.getPhone());
            stmt.setString(6, student.getDepartment());
            stmt.setInt(7, student.getSemester());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error adding student: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Get all students
     * 
     * @return List of all students
     */
    public List<Student> getAllStudents() {
        List<Student> students = new ArrayList<>();
        String sql = "SELECT * FROM students ORDER BY roll_number";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                students.add(extractStudentFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error getting all students: " + e.getMessage());
            e.printStackTrace();
        }
        return students;
    }

    /**
     * Get student by ID
     * 
     * @param studentId Student ID
     * @return Student object if found, null otherwise
     */
    public Student getStudentById(int studentId) {
        String sql = "SELECT * FROM students WHERE student_id = ?";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, studentId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return extractStudentFromResultSet(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error getting student by ID: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Get students enrolled in a specific subject
     * 
     * @param subjectId Subject ID
     * @return List of students enrolled in the subject
     */
    public List<Student> getStudentsBySubject(int subjectId) {
        List<Student> students = new ArrayList<>();
        String sql = "SELECT s.* FROM students s " +
                "JOIN student_subjects ss ON s.student_id = ss.student_id " +
                "WHERE ss.subject_id = ? ORDER BY s.roll_number";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, subjectId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                students.add(extractStudentFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error getting students by subject: " + e.getMessage());
            e.printStackTrace();
        }
        return students;
    }

    /**
     * Update student information
     * 
     * @param student Student object with updated information
     * @return true if successful, false otherwise
     */
    public boolean updateStudent(Student student) {
        String sql = "UPDATE students SET roll_number = ?, first_name = ?, last_name = ?, " +
                "email = ?, phone = ?, department = ?, semester = ? WHERE student_id = ?";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, student.getRollNumber());
            stmt.setString(2, student.getFirstName());
            stmt.setString(3, student.getLastName());
            stmt.setString(4, student.getEmail());
            stmt.setString(5, student.getPhone());
            stmt.setString(6, student.getDepartment());
            stmt.setInt(7, student.getSemester());
            stmt.setInt(8, student.getStudentId());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error updating student: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Delete student
     * 
     * @param studentId Student ID
     * @return true if successful, false otherwise
     */
    public boolean deleteStudent(int studentId) {
        String sql = "DELETE FROM students WHERE student_id = ?";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, studentId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting student: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Search students by name or roll number
     * 
     * @param query Search query
     * @return List of matching students
     */
    public List<Student> searchStudents(String query) {
        List<Student> students = new ArrayList<>();
        String sql = "SELECT * FROM students WHERE " +
                "roll_number LIKE ? OR first_name LIKE ? OR last_name LIKE ? " +
                "ORDER BY roll_number";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            String searchPattern = "%" + query + "%";
            stmt.setString(1, searchPattern);
            stmt.setString(2, searchPattern);
            stmt.setString(3, searchPattern);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                students.add(extractStudentFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error searching students: " + e.getMessage());
            e.printStackTrace();
        }
        return students;
    }

    /**
     * Extract Student object from ResultSet
     * 
     * @param rs ResultSet
     * @return Student object
     * @throws SQLException if error occurs
     */
    private Student extractStudentFromResultSet(ResultSet rs) throws SQLException {
        Student student = new Student();
        student.setStudentId(rs.getInt("student_id"));
        student.setRollNumber(rs.getString("roll_number"));
        student.setFirstName(rs.getString("first_name"));
        student.setLastName(rs.getString("last_name"));
        student.setEmail(rs.getString("email"));
        student.setPhone(rs.getString("phone"));
        student.setDepartment(rs.getString("department"));
        student.setSemester(rs.getInt("semester"));
        student.setCreatedAt(rs.getTimestamp("created_at"));
        return student;
    }
}
