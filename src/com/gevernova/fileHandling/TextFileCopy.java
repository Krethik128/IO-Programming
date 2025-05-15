package com.gevernova.fileHandling;

import java.io.*;

public class TextFileCopy {
    public static void main(String[] args) {
        File sourceFile=new File("/Users/krethik/desktop/sampl.txt");
        File newfile=new File("/Users/krethik/desktop/copiedSample.txt");
        String destinationPath="/Users/krethik/desktop/copiedSample.txt";
        String path="/Users/krethik/desktop/sample.txt";
        try {
            fileNotfound(sourceFile);
            if(newfile.createNewFile()){
                System.out.println("File created "+newfile.getName());
            }
            else{
                System.out.println("File already exists. Overwriting contents...\"");
            }
            try(BufferedReader reader=new BufferedReader(new FileReader(sourceFile));
            BufferedWriter writer= new BufferedWriter(new FileWriter(newfile))){
                String line;
                while((line=reader.readLine())!=null){
                    writer.write(line);
                    writer.newLine();
                }
                writer.write("here is the copied txt file from "+path);
                System.out.println("File copied successfully to '" + destinationPath + "'");
            }
        } catch (FileNotFoundException e) {
            System.out.println("FileNotFoundException: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("IOException occurred: " + e.getMessage());
        }

    }
    public static void fileNotfound(File file) throws FileNotFoundException {
        if (!file.exists()) {
            throw new FileNotFoundException("Error: Source file does not exist: " + file.getAbsolutePath());
        }
    }
}
