package ru.itis.encoding.algorithms.core.executors;

import lombok.Builder;
import lombok.Data;
import ru.itis.encoding.algorithms.core.ArithmeticMetaInformation;
import ru.itis.encoding.algorithms.core.HuffmanEntry;
import ru.itis.encoding.algorithms.core.MtfEntry;
import ru.itis.encoding.algorithms.utils.ProbabilitiesWorker;

import java.io.*;
import java.math.BigDecimal;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

@Data
@Builder
public class MtfEncodingExecutor {

    protected boolean textLogging;
    protected boolean dictionariesLogging;
    protected int sizeLimit;
    protected int encodingAlphabetSize;

    public void encode(String inputPath, String probabilitiesPath, String codePath, String metaFilePath){
        ProbabilitiesWorker probabilitiesWorker = ProbabilitiesWorker.builder()
                .sizeLimit(sizeLimit)
                .textLogging(textLogging)
                .build();
        Map<Character, Double> probabilitiesMap = probabilitiesWorker.readProbabilitiesFile(probabilitiesPath); // считываем вероятности с файла
        if (probabilitiesMap.isEmpty()){ //если файл пустой
            if (dictionariesLogging){
                System.out.println("Посчитанная таблица вероятностей: ");
            }
            probabilitiesMap = probabilitiesWorker.countProbabilities(inputPath); // считаем вероятности символов для файла
        } else {
            System.out.println("Прочитанная таблица вероятностей: ");
        }
        if (probabilitiesMap.isEmpty()){
            return;
        }

        if (dictionariesLogging){
            System.out.println("Character -> Probability");
            for (Map.Entry<Character, Double> entry : probabilitiesMap.entrySet()){
                if (entry.getKey().equals('\n')){
                    System.out.println("'" + "\\n" + "' -> " + entry.getValue());
                } else if (entry.getKey().equals('\r')){
                    System.out.println("'" + "\\r" + "' -> " + entry.getValue());
                } else {
                    System.out.println("'" + entry.getKey() + "' -> " + entry.getValue());
                }
            }
        } // Вывод таблицы вероятностей

        probabilitiesMap =
                probabilitiesMap.entrySet().stream()
                        .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                        .collect(Collectors.toMap(
                                Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new)); // сортируем вероятности по убыванию

        //исходная последовательность книг
        StringBuilder charSequence = new StringBuilder();
        for (Map.Entry<Character, Double> entry : probabilitiesMap.entrySet()){
            charSequence.append(entry.getKey());
        }

        MtfEntry startEntry = getShelf(charSequence.toString());

        List<String> codeList = getCodeList(charSequence.length());

        if (dictionariesLogging){
            System.out.println("Полученная таблица кодов: ");
            System.out.println("Character -> Code");
            int index = 0;
            for (Map.Entry<Character, Double> entry : probabilitiesMap.entrySet()){
                if (entry.getKey().equals('\n')){
                    System.out.println("'" + "\\n" + "' -> " + codeList.get(index));
                } else if (entry.getKey().equals('\r')){
                    System.out.println("'" + "\\r" + "' -> " + codeList.get(index));
                } else {
                    System.out.println("'" + entry.getKey() + "' -> " + codeList.get(index));
                }
                index++;
            }
        } // Вывод начального положения стопки книг
        if (textLogging){
            System.out.print("Полученный код: '");
        } // Шапка вывода полученного кода
        writeMetaData(metaFilePath, charSequence.toString()); // Вывод мета-данных в отдельный файл
        writeCode(startEntry, codeList, inputPath, codePath); // Кодирование текста и его вывод в файл для кода
    }

    protected String conv(int num, int to) {
        StringBuilder result = new StringBuilder();
        while(num>0)
        {
            result.insert(0, num % to);
            num=num/to;
        }
        return result.toString();
    }

    protected List<String> getCodeList(Integer length){
        //считаем максимальную
        int codeLength = 1;
        int m = 2;
        while (m < length){
            codeLength++;
            m = m * encodingAlphabetSize;
        }

        //считаем код для каждого индекса
        List<String> codeList = new ArrayList<>();
        StringBuilder currentCode = new StringBuilder();
        StringBuilder zeroes = new StringBuilder();

        for (int i = 0; i < length; i++){
            currentCode.append(conv(i, encodingAlphabetSize));
            for (int j = 0; j < codeLength - currentCode.length(); j++){
                zeroes.append("0");
            }
            codeList.add(zeroes.toString() + currentCode);
            currentCode = new StringBuilder();
            zeroes = new StringBuilder();
        }

        return codeList;
    }

    protected MtfEntry getShelf(String charSequence){

        MtfEntry startEntry = null;
        MtfEntry currentEntry = null;
        MtfEntry newEntry = null;

        //заполняем связный список
        for (int i = 0; i < charSequence.length(); i++){
            if (startEntry == null){
                startEntry = MtfEntry.builder()
                        .character(charSequence.charAt(i))
                        .build();
                currentEntry = startEntry;
            } else {
                newEntry = MtfEntry.builder()
                        .character(charSequence.charAt(i))
                        .build();
                currentEntry.setNext(newEntry);
                currentEntry = newEntry;
            }
        }

        return startEntry;
    }

