package com.gevernova.csvdatahandling;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class SorterCsv {

    private String filePath;

    public SorterCsv(String filePath) {
        this.filePath = filePath;
    }

    public void printTopPaidEmployees(int topN) {
        List<Employee> employeeList = new ArrayList<>();
        String csvDelimiter = ",";

        try (BufferedReader csvReader = new BufferedReader(new FileReader(filePath))) {
            // Read and discard header
            csvReader.readLine();

            String line;
            while ((line = csvReader.readLine()) != null) {
                String[] employeeDetails = line.split(csvDelimiter);
                if (employeeDetails.length >= 4) {
                    try {
                        String id = employeeDetails[0].trim();
                        String name = employeeDetails[1].trim();
                        String department = employeeDetails[2].trim();
                        double salary = Double.parseDouble(employeeDetails[3].trim());
                        employeeList.add(new Employee(id, name, department, salary));
                    } catch (NumberFormatException e) {
                        System.err.println("Warning: Invalid salary format in record: " + line);
                    }
                } else {
                    System.err.println("Warning: Skipping malformed record: " + line);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading CSV file: " + e.getMessage());
            return;
        }

        // Sort employees by salary in descending order
        Collections.sort(employeeList, Comparator.comparingDouble(Employee::getSalary).reversed());

        System.out.println("--- Top " + topN + " Highest-Paid Employees ---");
        for (int i = 0; i < Math.min(topN, employeeList.size()); i++) {
            Employee emp = employeeList.get(i);
            System.out.println("  Name: " + emp.getName() + ", Department: " + emp.getDepartment() + ", Salary: " + String.format("%.2f", emp.getSalary()));
        }
        if (employeeList.isEmpty()) {
            System.out.println("No employee records found to sort.");
        }
    }

    public static void main(String[] args) {
        // Use 'employees.csv' or 'employees_updated.csv'
        SorterCsv sorter = new SorterCsv("employees_updated.csv"); // Or "employees.csv"
        sorter.printTopPaidEmployees(5);
    }
}
