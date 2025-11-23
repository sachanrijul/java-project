#!/bin/bash

# Student Attendance Management System - Setup Script
# This script helps set up the database and compile the project

echo "=========================================="
echo "Student Attendance System - Setup"
echo "=========================================="
echo ""

# Check if MySQL is installed
if ! command -v mysql &> /dev/null; then
    echo "❌ MySQL is not installed. Please install MySQL first."
    exit 1
fi

echo "✓ MySQL found"

# Check if Java is installed
if ! command -v javac &> /dev/null; then
    echo "❌ Java compiler (javac) not found. Please install JDK."
    exit 1
fi

echo "✓ Java compiler found"
java -version

echo ""
echo "=========================================="
echo "Step 1: Database Setup"
echo "=========================================="
echo ""

read -p "Enter MySQL root password: " -s MYSQL_PASSWORD
echo ""

# Create database and tables
echo "Creating database and tables..."
mysql -u root -p"$MYSQL_PASSWORD" < database/schema.sql

if [ $? -eq 0 ]; then
    echo "✓ Database schema created successfully"
else
    echo "❌ Failed to create database schema"
    exit 1
fi

# Insert sample data
echo "Inserting sample data..."
mysql -u root -p"$MYSQL_PASSWORD" attendance_db < database/sample_data.sql

if [ $? -eq 0 ]; then
    echo "✓ Sample data inserted successfully"
else
    echo "❌ Failed to insert sample data"
    exit 1
fi

echo ""
echo "=========================================="
echo "Step 2: Compilation"
echo "=========================================="
echo ""

# Check if lib directory exists and has MySQL connector
if [ ! -d "lib" ]; then
    mkdir lib
    echo "Created lib/ directory"
fi

if [ ! -f "lib/mysql-connector-java-*.jar" ] && [ ! -f "lib/mysql-connector-j-*.jar" ]; then
    echo "⚠️  MySQL Connector JAR not found in lib/ directory"
    echo "Please download from: https://dev.mysql.com/downloads/connector/j/"
    echo "and place it in the lib/ directory"
    read -p "Press Enter when ready to continue..."
fi

# Create bin directory if it doesn't exist
if [ ! -d "bin" ]; then
    mkdir bin
    echo "Created bin/ directory"
fi

# Compile Java files
echo "Compiling Java files..."
javac -cp "bin:lib/*" -d bin src/main/java/com/attendance/**/*.java

if [ $? -eq 0 ]; then
    echo "✓ Compilation successful"
else
    echo "❌ Compilation failed"
    exit 1
fi

echo ""
echo "=========================================="
echo "Setup Complete!"
echo "=========================================="
echo ""
echo "To run the application:"
echo "  java -cp \"bin:lib/*\" com.attendance.Main"
echo ""
echo "Default credentials:"
echo "  Username: admin"
echo "  Password: admin123"
echo ""
echo "=========================================="
