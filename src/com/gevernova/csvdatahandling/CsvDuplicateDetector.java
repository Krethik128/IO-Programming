package com.gevernova.csvdatahandling;

import java.io.*;
import java.util.*;

public class CsvDuplicateDetector {
    public static void main(String[] args) {
        String filePath = "data_with_duplicates.csv";
        detectDuplicatesById(filePath);
    }

    public static void detectDuplicatesById(String filePath) {
        Map<String, List<String>> idToRecords = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            // Assuming first line is header
            String header = reader.readLine();
            while ((line = reader.readLine()) != null) {
                String[] fields = line.split(",");
                String id = fields[0];
                idToRecords.computeIfAbsent(id, k -> new ArrayList<>()).add(line);
            }

            System.out.println("Duplicate records:");
            for (Map.Entry<String, List<String>> entry : idToRecords.entrySet()) {
                if (entry.getValue().size() > 1) {
                    for (String record : entry.getValue()) {
                        System.out.println(record);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
