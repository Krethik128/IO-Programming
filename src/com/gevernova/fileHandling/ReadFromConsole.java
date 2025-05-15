package com.gevernova.fileHandling;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

public class ReadFromConsole {
    public static void main(String[] args) throws IOException {
        String path="/Users/krethik/Desktop/sample.txt";
        try(BufferedReader reader=new BufferedReader(new InputStreamReader(System.in));
            FileWriter writer=new FileWriter(path,true)){

            System.out.print("Enter your name: ");
            String name = reader.readLine();

            System.out.print("Enter your age: ");
            String age = reader.readLine();

            System.out.print("Enter your favorite programming language: ");
            String language = reader.readLine();

            writer.write("Name: " + name + "\n");
            writer.write("Age: " + age + "\n");
            writer.write("Favorite Language: " + language + "\n");
            writer.write("-------------------------\n");

        }catch (IOException e) {
            System.err.println("An error occurred while reading input or writing to the file: " + e.getMessage());
        }


    }
}
