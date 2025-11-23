package com.attendance.controllers;

import com.attendance.dao.AttendanceDAO;
import com.attendance.dao.StudentDAO;
import com.attendance.models.Attendance;
import com.attendance.models.Student;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

/**
 * Controller for attendance management operations
 */
public class AttendanceController {
    private AttendanceDAO attendanceDAO;
    private StudentDAO studentDAO;

    public AttendanceController() {
        this.attendanceDAO = new AttendanceDAO();
        this.studentDAO = new StudentDAO();
    }

    /**
     * Mark attendance for a student
     * 
     * @param studentId Student ID
     * @param subjectId Subject ID
     * @param date      Date
     * @param status    Attendance status (Present/Absent/Late)
     * @param markedBy  User ID of person marking attendance
     * @return true if successful, false otherwise
     */
    public boolean markAttendance(int studentId, int subjectId, Date date, String status, int markedBy) {
        Attendance attendance = new Attendance();
        attendance.setStudentId(studentId);
        attendance.setSubjectId(subjectId);
        attendance.setDate(date);
        attendance.setStatus(status);
        attendance.setMarkedBy(markedBy);

        return attendanceDAO.markAttendance(attendance);
    }

    /**
     * Mark attendance for multiple students
     * 
     * @param attendanceList List of attendance records
     * @return true if all successful, false otherwise
     */
    public boolean markBulkAttendance(List<Attendance> attendanceList) {
        boolean allSuccessful = true;
        for (Attendance attendance : attendanceList) {
            if (!attendanceDAO.markAttendance(attendance)) {
                allSuccessful = false;
            }
        }
        return allSuccessful;
    }

    /**
     * Get attendance records for a student
     * 
     * @param studentId Student ID
     * @return List of attendance records
     */
    public List<Attendance> getAttendanceByStudent(int studentId) {
        return attendanceDAO.getAttendanceByStudent(studentId);
    }

    /**
     * Get attendance for a subject on a specific date
     * 
     * @param subjectId Subject ID
     * @param date      Date
     * @return List of attendance records
     */
    public List<Attendance> getAttendanceBySubjectAndDate(int subjectId, Date date) {
        return attendanceDAO.getAttendanceBySubjectAndDate(subjectId, date);
    }

    /**
     * Calculate attendance percentage for a student in a subject
     * 
     * @param studentId Student ID
     * @param subjectId Subject ID
     * @return Attendance percentage
     */
    public double getAttendancePercentage(int studentId, int subjectId) {
        return attendanceDAO.calculateAttendancePercentage(studentId, subjectId);
    }

    /**
     * Get students with low attendance (below threshold)
     * 
     * @param subjectId Subject ID
     * @param threshold Threshold percentage (e.g., 75.0)
     * @return List of students with low attendance
     */
    public List<Student> getLowAttendanceStudents(int subjectId, double threshold) {
        List<Student> lowAttendanceStudents = new ArrayList<>();
        List<Student> allStudents = studentDAO.getStudentsBySubject(subjectId);

        for (Student student : allStudents) {
            double percentage = attendanceDAO.calculateAttendancePercentage(student.getStudentId(), subjectId);
            if (percentage < threshold && percentage > 0) { // Only include if they have some attendance records
                lowAttendanceStudents.add(student);
            }
        }

        return lowAttendanceStudents;
    }

    /**
     * Get attendance summary for a subject
     * 
     * @param subjectId Subject ID
     * @return List of attendance records with summary
     */
    public List<Attendance> getAttendanceSummary(int subjectId) {
        return attendanceDAO.getAttendanceSummaryBySubject(subjectId);
    }

    /**
     * Update attendance status
     * 
     * @param attendanceId Attendance ID
     * @param status       New status
     * @return true if successful, false otherwise
     */
    public boolean updateAttendance(int attendanceId, String status) {
        return attendanceDAO.updateAttendance(attendanceId, status);
    }
}