    protected void writeMetaData(String metaFilePath, String charSequence){
        try {
            int writerLimit = sizeLimit / 4;  //максимум символ будет весить 4 байта

            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(
                            new FileOutputStream(metaFilePath), StandardCharsets.UTF_8
                    ), writerLimit
            );
            if (textLogging){
                System.out.print(charSequence.replace("\r", "\\r").replace("\n", "\\n"));
            }
            writer.write(charSequence.toString());
            writer.flush();
            writer.close();
        } catch (Exception ex){
            throw new IllegalArgumentException(ex);
        }
    }

    protected void writeCode(MtfEntry start, List<String> codes, String inputPath, String codePath){
        int bufferLimit = sizeLimit / 4;  //максимум символ будет весить 4 байта

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(
                        new FileInputStream(inputPath), StandardCharsets.UTF_8
                ), bufferLimit
        ))
        {
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(
                            new FileOutputStream(codePath), StandardCharsets.UTF_8
                    ), bufferLimit
            );
            if (textLogging){
                System.out.print("{");
            }
            int nextChar;
            StringBuilder code = new StringBuilder();
            int maxCodeLength = codes.get(0).length();
            long sizeToFlush = sizeLimit - maxCodeLength; // size лимит берём 1 к 1, т.к. каждый наш кодирующий символ в UTF-8 будет весить 1 байт - Unicode

            while ((nextChar = reader.read()) != -1) {
                char ch = (char) nextChar;

                int currentIndex = 0;
                MtfEntry previous = null;
                MtfEntry current;

                current = start;
                while (!current.getCharacter().equals(ch)){
                    previous = current;
                    current = current.getNext();
                    currentIndex++;
                }
                code.append(codes.get(currentIndex));
                if (currentIndex != 0){
                    previous.setNext(current.getNext());
                    current.setNext(start);
                    start = current;

                }

                if (code.length() >= sizeToFlush){
                    writer.write(code.toString());
                    writer.flush();
                    if (textLogging){
                        System.out.print(code);
                    }
                    code = new StringBuilder();
                }
            }
            writer.write(code.toString());
            writer.flush();
            if (textLogging){
                System.out.print(code);
                System.out.println("}'; ({} - биты)");
            }
            writer.close();
        } catch (Exception ex){
            throw new IllegalArgumentException(ex);
        }
    }

    public void decode(String codePath, String outputPath, String metaFilePath){
        String charSequence = readMetaData(metaFilePath);
        if (charSequence.length() == 0){
            return;
        }
        MtfEntry startEntry = getShelf(charSequence.toString());
        List<String> codeList = getCodeList(charSequence.length());
        writeText(startEntry, codeList, codePath, outputPath);
    }

    protected String readMetaData(String metaFilePath){
        int readerLimit = sizeLimit / 4;  //максимум символ будет весить 4 байта

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(
                        //Objects.requireNonNull(ArithmeticEncodingExecutor.class.getClassLoader().getResourceAsStream(metaFilePath)), StandardCharsets.UTF_8
                        new FileInputStream(metaFilePath), StandardCharsets.UTF_8
                ), readerLimit
        )) {
            int nextChar;
            StringBuilder charSequence = new StringBuilder();
            while ((nextChar = reader.read()) != -1) {
                char ch = (char) nextChar;
                charSequence.append(ch);
            }
            if (dictionariesLogging){
                System.out.println("Полученная стартовая последовательность символов: " + charSequence);
            }
            return charSequence.toString();
        } catch (Exception ex){
            throw new IllegalArgumentException(ex);
        }
    }

    public void writeText(MtfEntry start, List<String> codes, String inputPath, String outputPath){
        int bufferLimit = sizeLimit / 4;  //максимум символ будет весить 4 байта

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(
                        new FileInputStream(inputPath), StandardCharsets.UTF_8
                ), bufferLimit
        ))
        {
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(
                            new FileOutputStream(outputPath), StandardCharsets.UTF_8
                    ), bufferLimit
            );
            if (textLogging){
                System.out.print("{");
            }
            int nextChar;
            StringBuilder text = new StringBuilder();
            int codeLength = codes.get(0).length();
            long sizeToFlush = sizeLimit - codeLength; // size лимит берём 1 к 1, т.к. каждый наш кодирующий символ в UTF-8 будет весить 1 байт - Unicode
            StringBuilder currentCode = new StringBuilder();

            while ((nextChar = reader.read()) != -1) {
                char ch = (char) nextChar;
                if (currentCode.length() < codeLength - 1){
                    currentCode.append(ch);
                } else {
                    currentCode.append(ch);
                    int index = deconv(currentCode.toString());
                    currentCode = new StringBuilder();

                    int currentIndex = 0;
                    MtfEntry previous = null;
                    MtfEntry current;

                    current = start;
                    while (currentIndex != index){
                        previous = current;
                        current = current.getNext();
                        currentIndex++;
                    }
                    text.append(current.getCharacter());
                    if (currentIndex != 0){
                        previous.setNext(current.getNext());
                        current.setNext(start);
                        start = current;
                    }

                    if (text.length() >= sizeToFlush){
                        writer.write(text.toString());
                        writer.flush();
                        if (textLogging){
                            System.out.print(text);
                        }
                        text = new StringBuilder();
                    }
                }
            }
            writer.write(text.toString());
            writer.flush();
            if (textLogging){
                System.out.print(text);
                System.out.println("}'; ({} - биты)");
            }
            writer.close();
        } catch (Exception ex){
            throw new IllegalArgumentException(ex);
        }
    }

    protected int deconv(String str) {
        int result = 0;
        int m = 1;
        for (int i = str.length() - 1; i>=0; i--){
            result+=Integer.parseInt("" + str.charAt(i)) * m;
            m = m * encodingAlphabetSize;
        }
        return result;
    }
}
