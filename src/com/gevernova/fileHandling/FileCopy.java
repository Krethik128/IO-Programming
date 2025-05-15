package com.gevernova.fileHandling;

import java.io.*;

public class FileCopy {
    public static void main(String[] args) {
        File file=new File("/Users/krethik/Downloads/HMKSBE.pdf");
        File newFile=new File("/Users/krethik/Desktop/Copied.pdf");
        try (FileInputStream fileInputStreams = new FileInputStream(file); //data in file are in the form of binary
             FileOutputStream fileOutputStreams = new FileOutputStream(newFile)) {

            int length;
            byte[] buffer=new byte[1024];
            while((length=fileInputStreams.read(buffer)) !=-1){
                System.out.println(length);
                    fileOutputStreams.write(buffer,0,length);
            }

            System.out.println("File copied successfully.");

        } catch (FileNotFoundException e) {
            System.err.println("File not found: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("I/O error occurred: " + e.getMessage());
        }

    }
}
