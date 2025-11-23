package com.attendance.controllers;

import com.attendance.dao.SubjectDAO;
import com.attendance.models.Subject;
import com.attendance.utils.ValidationUtil;

import java.util.List;

/**
 * Controller for subject management operations
 */
public class SubjectController {
    private SubjectDAO subjectDAO;

    public SubjectController() {
        this.subjectDAO = new SubjectDAO();
    }

    /**
     * Add a new subject with validation
     * 
     * @param subject Subject object
     * @return true if successful, false otherwise
     * @throws IllegalArgumentException if validation fails
     */
    public boolean addSubject(Subject subject) throws IllegalArgumentException {
        // Validate subject data
        validateSubject(subject);

        // Add subject to database
        return subjectDAO.addSubject(subject);
    }

    /**
     * Update subject information with validation
     * 
     * @param subject Subject object with updated information
     * @return true if successful, false otherwise
     * @throws IllegalArgumentException if validation fails
     */
    public boolean updateSubject(Subject subject) throws IllegalArgumentException {
        // Validate subject data
        validateSubject(subject);

        // Update subject in database
        return subjectDAO.updateSubject(subject);
    }

    /**
     * Delete a subject
     * 
     * @param subjectId Subject ID
     * @return true if successful, false otherwise
     */
    public boolean deleteSubject(int subjectId) {
        return subjectDAO.deleteSubject(subjectId);
    }

    /**
     * Get all subjects
     * 
     * @return List of all subjects
     */
    public List<Subject> getAllSubjects() {
        return subjectDAO.getAllSubjects();
    }

    /**
     * Get subject by ID
     * 
     * @param subjectId Subject ID
     * @return Subject object if found, null otherwise
     */
    public Subject getSubjectById(int subjectId) {
        return subjectDAO.getSubjectById(subjectId);
    }

    /**
     * Get subjects assigned to a faculty
     * 
     * @param facultyId Faculty ID
     * @return List of subjects
     */
    public List<Subject> getSubjectsByFaculty(int facultyId) {
        return subjectDAO.getSubjectsByFaculty(facultyId);
    }

    /**
     * Assign faculty to a subject
     * 
     * @param subjectId Subject ID
     * @param facultyId Faculty ID
     * @return true if successful, false otherwise
     */
    public boolean assignFaculty(int subjectId, int facultyId) {
        Subject subject = subjectDAO.getSubjectById(subjectId);
        if (subject != null) {
            subject.setFacultyId(facultyId);
            return subjectDAO.updateSubject(subject);
        }
        return false;
    }

    /**
     * Enroll a student in a subject
     * 
     * @param studentId Student ID
     * @param subjectId Subject ID
     * @return true if successful, false otherwise
     */
    public boolean enrollStudent(int studentId, int subjectId) {
        return subjectDAO.enrollStudent(studentId, subjectId);
    }

    /**
     * Unenroll a student from a subject
     * 
     * @param studentId Student ID
     * @param subjectId Subject ID
     * @return true if successful, false otherwise
     */
    public boolean unenrollStudent(int studentId, int subjectId) {
        return subjectDAO.unenrollStudent(studentId, subjectId);
    }

    /**
     * Validate subject data
     * 
     * @param subject Subject object to validate
     * @throws IllegalArgumentException if validation fails
     */
    private void validateSubject(Subject subject) throws IllegalArgumentException {
        if (!ValidationUtil.isNotEmpty(subject.getSubjectCode())) {
            throw new IllegalArgumentException("Subject code is required");
        }

        if (!ValidationUtil.isNotEmpty(subject.getSubjectName())) {
            throw new IllegalArgumentException("Subject name is required");
        }

        if (!ValidationUtil.isNotEmpty(subject.getDepartment())) {
            throw new IllegalArgumentException("Department is required");
        }

        if (!ValidationUtil.isValidSemester(subject.getSemester())) {
            throw new IllegalArgumentException("Semester must be between 1 and 8");
        }

        if (!ValidationUtil.isPositive(subject.getFacultyId())) {
            throw new IllegalArgumentException("Faculty must be assigned");
        }
    }
}
