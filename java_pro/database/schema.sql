-- Student Attendance Management System - Database Schema
-- Database: attendance_db

-- Create database
CREATE DATABASE IF NOT EXISTS attendance_db;
USE attendance_db;

-- Drop existing tables if they exist (for clean setup)
DROP TABLE IF EXISTS attendance;
DROP TABLE IF EXISTS student_subjects;
DROP TABLE IF EXISTS subjects;
DROP TABLE IF EXISTS students;
DROP TABLE IF EXISTS users;

-- Table: users
-- Stores admin and faculty user accounts
CREATE TABLE users (
    user_id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    role ENUM('Admin', 'Faculty') NOT NULL,
    full_name VARCHAR(100) NOT NULL,
    email VARCHAR(100),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_username (username),
    INDEX idx_role (role)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Table: students
-- Stores student information
CREATE TABLE students (
    student_id INT PRIMARY KEY AUTO_INCREMENT,
    roll_number VARCHAR(20) UNIQUE NOT NULL,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    email VARCHAR(100),
    phone VARCHAR(15),
    department VARCHAR(50),
    semester INT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_roll_number (roll_number),
    INDEX idx_department (department),
    INDEX idx_semester (semester)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Table: subjects
-- Stores subject/course information
CREATE TABLE subjects (
    subject_id INT PRIMARY KEY AUTO_INCREMENT,
    subject_code VARCHAR(20) UNIQUE NOT NULL,
    subject_name VARCHAR(100) NOT NULL,
    department VARCHAR(50),
    semester INT,
    faculty_id INT,
    FOREIGN KEY (faculty_id) REFERENCES users(user_id) ON DELETE SET NULL,
    INDEX idx_subject_code (subject_code),
    INDEX idx_faculty (faculty_id),
    INDEX idx_dept_sem (department, semester)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Table: attendance
-- Stores daily attendance records
CREATE TABLE attendance (
    attendance_id INT PRIMARY KEY AUTO_INCREMENT,
    student_id INT NOT NULL,
    subject_id INT NOT NULL,
    date DATE NOT NULL,
    status ENUM('Present', 'Absent', 'Late') NOT NULL,
    marked_by INT NOT NULL,
    marked_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (student_id) REFERENCES students(student_id) ON DELETE CASCADE,
    FOREIGN KEY (subject_id) REFERENCES subjects(subject_id) ON DELETE CASCADE,
    FOREIGN KEY (marked_by) REFERENCES users(user_id) ON DELETE CASCADE,
    UNIQUE KEY unique_attendance (student_id, subject_id, date),
    INDEX idx_student (student_id),
    INDEX idx_subject (subject_id),
    INDEX idx_date (date),
    INDEX idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Table: student_subjects
-- Junction table for many-to-many relationship between students and subjects
CREATE TABLE student_subjects (
    id INT PRIMARY KEY AUTO_INCREMENT,
    student_id INT NOT NULL,
    subject_id INT NOT NULL,
    enrolled_date DATE DEFAULT (CURRENT_DATE),
    FOREIGN KEY (student_id) REFERENCES students(student_id) ON DELETE CASCADE,
    FOREIGN KEY (subject_id) REFERENCES subjects(subject_id) ON DELETE CASCADE,
    UNIQUE KEY unique_enrollment (student_id, subject_id),
    INDEX idx_student (student_id),
    INDEX idx_subject (subject_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Display success message
SELECT 'Database schema created successfully!' AS Status;
