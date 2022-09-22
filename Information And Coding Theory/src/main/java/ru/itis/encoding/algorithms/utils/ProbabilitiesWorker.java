package ru.itis.encoding.algorithms.utils;

import lombok.Builder;
import lombok.Data;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Data
@Builder
public class ProbabilitiesWorker {

    protected int sizeLimit;
    protected boolean textLogging;

    public Map<Character, Double> readProbabilitiesFile(String probabilitiesPath) {
        int readerLimit = sizeLimit / 4;  //максимум символ будет весить 4 байта

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(
                        new FileInputStream(probabilitiesPath), StandardCharsets.UTF_8
                ), readerLimit
        )) {
            Map<Character, Double> result = new HashMap<Character, Double>();
            String nextLine;
            while ((nextLine = reader.readLine()) != null) {
                String[] lineParts = nextLine.split("=");
                if (lineParts[0].length() == 1){
                    result.put(lineParts[0].charAt(0), Double.parseDouble(lineParts[1]));
                } else if (lineParts[0].equals("\\r")){
                    result.put('\r', Double.parseDouble(lineParts[1]));
                } else if (lineParts[0].equals("\\n")){
                    result.put('\n', Double.parseDouble(lineParts[1]));
                }
            }
            return result;
        } catch (Exception ex){
            throw new IllegalArgumentException(ex);
        }
    }

    public Map<Character, Double> countProbabilities(String inputPath) {
        int readerLimit = sizeLimit / 4;  //максимум символ будет весить 4 байта

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(
                        new FileInputStream(inputPath), StandardCharsets.UTF_8
                ), readerLimit
        ))
        {
            Map<Character, Long> countMap = new HashMap<Character, Long>();
            int nextChar;
            long counter = 0;
            Long currentCounter;
            if (textLogging){
                System.out.print("Данные для кодирования: '");
            }
            while ((nextChar = reader.read()) != -1) {
                counter++;
                char ch = (char) nextChar;
                if (textLogging){
                    System.out.print(ch);
                }
                currentCounter = countMap.get(ch);
                if (currentCounter==null){
                    countMap.put(ch, 1L);
                } else {
                    countMap.put(ch, currentCounter + 1);
                }
            }
            if (textLogging){
                System.out.println("';");
            }
            Map<Character, Double> result = new HashMap<Character, Double>();
            for (Map.Entry<Character, Long> entry: countMap.entrySet()) {
                result.put(entry.getKey(), ((double) entry.getValue() / counter));
            }
            return result;
        } catch (Exception ex){
            throw new IllegalArgumentException(ex);
        }
    }

}
