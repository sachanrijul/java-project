package com.attendance.dao;

import com.attendance.models.Attendance;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for Attendance operations
 */
public class AttendanceDAO {

    /**
     * Mark attendance for a student
     * If attendance already exists for the date, it will be updated
     * 
     * @param attendance Attendance object
     * @return true if successful, false otherwise
     */
    public boolean markAttendance(Attendance attendance) {
        String sql = "INSERT INTO attendance (student_id, subject_id, date, status, marked_by) " +
                "VALUES (?, ?, ?, ?, ?) " +
                "ON DUPLICATE KEY UPDATE status = ?, marked_by = ?, marked_at = NOW()";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, attendance.getStudentId());
            stmt.setInt(2, attendance.getSubjectId());
            stmt.setDate(3, attendance.getDate());
            stmt.setString(4, attendance.getStatus());
            stmt.setInt(5, attendance.getMarkedBy());
            stmt.setString(6, attendance.getStatus());
            stmt.setInt(7, attendance.getMarkedBy());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error marking attendance: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Get all attendance records for a student
     * 
     * @param studentId Student ID
     * @return List of attendance records
     */
    public List<Attendance> getAttendanceByStudent(int studentId) {
        List<Attendance> attendanceList = new ArrayList<>();
        String sql = "SELECT a.*, s.subject_name, u.full_name as marked_by_name " +
                "FROM attendance a " +
                "JOIN subjects s ON a.subject_id = s.subject_id " +
                "JOIN users u ON a.marked_by = u.user_id " +
                "WHERE a.student_id = ? ORDER BY a.date DESC";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, studentId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                attendanceList.add(extractAttendanceFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error getting attendance by student: " + e.getMessage());
            e.printStackTrace();
        }
        return attendanceList;
    }

