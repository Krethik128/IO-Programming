package com.gevernova.fileHandling;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class ReadFile {
    public static void main(String[] args) {
        String filePath="/Users/krethik/Desktop/sample.txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while((line=reader.readLine())!=null){
                if(line.toLowerCase().contains("error")){
                    System.out.println(line);
                }
            }
        }catch (FileNotFoundException e){
            throw new RuntimeException(e.getMessage());
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
