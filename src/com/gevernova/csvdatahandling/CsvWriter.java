package com.gevernova.csvdatahandling;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;

class Employee {
    String id;
    String name;
    String department;
    double salary;

    public Employee(String id, String name, String department, double salary) {
        this.id = id;
        this.name = name;
        this.department = department;
        this.salary = salary;
    }

    // Getters
    public String getId() { return id; }
    public String getName() { return name; }
    public String getDepartment() { return department; }
    public double getSalary() { return salary; }

    @Override
    public String toString() {
        return id + "," + name + "," + department + "," + salary;
    }
}

public class CsvWriter {

    private String filePath;
    private static final String CSV_HEADER = "ID,Name,Department,Salary";

    public CsvWriter(String filePath) {
        this.filePath = filePath;
    }

    public void writeEmployeeData(List<Employee> employeeRecords) {
        try (FileWriter csvWriter = new FileWriter(filePath)) {
            csvWriter.append(CSV_HEADER);
            csvWriter.append("\n");

            for (Employee employee : employeeRecords) {
                csvWriter.append(employee.toString());
                csvWriter.append("\n");
            }
            System.out.println("Successfully wrote " + employeeRecords.size() + " employee records to '" + filePath + "'.");
        } catch (IOException e) {
            System.err.println("Error writing to CSV file: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        List<Employee> employees = new ArrayList<>();
        employees.add(new Employee("E001", "Alice Smith", "HR", 60000.0));
        employees.add(new Employee("E002", "Bob Johnson", "IT", 75000.0));
        employees.add(new Employee("E003", "Charlie Brown", "Finance", 62000.0));
        employees.add(new Employee("E004", "Diana Prince", "Marketing", 68000.0));
        employees.add(new Employee("E005", "Eve Adams", "IT", 80000.0));

        CsvWriter writer = new CsvWriter("employees.csv");
        writer.writeEmployeeData(employees);
    }
}