    /**
     * Get attendance for a specific subject on a specific date
     * 
     * @param subjectId Subject ID
     * @param date      Date
     * @return List of attendance records
     */
    public List<Attendance> getAttendanceBySubjectAndDate(int subjectId, Date date) {
        List<Attendance> attendanceList = new ArrayList<>();
        String sql = "SELECT a.*, st.first_name, st.last_name, st.roll_number, " +
                "s.subject_name, u.full_name as marked_by_name " +
                "FROM attendance a " +
                "JOIN students st ON a.student_id = st.student_id " +
                "JOIN subjects s ON a.subject_id = s.subject_id " +
                "JOIN users u ON a.marked_by = u.user_id " +
                "WHERE a.subject_id = ? AND a.date = ? ORDER BY st.roll_number";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, subjectId);
            stmt.setDate(2, date);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Attendance attendance = extractAttendanceFromResultSet(rs);
                attendance.setStudentName(rs.getString("first_name") + " " + rs.getString("last_name") +
                        " (" + rs.getString("roll_number") + ")");
                attendanceList.add(attendance);
            }
        } catch (SQLException e) {
            System.err.println("Error getting attendance by subject and date: " + e.getMessage());
            e.printStackTrace();
        }
        return attendanceList;
    }

    /**
     * Calculate attendance percentage for a student in a subject
     * 
     * @param studentId Student ID
     * @param subjectId Subject ID
     * @return Attendance percentage (0-100)
     */
    public double calculateAttendancePercentage(int studentId, int subjectId) {
        String sql = "SELECT " +
                "SUM(CASE WHEN status = 'Present' OR status = 'Late' THEN 1 ELSE 0 END) as present, " +
                "COUNT(*) as total " +
                "FROM attendance WHERE student_id = ? AND subject_id = ?";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, studentId);
            stmt.setInt(2, subjectId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                int present = rs.getInt("present");
                int total = rs.getInt("total");
                return total > 0 ? (present * 100.0 / total) : 0.0;
            }
        } catch (SQLException e) {
            System.err.println("Error calculating attendance percentage: " + e.getMessage());
            e.printStackTrace();
        }
        return 0.0;
    }

    /**
     * Get attendance records by date range
     * 
     * @param studentId Student ID
     * @param subjectId Subject ID
     * @param startDate Start date
     * @param endDate   End date
     * @return List of attendance records
     */
    public List<Attendance> getAttendanceByDateRange(int studentId, int subjectId, Date startDate, Date endDate) {
        List<Attendance> attendanceList = new ArrayList<>();
        String sql = "SELECT a.*, s.subject_name, u.full_name as marked_by_name " +
                "FROM attendance a " +
                "JOIN subjects s ON a.subject_id = s.subject_id " +
                "JOIN users u ON a.marked_by = u.user_id " +
                "WHERE a.student_id = ? AND a.subject_id = ? AND a.date BETWEEN ? AND ? " +
                "ORDER BY a.date DESC";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, studentId);
            stmt.setInt(2, subjectId);
            stmt.setDate(3, startDate);
            stmt.setDate(4, endDate);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                attendanceList.add(extractAttendanceFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error getting attendance by date range: " + e.getMessage());
            e.printStackTrace();
        }
        return attendanceList;
    }

    /**
     * Get attendance summary for a subject (all students)
     * 
     * @param subjectId Subject ID
     * @return List of attendance records with student details
     */
    public List<Attendance> getAttendanceSummaryBySubject(int subjectId) {
        List<Attendance> attendanceList = new ArrayList<>();
        String sql = "SELECT st.student_id, st.first_name, st.last_name, st.roll_number, " +
                "SUM(CASE WHEN a.status = 'Present' OR a.status = 'Late' THEN 1 ELSE 0 END) as present, " +
                "COUNT(a.attendance_id) as total " +
                "FROM students st " +
                "JOIN student_subjects ss ON st.student_id = ss.student_id " +
                "LEFT JOIN attendance a ON st.student_id = a.student_id AND a.subject_id = ? " +
                "WHERE ss.subject_id = ? " +
                "GROUP BY st.student_id ORDER BY st.roll_number";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, subjectId);
            stmt.setInt(2, subjectId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Attendance attendance = new Attendance();
                attendance.setStudentId(rs.getInt("student_id"));
                attendance.setStudentName(rs.getString("first_name") + " " + rs.getString("last_name") +
                        " (" + rs.getString("roll_number") + ")");
                attendance.setSubjectId(subjectId);
                // Store percentage in a custom way (we'll use this for display)
                int present = rs.getInt("present");
                int total = rs.getInt("total");
                double percentage = total > 0 ? (present * 100.0 / total) : 0.0;
                // We'll store this info in the status field temporarily for display
                attendance.setStatus(String.format("%.2f%%", percentage));
                attendanceList.add(attendance);
            }
        } catch (SQLException e) {
            System.err.println("Error getting attendance summary: " + e.getMessage());
            e.printStackTrace();
        }
        return attendanceList;
    }

    /**
     * Update attendance status
     * 
     * @param attendanceId Attendance ID
     * @param status       New status
     * @return true if successful, false otherwise
     */
    public boolean updateAttendance(int attendanceId, String status) {
        String sql = "UPDATE attendance SET status = ?, marked_at = NOW() WHERE attendance_id = ?";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, status);
            stmt.setInt(2, attendanceId);

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error updating attendance: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Delete attendance record
     * 
     * @param attendanceId Attendance ID
     * @return true if successful, false otherwise
     */
    public boolean deleteAttendance(int attendanceId) {
        String sql = "DELETE FROM attendance WHERE attendance_id = ?";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, attendanceId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting attendance: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Extract Attendance object from ResultSet
     * 
     * @param rs ResultSet
     * @return Attendance object
     * @throws SQLException if error occurs
     */
    private Attendance extractAttendanceFromResultSet(ResultSet rs) throws SQLException {
        Attendance attendance = new Attendance();
        attendance.setAttendanceId(rs.getInt("attendance_id"));
        attendance.setStudentId(rs.getInt("student_id"));
        attendance.setSubjectId(rs.getInt("subject_id"));
        attendance.setDate(rs.getDate("date"));
        attendance.setStatus(rs.getString("status"));
        attendance.setMarkedBy(rs.getInt("marked_by"));
        attendance.setMarkedAt(rs.getTimestamp("marked_at"));

        // Set additional display fields if available
        try {
            attendance.setSubjectName(rs.getString("subject_name"));
        } catch (SQLException e) {
            // Column might not exist in all queries
        }
        try {
            attendance.setMarkedByName(rs.getString("marked_by_name"));
        } catch (SQLException e) {
            // Column might not exist in all queries
        }

        return attendance;
    }
}
