package com.gevernova.fileHandling;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class CountWordFreq {
    public static void main(String[] args) {
        String filePath="/Users/krethik/Desktop/sample.txt";
        Map<String,Integer> hashMap=new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while((line=reader.readLine())!=null){
                String[] words=line.split("\\s+");
                for(String word:words) {
                    if (!word.isEmpty()) {
                        if (hashMap.containsKey(word)) {
                            hashMap.put(word, hashMap.get(word) + 1);
                        }else{
                            hashMap.put(word, 1);
                        }
                    }
                }
            }
            hashMap.entrySet()
                    .stream()
                    .sorted(Map.Entry.<String, Integer>comparingByValue(Comparator.reverseOrder()))
                    .limit(5)
                    .forEach(entry-> System.out.println(entry.getKey()+" = "+entry.getValue()));

        }catch (FileNotFoundException e){
            throw new RuntimeException(e.getMessage());
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
