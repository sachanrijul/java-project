package com.attendance.models;

import java.sql.Timestamp;

/**
 * Student model representing student information
 */
public class Student {
    private int studentId;
    private String rollNumber;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String department;
    private int semester;
    private Timestamp createdAt;

    // Default constructor
    public Student() {
    }

    // Constructor without studentId (for new students)
    public Student(String rollNumber, String firstName, String lastName,
            String email, String phone, String department, int semester) {
        this.rollNumber = rollNumber;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.department = department;
        this.semester = semester;
    }

    // Full constructor
    public Student(int studentId, String rollNumber, String firstName, String lastName,
            String email, String phone, String department, int semester, Timestamp createdAt) {
        this.studentId = studentId;
        this.rollNumber = rollNumber;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.department = department;
        this.semester = semester;
        this.createdAt = createdAt;
    }

    // Getters and Setters
    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public String getRollNumber() {
        return rollNumber;
    }

    public void setRollNumber(String rollNumber) {
        this.rollNumber = rollNumber;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    // Helper method to get full name
    public String getFullName() {
        return firstName + " " + lastName;
    }

    @Override
    public String toString() {
        return "Student{" +
                "studentId=" + studentId +
                ", rollNumber='" + rollNumber + '\'' +
                ", name='" + getFullName() + '\'' +
                ", department='" + department + '\'' +
                ", semester=" + semester +
                '}';
    }
}
