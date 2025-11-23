# Student Attendance Management System

A comprehensive Java-based desktop application for automating student attendance tracking with reporting capabilities and low attendance alerts.

## 📋 Features

- ✅ **User Authentication** - Secure login for Admin and Faculty roles
- ✅ **Student Management** - Complete CRUD operations for student records
- ✅ **Subject Management** - Manage subjects and assign faculty
- ✅ **Attendance Tracking** - Mark daily attendance (Present/Absent/Late)
- ✅ **Attendance Reports** - Generate reports by student, subject, or date
- ✅ **Low Attendance Alerts** - Automatic alerts for students with <75% attendance
- ✅ **Data Export** - Export reports to CSV and PDF formats
- ✅ **Role-Based Access** - Different features for Admin and Faculty users

## 🛠️ Technologies Used

- **Language:** Java 8+
- **Database:** MySQL
- **GUI Framework:** Java Swing
- **Database Connectivity:** JDBC (MySQL Connector)
- **Security:** SHA-256 password hashing
- **Architecture:** 3-Tier (Presentation → Business Logic → Data Access)

## 📁 Project Structure

```
StudentAttendanceSystem/
├── src/main/java/com/attendance/
│   ├── models/          # Data models (User, Student, Subject, Attendance)
│   ├── dao/             # Data Access Objects
│   ├── controllers/     # Business logic controllers
│   ├── views/           # Swing GUI frames
│   ├── utils/           # Utility classes (Password, Validation, Export)
│   └── Main.java        # Application entry point
├── database/
│   ├── schema.sql       # Database schema
│   └── sample_data.sql  # Sample data for testing
├── lib/                 # External JAR files
├── bin/                 # Compiled classes
└── README.md
```

## 🚀 Installation & Setup

### Prerequisites

1. **Java Development Kit (JDK) 8 or higher**
   ```bash
   java -version
   ```

2. **MySQL Server**
   ```bash
   mysql --version
   ```

3. **MySQL Connector JAR**
   - Download from: https://dev.mysql.com/downloads/connector/j/
   - Place in `lib/` directory

### Database Setup

1. **Start MySQL service**
   ```bash
   sudo systemctl start mysql
   # OR on Windows: net start MySQL
   ```

2. **Create database and tables**
   ```bash
   mysql -u root -p < database/schema.sql
   ```

3. **Insert sample data**
   ```bash
   mysql -u root -p attendance_db < database/sample_data.sql
   ```

4. **Update database credentials** (if needed)
   - Edit `src/main/java/com/attendance/dao/DatabaseConnection.java`
   - Update `USER` and `PASSWORD` constants

### Compilation & Execution

1. **Compile the project**
   ```bash
   javac -cp ".:lib/*" -d bin src/main/java/com/attendance/**/*.java
   ```

2. **Run the application**
   ```bash
   java -cp "bin:lib/*" com.attendance.Main
   ```

   **On Windows:**
   ```cmd
   javac -cp ".;lib/*" -d bin src/main/java/com/attendance/**/*.java
   java -cp "bin;lib/*" com.attendance.Main
   ```

## 🔐 Default Credentials

### Admin Account
- **Username:** `admin`
- **Password:** `admin123`

### Faculty Account
- **Username:** `faculty1`
- **Password:** `faculty123`

> **Note:** Change these credentials after first login for security.

## 📖 User Guide

### For Admin Users

1. **Manage Students**
   - Add, edit, or delete student records
   - Search students by name or roll number
   - View all student details

2. **Manage Subjects**
   - Create and manage subjects
   - Assign faculty to subjects
   - Enroll students in subjects

3. **View Reports**
   - Generate attendance reports
   - Export data to CSV/PDF

### For Faculty Users

1. **Mark Attendance**
   - Select subject and date
   - Mark students as Present/Absent/Late
   - Submit bulk attendance

2. **View Reports**
   - Check attendance percentages
   - Identify low attendance students
   - Export reports

## 📊 Database Schema

### Tables

- **users** - Admin and faculty accounts
- **students** - Student information
- **subjects** - Course/subject details
- **attendance** - Daily attendance records
- **student_subjects** - Student-subject enrollment

## 🔒 Security Features

- SHA-256 password hashing
- SQL injection prevention using PreparedStatements
- Session management for logged-in users
- Role-based access control

## 🐛 Troubleshooting

### Database Connection Error
```
Error: Unable to connect to database
```
**Solution:** 
- Verify MySQL is running
- Check database credentials in `DatabaseConnection.java`
- Ensure database `attendance_db` exists

### ClassNotFoundException: com.mysql.cj.jdbc.Driver
```
Error: MySQL JDBC Driver not found
```
**Solution:**
- Download MySQL Connector JAR
- Place in `lib/` directory
- Include in classpath when compiling/running

### Duplicate Entry Error
```
Error: Duplicate entry for key 'roll_number'
```
**Solution:**
- Roll numbers must be unique
- Use different roll number or update existing student

## 📝 License

This project is created for educational purposes.

## 👥 Contributors

- Student Attendance System Development Team

## 📧 Support

For issues or questions, please refer to the project documentation or contact the development team.

---

**Version:** 1.0  
**Last Updated:** November 2025
