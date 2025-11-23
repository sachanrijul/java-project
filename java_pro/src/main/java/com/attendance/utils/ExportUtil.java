package com.attendance.utils;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

/**
 * Utility class for exporting data to CSV and PDF formats
 */
public class ExportUtil {

    /**
     * Export data to CSV file
     * 
     * @param headers  Column headers
     * @param data     Data rows (each row is an array of strings)
     * @param filename Output filename
     * @return true if export successful, false otherwise
     */
    public static boolean exportToCSV(String[] headers, List<String[]> data, String filename) {
        try (FileWriter writer = new FileWriter(filename)) {
            // Write headers
            writer.append(String.join(",", headers));
            writer.append("\n");

            // Write data rows
            for (String[] row : data) {
                writer.append(String.join(",", row));
                writer.append("\n");
            }

            return true;
        } catch (IOException e) {
            System.err.println("Error exporting to CSV: " + e.getMessage());
            return false;
        }
    }

    /**
     * Export data to PDF file
     * Note: This is a placeholder. Full implementation requires iText library.
     * For now, it creates a simple text file.
     * 
     * @param headers  Column headers
     * @param data     Data rows
     * @param filename Output filename
     * @param title    Report title
     * @return true if export successful, false otherwise
     */
    public static boolean exportToPDF(String[] headers, List<String[]> data, String filename, String title) {
        try (FileWriter writer = new FileWriter(filename)) {
            // Write title
            writer.append(title + "\n");
            writer.append("=".repeat(title.length()) + "\n\n");

            // Write headers
            writer.append(String.join(" | ", headers));
            writer.append("\n");
            writer.append("-".repeat(80) + "\n");

            // Write data rows
            for (String[] row : data) {
                writer.append(String.join(" | ", row));
                writer.append("\n");
            }

            return true;
        } catch (IOException e) {
            System.err.println("Error exporting to PDF: " + e.getMessage());
            return false;
        }
    }

    /**
     * Escape CSV special characters
     * 
     * @param value String value to escape
     * @return Escaped string
     */
    private static String escapeCSV(String value) {
        if (value == null) {
            return "";
        }
        if (value.contains(",") || value.contains("\"") || value.contains("\n")) {
            return "\"" + value.replace("\"", "\"\"") + "\"";
        }
        return value;
    }
}
