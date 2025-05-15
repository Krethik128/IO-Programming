package com.gevernova.fileHandling;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class ReadFileUsingReader {
    public static void main(String[] args) {
        String filePath="/Users/krethik/Desktop/sample.txt";
        try (FileReader reader = new FileReader(filePath)) {
            int word;
            while((word=reader.read())!=-1){
                System.out.print((char)word+" ");
            }
        }catch (FileNotFoundException e){
            throw new RuntimeException(e.getMessage());
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
