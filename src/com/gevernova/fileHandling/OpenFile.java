package com.gevernova.fileHandling;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;

public class OpenFile {
    public static void main(String[] args) {
        String path="/Users/krethik/Desktop/sample.txt";
        try{
            File file=new File(path);
            fileNotfound(file);
            if(Desktop.isDesktopSupported()){
                System.out.println("Desktop is supported");
            }
            Desktop desktop=Desktop.getDesktop();
            desktop.open(file);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
    public static void fileNotfound(File file) throws FileNotFoundException {
        if (!file.exists()) {
            throw new FileNotFoundException("Error: Source file does not exist: " + file.getAbsolutePath());
        }
    }
}
