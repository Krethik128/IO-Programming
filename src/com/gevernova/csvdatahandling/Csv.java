package com.gevernova.csvdatahandling;

import java.io.*;
import java.sql.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Csv {

    // --- 1. Read CSV in Chunks ---
    public static void readCsvInChunks(String filePath, int chunkSize) {
        int recordCount = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            List<String> chunk = new ArrayList<>();
            String line;
            while ((line = reader.readLine()) != null) {
                chunk.add(line);
                if (chunk.size() == chunkSize) {
                    processChunk(chunk);
                    recordCount += chunk.size();
                    System.out.println("Processed records: " + recordCount);
                    chunk.clear();
                }
            }
            if (!chunk.isEmpty()) {
                processChunk(chunk);
                recordCount += chunk.size();
                System.out.println("Processed records: " + recordCount);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void processChunk(List<String> lines) {
        for (String line : lines) {
            String[] parts = line.split(",");
            if (parts.length > 0) {
                System.out.println("ID: " + parts[0]);
            }
        }
    }

    public static void createSampleCsvFile(String filePath, int totalRows) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write("ID,Name,Email,Phone\n");
            for (int i = 1; i <= totalRows; i++) {
                writer.write(i + ",Name" + i + ",user" + i + "@example.com,1234567890\n");
            }
            System.out.println("Sample CSV file created with " + totalRows + " rows.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // --- 2. Detect Duplicates by ID ---
    public static void detectDuplicatesById(String filePath) {
        Map<String, List<String>> idToRecords = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String header = reader.readLine();
            String line;
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

    // --- 3. Merge Student Data ---
    public static void mergeStudentData(String students1FilePath, String students2FilePath, String outputFilePath) {
        String csvDelimiter = ",";
        Map<String, String[]> student1Data = new HashMap<>();
        String header1 = "";
        String header2 = "";

        try (BufferedReader reader1 = new BufferedReader(new FileReader(students1FilePath))) {
            header1 = reader1.readLine();
            String line;
            while ((line = reader1.readLine()) != null) {
                String[] parts = line.split(csvDelimiter);
                if (parts.length >= 3) {
                    student1Data.put(parts[0].trim(), new String[]{parts[1].trim(), parts[2].trim()});
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading " + students1FilePath + ": " + e.getMessage());
            return;
        }

        try (BufferedReader reader2 = new BufferedReader(new FileReader(students2FilePath));
             FileWriter writer = new FileWriter(outputFilePath)) {
            header2 = reader2.readLine();
            String[] headers1 = header1.split(csvDelimiter);
            String[] headers2 = header2.split(csvDelimiter);
            StringBuilder mergedHeaderBuilder = new StringBuilder();
            mergedHeaderBuilder.append(headers1[0]).append(csvDelimiter)
                    .append(headers1[1]).append(csvDelimiter)
                    .append(headers1[2]);
            for (int i = 1; i < headers2.length; i++) {
                mergedHeaderBuilder.append(csvDelimiter).append(headers2[i]);
            }
            writer.append(mergedHeaderBuilder.toString()).append("\n");

            String line;
            while ((line = reader2.readLine()) != null) {
                String[] parts = line.split(csvDelimiter);
                if (parts.length >= 3) {
                    String id = parts[0].trim();
                    String[] student1Info = student1Data.get(id);
                    if (student1Info != null) {
                        writer.append(id).append(csvDelimiter)
                                .append(student1Info[0]).append(csvDelimiter)
                                .append(student1Info[1]).append(csvDelimiter)
                                .append(parts[1].trim()).append(csvDelimiter)
                                .append(parts[2].trim()).append("\n");
                    }
                }
            }
            System.out.println("Successfully merged files to '" + outputFilePath + "'.");
        } catch (IOException e) {
            System.err.println("Error merging CSV files: " + e.getMessage());
        }
    }

    // --- 4. Count Data Rows ---
    public static int countDataRows(String filePath) {
        int rowCount = 0;
        try (BufferedReader csvReader = new BufferedReader(new FileReader(filePath))) {
            csvReader.readLine(); // Skip header
            while (csvReader.readLine() != null) {
                rowCount++;
            }
            System.out.println("Successfully counted rows in '" + filePath + "'.");
        } catch (IOException e) {
            System.err.println("Error reading CSV file: " + e.getMessage());
            return -1;
        }
        return rowCount;
    }

    // --- 5. Read and Print Student Data ---
    public static void readAndPrintStudentData(String filePath) {
        String line;
        String csvDelimiter = ",";
        System.out.println("--- Student Records from " + filePath + " ---");
        try (BufferedReader csvReader = new BufferedReader(new FileReader(filePath))) {
            csvReader.readLine(); // Skip header
            int recordNumber = 1;
            while ((line = csvReader.readLine()) != null) {
                String[] studentDetails = line.split(csvDelimiter);
                if (studentDetails.length >= 4) {
                    System.out.println("Record " + recordNumber + ":");
                    System.out.println(" ID: " + studentDetails[0]);
                    System.out.println(" Name: " + studentDetails[1]);
                    System.out.println(" Age: " + studentDetails[2]);
                    System.out.println(" Marks: " + studentDetails[3]);
                    System.out.println("--------------------");
                    recordNumber++;
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading CSV file: " + e.getMessage());
        }
    }

    // --- 6. Write Employee Data ---
    public static class Employee {
        String id, name, department;
        double salary;
        public Employee(String id, String name, String department, double salary) {
            this.id = id;
            this.name = name;
            this.department = department;
            this.salary = salary;
        }
        public String getName() { return name; }
        public String getDepartment() { return department; }
        public double getSalary() { return salary; }
        @Override
        public String toString() {
            return id + "," + name + "," + department + "," + salary;
        }
    }

    public static void writeEmployeeData(String filePath, List<Employee> employeeRecords) {
        String CSV_HEADER = "ID,Name,Department,Salary";
        try (FileWriter csvWriter = new FileWriter(filePath)) {
            csvWriter.append(CSV_HEADER).append("\n");
            for (Employee employee : employeeRecords) {
                csvWriter.append(employee.toString()).append("\n");
            }
            System.out.println("Successfully wrote " + employeeRecords.size() + " employee records to '" + filePath + "'.");
        } catch (IOException e) {
            System.err.println("Error writing to CSV file: " + e.getMessage());
        }
    }

    // --- 7. Filter Students by Marks (from RecordFilter.java) ---
    public static void filterStudentsByMarks(String filePath, int minMarks) {
        String line;
        String csvDelimiter = ",";
        System.out.println("--- Students with Marks > " + minMarks + " from " + filePath + " ---");
        try (BufferedReader csvReader = new BufferedReader(new FileReader(filePath))) {
            String headerLine = csvReader.readLine();
            if (headerLine == null) {
                System.out.println("CSV file is empty.");
                return;
            }
            int recordNumber = 0;
            while ((line = csvReader.readLine()) != null) {
                String[] studentDetails = line.split(csvDelimiter);
                if (studentDetails.length >= 4) {
                    try {
                        int marks = Integer.parseInt(studentDetails[3].trim());
                        if (marks > minMarks) {
                            System.out.println("Record " + (++recordNumber) + ":");
                            System.out.println(" ID: " + studentDetails[0]);
                            System.out.println(" Name: " + studentDetails[1]);
                            System.out.println(" Age: " + studentDetails[2]);
                            System.out.println(" Marks: " + studentDetails[3]);
                            System.out.println("--------------------");
                        }
                    } catch (NumberFormatException e) {
                        System.err.println("Warning: Invalid marks format in record: " + line);
                    }
                } else {
                    System.err.println("Warning: Skipping malformed record: " + line);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading CSV file: " + e.getMessage());
        }
    }

    // --- 8. Modify CSV (Increase IT Salaries) ---
    public static void increaseITSalary(String inputFilePath, String outputFilePath, double percentageIncrease) {
        List<String[]> allRecords = new ArrayList<>();
        String csvDelimiter = ",";
        try (BufferedReader csvReader = new BufferedReader(new FileReader(inputFilePath))) {
            String headerLine = csvReader.readLine();
            if (headerLine == null) {
                System.out.println("Input CSV file is empty.");
                return;
            }
            allRecords.add(headerLine.split(csvDelimiter));
            String line;
            while ((line = csvReader.readLine()) != null) {
                String[] record = line.split(csvDelimiter);
                if (record.length >= 4) {
                    String department = record[2].trim();
                    if (department.equalsIgnoreCase("IT")) {
                        try {
                            double currentSalary = Double.parseDouble(record[3].trim());
                            double newSalary = currentSalary * (1 + percentageIncrease / 100.0);
                            record[3] = String.format("%.2f", newSalary);
                            System.out.println("Updated IT employee: " + record[1] + ", Old Salary: " + currentSalary + ", New Salary: " + newSalary);
                        } catch (NumberFormatException ignored) {}
                    }
                    allRecords.add(record);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading input CSV file: " + e.getMessage());
            return;
        }
        try (FileWriter csvWriter = new FileWriter(outputFilePath)) {
            for (String[] record : allRecords) {
                csvWriter.append(String.join(csvDelimiter, record)).append("\n");
            }
            System.out.println("Successfully updated salaries and saved to '" + outputFilePath + "'.");
        } catch (IOException e) {
            System.err.println("Error writing to output CSV file: " + e.getMessage());
        }
    }

    // --- 9. Convert CSV to Java Objects ---
    public static class Student {
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
        @Override
        public String toString() {
            return "Student [ID=" + id + ", Name=" + name + ", Age=" + age + ", Marks=" + marks + "]";
        }
    }

    public static List<Student> convertToStudentObjects(String filePath) {
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
                    } catch (NumberFormatException ignored) {}
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading CSV file: " + e.getMessage());
        }
        return studentList;
    }

    // --- 10. Search Employee by Name (from RecordSearcher.java) ---
    public static void searchEmployeeByName(String filePath, String employeeName) {
        String line;
        String csvDelimiter = ",";
        boolean found = false;
        System.out.println("--- Searching for employee '" + employeeName + "' in " + filePath + " ---");
        try (BufferedReader csvReader = new BufferedReader(new FileReader(filePath))) {
            csvReader.readLine(); // Read the header
            while ((line = csvReader.readLine()) != null) {
                String[] employeeDetails = line.split(csvDelimiter);
                if (employeeDetails.length >= 4) {
                    String currentName = employeeDetails[1].trim();
                    if (currentName.equalsIgnoreCase(employeeName.trim())) {
                        System.out.println("Employee Found:");
                        System.out.println(" Name: " + employeeDetails[1]);
                        System.out.println(" Department: " + employeeDetails[2]);
                        System.out.println(" Salary: " + employeeDetails[3]);
                        found = true;
                        break;
                    }
                }
            }
            if (!found) {
                System.out.println("Employee '" + employeeName + "' not found.");
            }
        } catch (IOException e) {
            System.err.println("Error reading CSV file: " + e.getMessage());
        }
    }

    // --- 11. Print Top Paid Employees (from SorterCsv.java) ---
    public static void printTopPaidEmployees(String filePath, int topN) {
        List<Employee> employeeList = new ArrayList<>();
        String csvDelimiter = ",";
        try (BufferedReader csvReader = new BufferedReader(new FileReader(filePath))) {
            csvReader.readLine(); // Read and discard header
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
            // Sort employees by salary in descending order
            employeeList.sort(Comparator.comparingDouble(Employee::getSalary).reversed());
            System.out.println("--- Top " + topN + " Highest-Paid Employees ---");
            for (int i = 0; i < Math.min(topN, employeeList.size()); i++) {
                Employee emp = employeeList.get(i);
                System.out.println(" Name: " + emp.getName() + ", Department: " + emp.getDepartment() + ", Salary: " + String.format("%.2f", emp.getSalary()));
            }
            if (employeeList.isEmpty()) {
                System.out.println("No employee records found to sort.");
            }
        } catch (IOException e) {
            System.err.println("Error reading CSV file: " + e.getMessage());
        }
    }

    // --- 12. Validate Records (from Validator.java) ---
    public static void validateRecords(String filePath) {
        String line;
        String csvDelimiter = ",";
        int rowNumber = 0;
        System.out.println("--- Validating records in " + filePath + " ---");
        // Basic email regex (can be more comprehensive)
        final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);
        // Phone number regex for exactly 10 digits
        final String PHONE_REGEX = "^\\d{10}$";
        final Pattern PHONE_PATTERN = Pattern.compile(PHONE_REGEX);

        try (BufferedReader csvReader = new BufferedReader(new FileReader(filePath))) {
            String headerLine = csvReader.readLine(); // Read header
            if (headerLine == null) {
                System.out.println("CSV file is empty.");
                return;
            }
            // Assuming header: ID, Name, Email, Phone, Other
            // For this example, let's assume Email is at index 2, Phone at index 3
            int emailIndex = -1;
            int phoneIndex = -1;
            String[] headers = headerLine.split(csvDelimiter);
            for(int i = 0; i < headers.length; i++) {
                if (headers[i].trim().equalsIgnoreCase("Email")) {
                    emailIndex = i;
                } else if (headers[i].trim().equalsIgnoreCase("Phone")) {
                    phoneIndex = i;
                }
            }
            if (emailIndex == -1 || phoneIndex == -1) {
                System.err.println("Error: 'Email' or 'Phone' column not found in header. Please ensure your CSV has these headers.");
                System.out.println("Header found: " + headerLine);
                return;
            }
            while ((line = csvReader.readLine()) != null) {
                rowNumber++;
                String[] recordFields = line.split(csvDelimiter);
                boolean isValidRow = true;
                StringBuilder errorMessage = new StringBuilder("Invalid row " + rowNumber + ": " + line + " - Errors: ");
                if (recordFields.length <= Math.max(emailIndex, phoneIndex)) {
                    errorMessage.append("Insufficient columns. ");
                    isValidRow = false;
                } else {
                    // Validate Email
                    String email = recordFields[emailIndex].trim();
                    Matcher emailMatcher = EMAIL_PATTERN.matcher(email);
                    if (!emailMatcher.matches()) {
                        errorMessage.append("Invalid Email format ('").append(email).append("'). ");
                        isValidRow = false;
                    }
                    // Validate Phone Number
                    String phoneNumber = recordFields[phoneIndex].trim();
                    Matcher phoneMatcher = PHONE_PATTERN.matcher(phoneNumber);
                    if (!phoneMatcher.matches()) {
                        errorMessage.append("Invalid Phone Number format ('").append(phoneNumber).append("') - must be 10 digits. ");
                        isValidRow = false;
                    }
                }
                if (!isValidRow) {
                    System.out.println(errorMessage.toString());
                }
            }
            System.out.println("--- Validation complete ---");
        } catch (IOException e) {
            System.err.println("Error reading CSV file: " + e.getMessage());
        }
    }
}
