package com.gevernova.csvdatahandling;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class RecordSearcher {

    private String filePath;

    public RecordSearcher(String filePath) {
        this.filePath = filePath;
    }

    public void searchEmployeeByName(String employeeName) {
        String line;
        String csvDelimiter = ",";
        boolean found = false;

        System.out.println("--- Searching for employee '" + employeeName + "' in " + filePath + " ---");

        try (BufferedReader csvReader = new BufferedReader(new FileReader(filePath))) {
            // Read the header
            csvReader.readLine();

            while ((line = csvReader.readLine()) != null) {
                String[] employeeDetails = line.split(csvDelimiter);

                if (employeeDetails.length >= 4) {
                    String currentName = employeeDetails[1].trim();
                    if (currentName.equalsIgnoreCase(employeeName.trim())) {
                        System.out.println("Employee Found:");
                        System.out.println("  Name: " + employeeDetails[1]);
                        System.out.println("  Department: " + employeeDetails[2]);
                        System.out.println("  Salary: " + employeeDetails[3]);
                        found = true;
                        break; // Assuming unique names or first match is sufficient
                    }
                }
            }
            if (!found) {
                System.out.println("Employee '" + employeeName + "' not found.");
            }
        } catch (IOException e) {
            System.err.println("Error reading CSV file: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        // Use 'employees.csv' created in problem 2.
        RecordSearcher searcher = new RecordSearcher("employees.csv");
        searcher.searchEmployeeByName("Bob Johnson");
        searcher.searchEmployeeByName("Unknown Person");
    }
}
