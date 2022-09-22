package ru.itis.encoding.algorithms.core.executors;

import lombok.Builder;
import lombok.Data;
import ru.itis.encoding.algorithms.core.BwtEntry;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

@Data
@Builder
public class BwtTransformationExecutor {

    protected boolean textLogging;
    protected boolean dictionariesLogging;
    protected int sizeLimit;
    protected int encodingAlphabetSize;

    public void convert(String inputPath, String codePath, String metaPath){
        int bufferLimit = sizeLimit / 4;  //максимум символ будет весить 4 байта

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(
                        new FileInputStream(inputPath), StandardCharsets.UTF_8
                ), bufferLimit
        ))
        {
            int nextChar;
            StringBuilder stringToConvert = new StringBuilder();

            //считываем строку
            while ((nextChar = reader.read()) != -1) {
                char ch = (char) nextChar;
                stringToConvert.append(ch);
            }
            if (stringToConvert.length() == 0){
                return;
            }


            //строим матрицу сдвигов
            List<String> shifts = new ArrayList<>();
            shifts.add(stringToConvert.toString());
            for (int currentShift = 1; currentShift < stringToConvert.length(); currentShift++){
                shifts.add(stringToConvert.substring(stringToConvert.length() - currentShift)
                        + stringToConvert.substring(0, stringToConvert.length() - currentShift));
            }

            //сортируем матрицу сдвигов
            Collections.sort(shifts);

            BufferedWriter metaWriter = new BufferedWriter(
                    new OutputStreamWriter(
                            new FileOutputStream(metaPath), StandardCharsets.UTF_8
                    ), bufferLimit
            );

            //в мета-информацию записываем номер исходной строки в отсортированной матрице
            metaWriter.write("" + shifts.indexOf(stringToConvert.toString()));
            metaWriter.flush();
            metaWriter.close();

            //собираем преобразованную строку
            StringBuilder transformation = new StringBuilder();
            for (int i = 0; i < shifts.size(); i++){
                transformation.append(shifts.get(i).charAt(stringToConvert.length() - 1));
            }

            BufferedWriter codeWriter = new BufferedWriter(
                    new OutputStreamWriter(
                            new FileOutputStream(codePath), StandardCharsets.UTF_8
                    ), bufferLimit
            );

            //записываем преобразованную строку
            codeWriter.write("" + transformation);
            codeWriter.flush();
            codeWriter.close();
        } catch (Exception ex){
            throw new IllegalArgumentException(ex);
        }
    }

    public void reconvert(String codePath, String outputPath, String metaFilePath){
        int bufferLimit = sizeLimit / 4;  //максимум символ будет весить 4 байта

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(
                        new FileInputStream(codePath), StandardCharsets.UTF_8
                ), bufferLimit
        ))
        {
            int nextChar;
            StringBuilder stringToConvert = new StringBuilder();

            //считываем строку
            while ((nextChar = reader.read()) != -1) {
                char ch = (char) nextChar;
                stringToConvert.append(ch);
            }

            if (stringToConvert.length() == 0){
                return;
            }

            //создаём список с записями (шаг 1)
            List<BwtEntry> entries = new ArrayList<>();
            for (int i = 0; i < stringToConvert.length(); i++){
                entries.add(BwtEntry.builder()
                        .secondSymbol(stringToConvert.charAt(i))
                        .firstIndex(i)
                        .build());
            }

            //создаём копию списка и сортируем его
            List<BwtEntry> entriesToSort = new ArrayList<>(entries);
            entriesToSort = entriesToSort.stream().sorted(Comparator.comparingInt(BwtEntry::getSecondSymbol)).collect(Collectors.toList());

            //заполняем недостающие данные
            for (int i = 0; i < entriesToSort.size(); i++){
                entriesToSort.get(i).setSecondIndex(i);
                entries.get(i).setFirstSymbol(entriesToSort.get(i).getSecondSymbol());
            }

            entries = entries.stream().sorted(Comparator.comparingInt(BwtEntry::getSecondSymbol)).collect(Collectors.toList());

            //считываем индекс исходной строки
            BufferedReader metaReader = new BufferedReader(
                    new InputStreamReader(
                            new FileInputStream(metaFilePath), StandardCharsets.UTF_8
                    ), bufferLimit
            );
            int index = Integer.parseInt(metaReader.readLine());
            metaReader.close();

            //собираем исходную строку
            StringBuilder reconvertedString = new StringBuilder();
            BwtEntry currentEntry = entries.get(index);
            reconvertedString.append(currentEntry.getSecondSymbol());
            currentEntry = entries.get(currentEntry.getFirstIndex());
                while (currentEntry.getSecondIndex() != index){
                    reconvertedString.append(currentEntry.getSecondSymbol());
                    currentEntry = entries.get(currentEntry.getFirstIndex());
                }
            if (reconvertedString.length() < stringToConvert.length()){
                String repeatedBlock = reconvertedString.toString();
                for (int i = 1; i < stringToConvert.length() / repeatedBlock.length(); i++){
                    reconvertedString.append(repeatedBlock);
                }
            }

            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(
                            new FileOutputStream(outputPath), StandardCharsets.UTF_8
                    ), bufferLimit
            );

            //записываем обратно преобразованную строку
            writer.write(reconvertedString.toString());
            writer.flush();
            writer.close();
        } catch (Exception ex){
            throw new IllegalArgumentException(ex);
        }
    }

}
