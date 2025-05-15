package com.gevernova.csvdatahandling;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ModifyCsv {

    private String inputFilePath;
    private String outputFilePath;

    public ModifyCsv(String inputFilePath, String outputFilePath) {
        this.inputFilePath = inputFilePath;
        this.outputFilePath = outputFilePath;
    }

    public void increaseITSalary(double percentageIncrease) {
        List<String[]> allRecords = new ArrayList<>();
        String csvDelimiter = ",";

        try (BufferedReader csvReader = new BufferedReader(new FileReader(inputFilePath))) {
            String headerLine = csvReader.readLine();
            if (headerLine == null) {
                System.out.println("Input CSV file is empty.");
                return;
            }
            allRecords.add(headerLine.split(csvDelimiter)); // Add header

            String line;
            while ((line = csvReader.readLine()) != null) {
                String[] record = line.split(csvDelimiter);
                if (record.length >= 4) {
                    String department = record[2].trim();
                    if (department.equalsIgnoreCase("IT")) {
                        try {
                            double currentSalary = Double.parseDouble(record[3].trim());
                            double newSalary = currentSalary * (1 + percentageIncrease / 100.0);
                            record[3] = String.format("%.2f", newSalary); // Format to 2 decimal places
                            System.out.println("Updated IT employee: " + record[1] + ", Old Salary: " + currentSalary + ", New Salary: " + newSalary);
                        } catch (NumberFormatException e) {
                            System.err.println("Warning: Invalid salary format for IT employee: " + record[1]);
                        }
                    }
                }
                allRecords.add(record);
            }
        } catch (IOException e) {
            System.err.println("Error reading input CSV file: " + e.getMessage());
            return;
        }

        // Write the updated records to the new CSV file
        try (FileWriter csvWriter = new FileWriter(outputFilePath)) {
            for (String[] record : allRecords) {
                csvWriter.append(String.join(csvDelimiter, record));
                csvWriter.append("\n");
            }
            System.out.println("Successfully updated salaries and saved to '" + outputFilePath + "'.");
        } catch (IOException e) {
            System.err.println("Error writing to output CSV file: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        ModifyCsv modifier = new ModifyCsv("employees.csv", "employees_updated.csv");
        modifier.increaseITSalary(10.0); // Increase IT salaries by 10%
    }
}