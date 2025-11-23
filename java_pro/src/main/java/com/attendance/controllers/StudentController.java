package com.attendance.controllers;

import com.attendance.dao.StudentDAO;
import com.attendance.models.Student;
import com.attendance.utils.ValidationUtil;

import java.util.List;

/**
 * Controller for student management operations
 */
public class StudentController {
    private StudentDAO studentDAO;

    public StudentController() {
        this.studentDAO = new StudentDAO();
    }

    /**
     * Add a new student with validation
     * 
     * @param student Student object
     * @return true if successful, false otherwise
     * @throws IllegalArgumentException if validation fails
     */
    public boolean addStudent(Student student) throws IllegalArgumentException {
        // Validate student data
        validateStudent(student);

        // Add student to database
        return studentDAO.addStudent(student);
    }

    /**
     * Update student information with validation
     * 
     * @param student Student object with updated information
     * @return true if successful, false otherwise
     * @throws IllegalArgumentException if validation fails
     */
    public boolean updateStudent(Student student) throws IllegalArgumentException {
        // Validate student data
        validateStudent(student);

        // Update student in database
        return studentDAO.updateStudent(student);
    }

    /**
     * Delete a student
     * 
     * @param studentId Student ID
     * @return true if successful, false otherwise
     */
    public boolean deleteStudent(int studentId) {
        return studentDAO.deleteStudent(studentId);
    }

    /**
     * Get all students
     * 
     * @return List of all students
     */
    public List<Student> getAllStudents() {
        return studentDAO.getAllStudents();
    }

    /**
     * Get student by ID
     * 
     * @param studentId Student ID
     * @return Student object if found, null otherwise
     */
    public Student getStudentById(int studentId) {
        return studentDAO.getStudentById(studentId);
    }

    /**
     * Get students enrolled in a subject
     * 
     * @param subjectId Subject ID
     * @return List of students
     */
    public List<Student> getStudentsBySubject(int subjectId) {
        return studentDAO.getStudentsBySubject(subjectId);
    }

    /**
     * Search students by name or roll number
     * 
     * @param query Search query
     * @return List of matching students
     */
    public List<Student> searchStudents(String query) {
        return studentDAO.searchStudents(query);
    }

    /**
     * Validate student data
     * 
     * @param student Student object to validate
     * @throws IllegalArgumentException if validation fails
     */
    private void validateStudent(Student student) throws IllegalArgumentException {
        if (!ValidationUtil.isNotEmpty(student.getRollNumber())) {
            throw new IllegalArgumentException("Roll number is required");
        }

        if (!ValidationUtil.isValidRollNumber(student.getRollNumber())) {
            throw new IllegalArgumentException(
                    "Invalid roll number format. Expected format: XX0000000 (e.g., CS2021001)");
        }

        if (!ValidationUtil.isNotEmpty(student.getFirstName())) {
            throw new IllegalArgumentException("First name is required");
        }

        if (!ValidationUtil.isNotEmpty(student.getLastName())) {
            throw new IllegalArgumentException("Last name is required");
        }

        if (student.getEmail() != null && !student.getEmail().isEmpty()) {
            if (!ValidationUtil.isValidEmail(student.getEmail())) {
                throw new IllegalArgumentException("Invalid email format");
            }
        }

        if (student.getPhone() != null && !student.getPhone().isEmpty()) {
            if (!ValidationUtil.isValidPhone(student.getPhone())) {
                throw new IllegalArgumentException("Invalid phone number. Must be 10 digits");
            }
        }

        if (!ValidationUtil.isNotEmpty(student.getDepartment())) {
            throw new IllegalArgumentException("Department is required");
        }

        if (!ValidationUtil.isValidSemester(student.getSemester())) {
            throw new IllegalArgumentException("Semester must be between 1 and 8");
        }
    }
}
