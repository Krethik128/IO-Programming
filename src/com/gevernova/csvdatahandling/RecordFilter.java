package com.gevernova.csvdatahandling;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class RecordFilter {

    private String filePath;

    public RecordFilter(String filePath) {
        this.filePath = filePath;
    }

    public void filterStudentsByMarks(int minMarks) {
        String line;
        String csvDelimiter = ",";

        System.out.println("--- Students with Marks > " + minMarks + " from " + filePath + " ---");

        try (BufferedReader csvReader = new BufferedReader(new FileReader(filePath))) {
            // Read the header
            String headerLine = csvReader.readLine();
            if (headerLine == null) {
                System.out.println("CSV file is empty.");
                return;
            }
            // Optional: Print header if needed for filtered output
            // System.out.println(headerLine);

            int recordNumber = 0;
            while ((line = csvReader.readLine()) != null) {
                String[] studentDetails = line.split(csvDelimiter);

                if (studentDetails.length >= 4) {
                    try {
                        int marks = Integer.parseInt(studentDetails[3].trim());
                        if (marks > minMarks) {
                            System.out.println("Record " + (++recordNumber) + ":");
                            System.out.println("  ID: " + studentDetails[0]);
                            System.out.println("  Name: " + studentDetails[1]);
                            System.out.println("  Age: " + studentDetails[2]);
                            System.out.println("  Marks: " + studentDetails[3]);
                            System.out.println("--------------------");
                        }
                    } catch (NumberFormatException e) {
                        System.err.println("Warning: Invalid marks format in record: " + line);
                    }
                } else {
                    System.err.println("Warning: Skipping malformed record: " + line);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading CSV file: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        // Use 'students.csv' created in problem 1.
        RecordFilter filter = new RecordFilter("students.csv");
        filter.filterStudentsByMarks(80);
    }
}
