package com.attendance.models;

import java.sql.Date;
import java.sql.Timestamp;

/**
 * Attendance model representing daily attendance records
 */
public class Attendance {
    private int attendanceId;
    private int studentId;
    private int subjectId;
    private Date date;
    private String status; // "Present", "Absent", or "Late"
    private int markedBy;
    private Timestamp markedAt;

    // Additional fields for display purposes
    private String studentName;
    private String subjectName;
    private String markedByName;

    // Attendance status constants
    public static final String STATUS_PRESENT = "Present";
    public static final String STATUS_ABSENT = "Absent";
    public static final String STATUS_LATE = "Late";

    // Default constructor
    public Attendance() {
    }

    // Constructor without attendanceId (for new attendance records)
    public Attendance(int studentId, int subjectId, Date date, String status, int markedBy) {
        this.studentId = studentId;
        this.subjectId = subjectId;
        this.date = date;
        this.status = status;
        this.markedBy = markedBy;
    }

    // Full constructor
    public Attendance(int attendanceId, int studentId, int subjectId, Date date,
            String status, int markedBy, Timestamp markedAt) {
        this.attendanceId = attendanceId;
        this.studentId = studentId;
        this.subjectId = subjectId;
        this.date = date;
        this.status = status;
        this.markedBy = markedBy;
        this.markedAt = markedAt;
    }

    // Getters and Setters
    public int getAttendanceId() {
        return attendanceId;
    }

    public void setAttendanceId(int attendanceId) {
        this.attendanceId = attendanceId;
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public int getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(int subjectId) {
        this.subjectId = subjectId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getMarkedBy() {
        return markedBy;
    }

    public void setMarkedBy(int markedBy) {
        this.markedBy = markedBy;
    }

    public Timestamp getMarkedAt() {
        return markedAt;
    }

    public void setMarkedAt(Timestamp markedAt) {
        this.markedAt = markedAt;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public String getMarkedByName() {
        return markedByName;
    }

    public void setMarkedByName(String markedByName) {
        this.markedByName = markedByName;
    }

    // Helper methods
    public boolean isPresent() {
        return STATUS_PRESENT.equals(status);
    }

    public boolean isAbsent() {
        return STATUS_ABSENT.equals(status);
    }

    public boolean isLate() {
        return STATUS_LATE.equals(status);
    }

    @Override
    public String toString() {
        return "Attendance{" +
                "attendanceId=" + attendanceId +
                ", studentId=" + studentId +
                ", subjectId=" + subjectId +
                ", date=" + date +
                ", status='" + status + '\'' +
                '}';
    }
}
