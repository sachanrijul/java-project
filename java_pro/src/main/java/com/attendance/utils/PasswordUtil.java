package com.attendance.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Utility class for password hashing and verification using SHA-256
 */
public class PasswordUtil {

    /**
     * Hash a password using SHA-256 algorithm
     * 
     * @param password Plain text password
     * @return Hashed password as hexadecimal string
     */
    public static String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes());
            StringBuilder hexString = new StringBuilder();

            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error hashing password: " + e.getMessage(), e);
        }
    }

    /**
     * Verify a password against a hash
     * 
     * @param password Plain text password to verify
     * @param hash     Stored password hash
     * @return true if password matches hash, false otherwise
     */
    public static boolean verifyPassword(String password, String hash) {
        String hashedPassword = hashPassword(password);
        return hashedPassword.equals(hash);
    }
}
