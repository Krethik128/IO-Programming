package com.gevernova.csvdatahandling;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class CsvChunkReader {
    private static final int CHUNK_SIZE = 100;

    public static void main(String[] args) {
        // Create a large test CSV file with 1000 dummy rows for demonstration
        String filePath = "large_data.csv";
        createSampleCsvFile(filePath, 1000);

        readCsvInChunks(filePath);
    }

    // Reads the CSV file 100 lines at a time and processes each chunk
    public static void readCsvInChunks(String filePath) {
        int recordCount = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            List<String> chunk = new ArrayList<>();
            String line;

            while ((line = reader.readLine()) != null) {
                chunk.add(line);
                if (chunk.size() == CHUNK_SIZE) {
                    processChunk(chunk);
                    recordCount += chunk.size();
                    System.out.println("Processed records: " + recordCount);
                    chunk.clear(); // Clear for next batch
                }
            }

            // Process any remaining lines
            if (!chunk.isEmpty()) {
                processChunk(chunk);
                recordCount += chunk.size();
                System.out.println("Processed records: " + recordCount);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Example chunk processor: prints first column (e.g., ID) of each line
    private static void processChunk(List<String> lines) {
        for (String line : lines) {
            String[] parts = line.split(",");
            if (parts.length > 0) {
                System.out.println("ID: " + parts[0]); // Assumes the first field is an ID
            }
        }
    }

    // Helper method to create a test CSV file
    private static void createSampleCsvFile(String filePath, int totalRows) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write("ID,Name,Email\n"); // Header
            for (int i = 1; i <= totalRows; i++) {
                writer.write(i + ",Name" + i + ",user" + i + "@example.com\n");
            }
            System.out.println("Sample CSV file created with " + totalRows + " rows.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
