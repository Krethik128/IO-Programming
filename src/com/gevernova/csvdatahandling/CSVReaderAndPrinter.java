package com.gevernova.csvdatahandling;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class CSVReaderAndPrinter {

    private String filePath;

    public CSVReaderAndPrinter(String filePath) {
        this.filePath = filePath;
    }

    public void readAndPrintStudentData() {
        String line;
        String csvDelimiter = ","; // Assuming comma as delimiter

        System.out.println("--- Student Records from " + filePath + " ---");

        try (BufferedReader csvReader = new BufferedReader(new FileReader(filePath))) {
            // Read the header line (and discard it if not needed for printing)
            csvReader.readLine(); // Assuming the first line is the header

            int recordNumber = 1;
            while ((line = csvReader.readLine()) != null) {
                String[] studentDetails = line.split(csvDelimiter);

                if (studentDetails.length >= 4) { // Ensure all expected fields are present
                    System.out.println("Record " + recordNumber + ":");
                    System.out.println("  ID: " + studentDetails[0]);
                    System.out.println("  Name: " + studentDetails[1]);
                    System.out.println("  Age: " + studentDetails[2]);
                    System.out.println("  Marks: " + studentDetails[3]);
                    System.out.println("--------------------");
                    recordNumber++;
                } else {
                    System.err.println("Warning: Skipping malformed record: " + line);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading CSV file: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        // To run this example, create a 'students.csv' file in your project root
        // with the following content:
        // ID,Name,Age,Marks
        // 1,Alice,20,85
        // 2,Bob,22,78
        // 3,Charlie,21,92
        // 4,David,23,65

        CSVReaderAndPrinter reader = new CSVReaderAndPrinter("students.csv");
        reader.readAndPrintStudentData();
    }
}