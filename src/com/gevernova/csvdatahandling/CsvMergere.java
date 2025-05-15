package com.gevernova.csvdatahandling;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class CsvMergere {

    private String students1FilePath;
    private String students2FilePath;
    private String outputFilePath;

    public CsvMergere(String students1FilePath, String students2FilePath, String outputFilePath) {
        this.students1FilePath = students1FilePath;
        this.students2FilePath = students2FilePath;
        this.outputFilePath = outputFilePath;
    }

    public void mergeStudentData() {
        String csvDelimiter = ",";
        Map<String, String[]> student1Data = new HashMap<>(); // ID -> [Name, Age]
        String header1 = "";
        String header2 = "";

        // Read students1.csv
        try (BufferedReader reader1 = new BufferedReader(new FileReader(students1FilePath))) {
            header1 = reader1.readLine(); // Read header
            String line;
            while ((line = reader1.readLine()) != null) {
                String[] parts = line.split(csvDelimiter);
                if (parts.length >= 3) { // ID, Name, Age
                    student1Data.put(parts[0].trim(), new String[]{parts[1].trim(), parts[2].trim()});
                } else {
                    System.err.println("Warning: Malformed record in " + students1FilePath + ": " + line);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading " + students1FilePath + ": " + e.getMessage());
            return;
        }

        // Merge and write to new file
        try (BufferedReader reader2 = new BufferedReader(new FileReader(students2FilePath));
             FileWriter writer = new FileWriter(outputFilePath)) {

            header2 = reader2.readLine(); // Read header of second file

            // Determine merged header
            String mergedHeader = "";
            if (header1 != null && header2 != null) {
                String[] headers1 = header1.split(csvDelimiter);
                String[] headers2 = header2.split(csvDelimiter);

                StringBuilder mergedHeaderBuilder = new StringBuilder();
                mergedHeaderBuilder.append(headers1[0]); // ID
                mergedHeaderBuilder.append(csvDelimiter).append(headers1[1]); // Name
                mergedHeaderBuilder.append(csvDelimiter).append(headers1[2]); // Age
                // Assuming students2.csv has ID, Marks, Grade. Skip ID from second header.
                for (int i = 1; i < headers2.length; i++) {
                    mergedHeaderBuilder.append(csvDelimiter).append(headers2[i]);
                }
                mergedHeader = mergedHeaderBuilder.toString();
            } else {
                mergedHeader = "ID,Name,Age,Marks,Grade"; // Fallback header
            }

            writer.append(mergedHeader);
            writer.append("\n");

            String line;
            while ((line = reader2.readLine()) != null) {
                String[] parts = line.split(csvDelimiter);
                if (parts.length >= 3) { // ID, Marks, Grade
                    String id = parts[0].trim();
                    String marks = parts[1].trim();
                    String grade = parts[2].trim();

                    String[] student1Info = student1Data.get(id);
                    if (student1Info != null) {
                        // Found a match, merge data
                        writer.append(id).append(csvDelimiter)
                                .append(student1Info[0]).append(csvDelimiter) // Name
                                .append(student1Info[1]).append(csvDelimiter) // Age
                                .append(marks).append(csvDelimiter)
                                .append(grade);
                        writer.append("\n");
                    } else {
                        System.err.println("Warning: ID " + id + " from " + students2FilePath + " not found in " + students1FilePath);
                    }
                } else {
                    System.err.println("Warning: Malformed record in " + students2FilePath + ": " + line);
                }
            }
            System.out.println("Successfully merged files to '" + outputFilePath + "'.");
        } catch (IOException e) {
            System.err.println("Error merging CSV files: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        // Create students1.csv:
        // ID,Name,Age
        // 1,Alice,20
        // 2,Bob,22
        // 3,Charlie,21

        // Create students2.csv:
        // ID,Marks,Grade
        // 1,85,A
        // 2,78,B
        // 3,92,A+
        // 4,David,65,C (This record won't be merged as ID 4 is not in students1.csv)

        CsvMergere merger = new CsvMergere("students1.csv", "students2.csv", "merged_students.csv");
        merger.mergeStudentData();
    }
}
