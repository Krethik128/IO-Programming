package com.gevernova.fileHandling;

import java.io.*;

public class LowerCaseToUppercase {
    public static void main(String[] args) {
        File sourceFile=new File("/Users/krethik/Desktop/sample.txt");
        File destinationFile=new File("/Users/krethik/Desktop/createdFile.txt");
        try{
                fileNotfound(sourceFile);
                try (
                BufferedReader reader=new BufferedReader(new FileReader(sourceFile));
                BufferedWriter writer=new BufferedWriter(new FileWriter(destinationFile))) {

                    if (destinationFile.createNewFile()) {
                        System.out.println("File created " + destinationFile.getName());
                    }
                    else {
                        System.out.println("File already exists. Content will be overwritten.");
                    }
                    String Line;
                    while((Line=reader.readLine())!=null){
                        String[] words=Line.split("\\s+");
                        for(String word:words){
                            writer.write(word.toUpperCase()+" ");
                        }
                        writer.newLine();
                    }
                }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static void fileNotfound(File file) throws FileNotFoundException {
        if (!file.exists()) {
            throw new FileNotFoundException("Error: Source file does not exist: " + file.getAbsolutePath());
        }
    }
}
