package com.gevernova.csvdatahandling;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class CsvRowCount {

    private String filePath;

    public CsvRowCount(String filePath) {
        this.filePath = filePath;
    }

    public int countDataRows() {
        int rowCount = 0;
        try (BufferedReader csvReader = new BufferedReader(new FileReader(filePath))) {
            // Read and discard the header line
            csvReader.readLine();
            while (csvReader.readLine() != null) {
                rowCount++;
            }
            System.out.println("Successfully counted rows in '" + filePath + "'.");
        } catch (IOException e) {
            System.err.println("Error reading CSV file: " + e.getMessage());
            return -1; // Indicate an error
        }
        return rowCount;
    }

    public static void main(String[] args) {
        // Example with 'students.csv':
        // ID, Name, Age, Marks
        // 1,Alice,20,85

        CsvRowCount rowCounter = new CsvRowCount("students.csv");
        int numberOfRecords = rowCounter.countDataRows();

        if (numberOfRecords != -1) {
            System.out.println("Number of data records: " + numberOfRecords);
        }
    }
}
