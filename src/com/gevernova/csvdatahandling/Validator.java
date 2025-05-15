package com.gevernova.csvdatahandling;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validator {

    private String filePath;
    // Basic email regex (can be more comprehensive)
    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);
    // Phone number regex for exactly 10 digits
    private static final String PHONE_REGEX = "^\\d{10}$";
    private static final Pattern PHONE_PATTERN = Pattern.compile(PHONE_REGEX);

    public Validator(String filePath) {
        this.filePath = filePath;
    }

    public void validateRecords() {
        String line;
        String csvDelimiter = ",";
        int rowNumber = 0;

        System.out.println("--- Validating records in " + filePath + " ---");

        try (BufferedReader csvReader = new BufferedReader(new FileReader(filePath))) {
            String headerLine = csvReader.readLine(); // Read header
            if (headerLine == null) {
                System.out.println("CSV file is empty.");
                return;
            }
            // Assuming header: ID,Name,Email,Phone,Other
            // For this example, let's assume Email is at index 2, Phone at index 3
            // You might need to adjust indices based on your actual CSV structure.
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
                } else {
                    // System.out.println("Row " + rowNumber + " is valid."); // Uncomment to see valid rows
                }
            }
            System.out.println("--- Validation complete ---");
        } catch (IOException e) {
            System.err.println("Error reading CSV file: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        // ID,Name,Email,Phone,City
        // 1,John Doe,john.doe@example.com,1234567890,New York
        // 2,Jane Smith,jane.smith@invalid,123,Los Angeles
        // 3,Peter Jones,peter@example.com,9876543210,Chicago

        Validator validator = new Validator("users.csv");
        validator.validateRecords();
    }
}
