package com.gevernova.csvdatahandling;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

class Student {
    private int id;
    private String name;
    private int age;
    private int marks;

    public Student(int id, String name, int age, int marks) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.marks = marks;
    }

    // Getters
    public int getId() { return id; }
    public String getName() { return name; }
    public int getAge() { return age; }
    public int getMarks() { return marks; }

    @Override
    public String toString() {
        return "Student [ID=" + id + ", Name=" + name + ", Age=" + age + ", Marks=" + marks + "]";
    }
}

public class CsvToJavaObjectConverter {

    private String filePath;

    public CsvToJavaObjectConverter(String filePath) {
        this.filePath = filePath;
    }

    public List<Student> convertToStudentObjects() {
        List<Student> studentList = new ArrayList<>();
        String line;
        String csvDelimiter = ",";

        try (BufferedReader csvReader = new BufferedReader(new FileReader(filePath))) {
            csvReader.readLine(); // Skip header

            while ((line = csvReader.readLine()) != null) {
                String[] studentDetails = line.split(csvDelimiter);
                if (studentDetails.length >= 4) {
                    try {
                        int id = Integer.parseInt(studentDetails[0].trim());
                        String name = studentDetails[1].trim();
                        int age = Integer.parseInt(studentDetails[2].trim());
                        int marks = Integer.parseInt(studentDetails[3].trim());
                        studentList.add(new Student(id, name, age, marks));
                    } catch (NumberFormatException e) {
                        System.err.println("Warning: Invalid number format in record: " + line);
                    }
                } else {
                    System.err.println("Warning: Skipping malformed record: " + line);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading CSV file: " + e.getMessage());
        }
        return studentList;
    }

    public static void main(String[] args) {
        // Use 'students.csv' created in problem 1.
        CsvToJavaObjectConverter converter = new CsvToJavaObjectConverter("students.csv");
        List<Student> students = converter.convertToStudentObjects();

        System.out.println("--- Converted Student Objects ---");
        for (Student student : students) {
            System.out.println(student);
        }
        if (students.isEmpty()) {
            System.out.println("No student objects created.");
        }
    }
}
