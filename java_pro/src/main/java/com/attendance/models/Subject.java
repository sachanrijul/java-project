package com.attendance.models;

/**
 * Subject model representing courses/subjects in the system
 */
public class Subject {
    private int subjectId;
    private String subjectCode;
    private String subjectName;
    private String department;
    private int semester;
    private int facultyId;
    private String facultyName; // For display purposes

    // Default constructor
    public Subject() {
    }

    // Constructor without subjectId (for new subjects)
    public Subject(String subjectCode, String subjectName, String department,
            int semester, int facultyId) {
        this.subjectCode = subjectCode;
        this.subjectName = subjectName;
        this.department = department;
        this.semester = semester;
        this.facultyId = facultyId;
    }

    // Full constructor
    public Subject(int subjectId, String subjectCode, String subjectName,
            String department, int semester, int facultyId) {
        this.subjectId = subjectId;
        this.subjectCode = subjectCode;
        this.subjectName = subjectName;
        this.department = department;
        this.semester = semester;
        this.facultyId = facultyId;
    }

    // Getters and Setters
    public int getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(int subjectId) {
        this.subjectId = subjectId;
    }

    public String getSubjectCode() {
        return subjectCode;
    }

    public void setSubjectCode(String subjectCode) {
        this.subjectCode = subjectCode;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public int getSemester() {
        return semester;
    }

    public void setSemester(int semester) {
        this.semester = semester;
    }

    public int getFacultyId() {
        return facultyId;
    }

    public void setFacultyId(int facultyId) {
        this.facultyId = facultyId;
    }

    public String getFacultyName() {
        return facultyName;
    }

    public void setFacultyName(String facultyName) {
        this.facultyName = facultyName;
    }

    @Override
    public String toString() {
        return "Subject{" +
                "subjectId=" + subjectId +
                ", subjectCode='" + subjectCode + '\'' +
                ", subjectName='" + subjectName + '\'' +
                ", department='" + department + '\'' +
                ", semester=" + semester +
                '}';
    }
}
