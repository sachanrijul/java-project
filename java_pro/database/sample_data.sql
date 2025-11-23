-- Student Attendance Management System - Sample Data
-- This file populates the database with sample data for testing

USE attendance_db;

-- Insert default admin user
-- Username: admin, Password: admin123 (hashed with SHA-256)
INSERT INTO users (username, password_hash, role, full_name, email) VALUES
('admin', '240be518fabd2724ddb6f04eeb1da5967448d7e831c08c8fa822809f74c720a9', 'Admin', 'System Administrator', 'admin@attendance.edu'),
('faculty1', 'fc1ebc848e31e0a68e868432225e3c82d67e77c8e5a3e3f8e2e6e8f8c2c8e8f8', 'Faculty', 'Dr. John Smith', 'john.smith@attendance.edu'),
('faculty2', 'a4c8f8e2e6e8f8c2c8e8f8fc1ebc848e31e0a68e868432225e3c82d67e77c8e5', 'Faculty', 'Prof. Sarah Johnson', 'sarah.johnson@attendance.edu');

-- Insert sample students
INSERT INTO students (roll_number, first_name, last_name, email, phone, department, semester) VALUES
('CS2021001', 'Alice', 'Anderson', 'alice.anderson@student.edu', '9876543210', 'Computer Science', 3),
('CS2021002', 'Bob', 'Brown', 'bob.brown@student.edu', '9876543211', 'Computer Science', 3),
('CS2021003', 'Charlie', 'Chen', 'charlie.chen@student.edu', '9876543212', 'Computer Science', 3),
('CS2021004', 'Diana', 'Davis', 'diana.davis@student.edu', '9876543213', 'Computer Science', 3),
('CS2021005', 'Ethan', 'Evans', 'ethan.evans@student.edu', '9876543214', 'Computer Science', 3),
('CS2021006', 'Fiona', 'Foster', 'fiona.foster@student.edu', '9876543215', 'Computer Science', 3),
('CS2021007', 'George', 'Garcia', 'george.garcia@student.edu', '9876543216', 'Computer Science', 3),
('CS2021008', 'Hannah', 'Harris', 'hannah.harris@student.edu', '9876543217', 'Computer Science', 3),
('IT2021001', 'Ian', 'Ivanov', 'ian.ivanov@student.edu', '9876543218', 'Information Technology', 3),
('IT2021002', 'Julia', 'Jackson', 'julia.jackson@student.edu', '9876543219', 'Information Technology', 3);

-- Insert sample subjects
INSERT INTO subjects (subject_code, subject_name, department, semester, faculty_id) VALUES
('CS301', 'Database Management Systems', 'Computer Science', 3, 2),
('CS302', 'Operating Systems', 'Computer Science', 3, 2),
('CS303', 'Computer Networks', 'Computer Science', 3, 3),
('IT301', 'Web Technologies', 'Information Technology', 3, 3),
('CS304', 'Software Engineering', 'Computer Science', 3, 2);

-- Enroll students in subjects
-- CS students enrolled in CS subjects
INSERT INTO student_subjects (student_id, subject_id, enrolled_date) VALUES
-- Database Management Systems (subject_id: 1)
(1, 1, '2025-01-01'), (2, 1, '2025-01-01'), (3, 1, '2025-01-01'), 
(4, 1, '2025-01-01'), (5, 1, '2025-01-01'), (6, 1, '2025-01-01'), 
(7, 1, '2025-01-01'), (8, 1, '2025-01-01'),

-- Operating Systems (subject_id: 2)
(1, 2, '2025-01-01'), (2, 2, '2025-01-01'), (3, 2, '2025-01-01'), 
(4, 2, '2025-01-01'), (5, 2, '2025-01-01'), (6, 2, '2025-01-01'), 
(7, 2, '2025-01-01'), (8, 2, '2025-01-01'),

-- Computer Networks (subject_id: 3)
(1, 3, '2025-01-01'), (2, 3, '2025-01-01'), (3, 3, '2025-01-01'), 
(4, 3, '2025-01-01'), (5, 3, '2025-01-01'), (6, 3, '2025-01-01'), 
(7, 3, '2025-01-01'), (8, 3, '2025-01-01'),

-- Software Engineering (subject_id: 5)
(1, 5, '2025-01-01'), (2, 5, '2025-01-01'), (3, 5, '2025-01-01'), 
(4, 5, '2025-01-01'), (5, 5, '2025-01-01'), (6, 5, '2025-01-01'), 
(7, 5, '2025-01-01'), (8, 5, '2025-01-01'),

-- IT students enrolled in Web Technologies (subject_id: 4)
(9, 4, '2025-01-01'), (10, 4, '2025-01-01');

-- Insert sample attendance records (for testing purposes)
-- Database Management Systems - Last 10 days
INSERT INTO attendance (student_id, subject_id, date, status, marked_by) VALUES
-- Day 1 (2025-11-13)
(1, 1, '2025-11-13', 'Present', 2), (2, 1, '2025-11-13', 'Present', 2),
(3, 1, '2025-11-13', 'Absent', 2), (4, 1, '2025-11-13', 'Present', 2),
(5, 1, '2025-11-13', 'Late', 2), (6, 1, '2025-11-13', 'Present', 2),
(7, 1, '2025-11-13', 'Present', 2), (8, 1, '2025-11-13', 'Absent', 2),

-- Day 2 (2025-11-14)
(1, 1, '2025-11-14', 'Present', 2), (2, 1, '2025-11-14', 'Absent', 2),
(3, 1, '2025-11-14', 'Present', 2), (4, 1, '2025-11-14', 'Present', 2),
(5, 1, '2025-11-14', 'Present', 2), (6, 1, '2025-11-14', 'Absent', 2),
(7, 1, '2025-11-14', 'Present', 2), (8, 1, '2025-11-14', 'Present', 2),

-- Day 3 (2025-11-15)
(1, 1, '2025-11-15', 'Present', 2), (2, 1, '2025-11-15', 'Present', 2),
(3, 1, '2025-11-15', 'Present', 2), (4, 1, '2025-11-15', 'Absent', 2),
(5, 1, '2025-11-15', 'Present', 2), (6, 1, '2025-11-15', 'Present', 2),
(7, 1, '2025-11-15', 'Absent', 2), (8, 1, '2025-11-15', 'Present', 2),

-- Operating Systems - Sample data
(1, 2, '2025-11-13', 'Present', 2), (2, 2, '2025-11-13', 'Present', 2),
(3, 2, '2025-11-13', 'Present', 2), (4, 2, '2025-11-13', 'Absent', 2),
(5, 2, '2025-11-13', 'Present', 2), (6, 2, '2025-11-13', 'Present', 2),

-- Computer Networks - Sample data
(1, 3, '2025-11-13', 'Present', 3), (2, 3, '2025-11-13', 'Absent', 3),
(3, 3, '2025-11-13', 'Absent', 3), (4, 3, '2025-11-13', 'Present', 3),
(5, 3, '2025-11-13', 'Present', 3), (6, 3, '2025-11-13', 'Absent', 3);

-- Display summary
SELECT 'Sample data inserted successfully!' AS Status;
SELECT COUNT(*) AS 'Total Users' FROM users;
SELECT COUNT(*) AS 'Total Students' FROM students;
SELECT COUNT(*) AS 'Total Subjects' FROM subjects;
SELECT COUNT(*) AS 'Total Enrollments' FROM student_subjects;
SELECT COUNT(*) AS 'Total Attendance Records' FROM attendance;
