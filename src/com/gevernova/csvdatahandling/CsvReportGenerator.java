package com.gevernova.csvdatahandling;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.util.Arrays;

public class CsvReportGenerator {
    public static void main(String[] args) {
        String outputCsvPath = "employee_report.csv";
        generateEmployeeCsv(outputCsvPath);
    }

    public static void generateEmployeeCsv(String filePath) {
        String url = "jdbc:mysql://localhost:3306/company_db";
        String user = "root";
        String password = "password";

        String query = "SELECT employee_id, name, department, salary FROM employees";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query);
             FileWriter writer = new FileWriter(filePath)) {

            // Write header
            writer.append("Employee ID,Name,Department,Salary\n");

            while (rs.next()) {
                writer.append(rs.getInt("employee_id") + ",")
                        .append(rs.getString("name") + ",")
                        .append(rs.getString("department") + ",")
                        .append(String.valueOf(rs.getDouble("salary")))
                        .append("\n");
            }
            System.out.println("CSV report generated successfully.");

        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }
}

