package com.attendance.utils;

import java.util.regex.Pattern;

/**
 * Utility class for input validation
 */
public class ValidationUtil {

    // Email regex pattern
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

    // Phone regex pattern (10 digits)
    private static final Pattern PHONE_PATTERN = Pattern.compile("^[0-9]{10}$");

    // Roll number pattern (alphanumeric)
    private static final Pattern ROLL_NUMBER_PATTERN = Pattern.compile("^[A-Z]{2}[0-9]{7}$");

    /**
     * Check if a string is not null and not empty
     * 
     * @param str String to check
     * @return true if string is not empty, false otherwise
     */
    public static boolean isNotEmpty(String str) {
        return str != null && !str.trim().isEmpty();
    }

    /**
     * Validate email format
     * 
     * @param email Email address to validate
     * @return true if email is valid, false otherwise
     */
    public static boolean isValidEmail(String email) {
        if (!isNotEmpty(email)) {
            return false;
        }
        return EMAIL_PATTERN.matcher(email).matches();
    }

    /**
     * Validate phone number format (10 digits)
     * 
     * @param phone Phone number to validate
     * @return true if phone is valid, false otherwise
     */
    public static boolean isValidPhone(String phone) {
        if (!isNotEmpty(phone)) {
            return false;
        }
        return PHONE_PATTERN.matcher(phone).matches();
    }

    /**
     * Validate roll number format (e.g., CS2021001)
     * 
     * @param rollNumber Roll number to validate
     * @return true if roll number is valid, false otherwise
     */
    public static boolean isValidRollNumber(String rollNumber) {
        if (!isNotEmpty(rollNumber)) {
            return false;
        }
        return ROLL_NUMBER_PATTERN.matcher(rollNumber).matches();
    }

    /**
     * Validate semester (should be between 1 and 8)
     * 
     * @param semester Semester number
     * @return true if semester is valid, false otherwise
     */
    public static boolean isValidSemester(int semester) {
        return semester >= 1 && semester <= 8;
    }

    /**
     * Validate that a number is positive
     * 
     * @param number Number to validate
     * @return true if number is positive, false otherwise
     */
    public static boolean isPositive(int number) {
        return number > 0;
    }
}
