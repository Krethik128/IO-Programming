package com.gevernova.csvdatahandling;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;

public class CsvMain {
    public static void main(String[] args) {
        // The file you created with all the data
        String filePath = "/Users/krethik/Desktop/Java-IO Programming/src/com/gevernova/csvdatahandling/sample_data.csv";

        // 1. Read CSV in chunks (for example, 3 rows at a time)
        // This reads and processes the file in small parts, useful for large files.
        Csv.readCsvInChunks(filePath, 3);

        // 2. Detect duplicate records by ID
        // This will print any rows that have the same ID value.
        Csv.detectDuplicatesById(filePath);

        // 3. Count the number of data rows (excluding the header)
        // This gives you the total number of records in your file.
        int rowCount = Csv.countDataRows(filePath);
        System.out.println("Number of data records: " + rowCount);

        // 4. Read and print student data (ID, Name, Age, Marks)
        // This prints each student's main details from the CSV.
        Csv.readAndPrintStudentData(filePath);

        // 5. Write employee data to a new CSV file
        // Here, we extract employees from your sample data.
        List<Csv.Employee> employees = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String header = br.readLine(); // skip header
            String line;
            while ((line = br.readLine()) != null) {
                String[] fields = line.split(",");
                if (fields.length >= 8) { // ID, Name, Age, Marks, Email, Phone, Department, Salary
                    String id = fields[0];
                    String name = fields[1];
                    String department = fields[6];
                    double salary = Double.parseDouble(fields[7]);
                    employees.add(new Csv.Employee(id, name, department, salary));
                }
            }
        } catch (Exception e) {
            System.err.println("Error extracting employees: " + e.getMessage());
        }
        Csv.writeEmployeeData("/Users/krethik/Desktop/Java-IO Programming/src/com/gevernova/csvdatahandling/outputcsv/employees_out.csv", employees);

        // 6. Filter students by marks > 80
        // This prints all students who scored more than 80 marks.
        Csv.filterStudentsByMarks(filePath, 80);

        // 7. Increase IT department salaries by 10% and save to a new file
        // This finds all IT employees and increases their salary by 10%.
        Csv.increaseITSalary("/Users/krethik/Desktop/Java-IO Programming/src/com/gevernova/csvdatahandling/outputcsv/employees_out.csv",
                "/Users/krethik/Desktop/Java-IO Programming/src/com/gevernova/csvdatahandling/outputcsv/employees_updated.csv", 10.0);

        // 8. Convert CSV rows to Student objects and print them
        // This reads student info from the CSV and creates Java objects.
        List<Csv.Student> students = Csv.convertToStudentObjects(filePath);
        for (Csv.Student student : students) {
            System.out.println(student);
        }

        // 9. Search for an employee by name
        // This searches for "Bob Johnson" and prints their details if found.
        Csv.searchEmployeeByName("/Users/krethik/Desktop/Java-IO Programming/src/com/gevernova/csvdatahandling/outputcsv/employees_out.csv", "Bob Johnson");

        // 10. Print top 3 highest-paid employees
        // This sorts employees by salary and prints the top 3.
        Csv.printTopPaidEmployees("/Users/krethik/Desktop/Java-IO Programming/src/com/gevernova/csvdatahandling/outputcsv/employees_updated.csv", 3);

        // 11. Validate email and phone fields in the CSV
        // This checks each row for valid email and 10-digit phone number.
        Csv.validateRecords(filePath);

        // 12. Merge student data or generate employee CSV from database
         Csv.mergeStudentData("/Users/krethik/Desktop/Java-IO Programming/src/com/gevernova/csvdatahandling/students1.csv",
                 "/Users/krethik/Desktop/Java-IO Programming/src/com/gevernova/csvdatahandling/students2.csv",
                 "/Users/krethik/Desktop/Java-IO Programming/src/com/gevernova/csvdatahandling/outputcsv/merged_students.csv");



    }
}
