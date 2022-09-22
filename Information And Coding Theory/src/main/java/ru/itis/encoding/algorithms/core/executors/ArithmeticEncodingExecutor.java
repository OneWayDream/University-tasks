package ru.itis.encoding.algorithms.core.executors;

import lombok.Builder;
import lombok.Data;
import ru.itis.encoding.algorithms.core.ArithmeticEntry;
import ru.itis.encoding.algorithms.core.ArithmeticMetaInformation;
import ru.itis.encoding.algorithms.core.Interval;
import ru.itis.encoding.algorithms.utils.ProbabilitiesWorker;

import java.io.*;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

@Data
@Builder
public class ArithmeticEncodingExecutor {

    protected boolean textLogging;
    protected boolean dictionariesLogging;
    protected int sizeLimit;

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
        } // Вывод таблицы вероятностей, по которой будет происходить арифметическое кодирование
        writeMetaData(metaFilePath, countSymbols(inputPath),probabilitiesMap);
        writeCode(probabilitiesMap, inputPath, codePath);
    }

    protected void writeMetaData(String metaFilePath, int symbolCount, Map<Character, Double > probabilities){
        try{
            int writerLimit = sizeLimit / 4;  //максимум символ будет весить 4 байта

            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(
                            new FileOutputStream(metaFilePath), StandardCharsets.UTF_8
                    ), writerLimit
            );
            StringBuilder builder = new StringBuilder();
            for (Map.Entry<Character, Double> entry : probabilities.entrySet()){
                if (entry.getKey().equals('\r')){
                    builder.append("\\r");
                } else if (entry.getKey().equals('\n')){
                    builder.append("\\n");
                } else {
                    builder.append(entry.getKey());
                }
                builder.append("=").append(entry.getValue()).append("\n");
            }
            builder.append("length=").append(symbolCount);
            if (textLogging){
                System.out.print(builder);
            }
            writer.write(builder.toString());
            writer.flush();
        } catch (Exception ex){
            throw new IllegalArgumentException(ex);
        }
    }

    protected int countSymbols(String inputPath){
        int bufferLimit = sizeLimit / 4;  //максимум символ будет весить 4 байта

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(
                        new FileInputStream(inputPath), StandardCharsets.UTF_8
                ), bufferLimit
        ))
        {

            int nextChar;
            int counter = 0;
            while ((nextChar = reader.read()) != -1) {
                char ch = (char) nextChar;
                counter++;
            }
            return counter;
        } catch (Exception ex){
            throw new IllegalArgumentException(ex);
        }
    }

    protected void writeCode(Map<Character, Double> probabilities, String inputPath, String codePath){
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

            probabilities =
                    probabilities.entrySet().stream()
                            .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                            .collect(Collectors.toMap(
                                    Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new)); // сортируем вероятности по убыванию

            Map<Character, Interval> intervals = new HashMap<>(); // словарь символов и соответствующих им интервалов

            Interval previousInterval = null;
            for (Map.Entry<Character, Double> entry: probabilities.entrySet()){ //строим список интервалов и символов
                if (previousInterval == null){
                    previousInterval = Interval.builder()
                            .start(BigDecimal.valueOf(0.0))
                            .end(BigDecimal.valueOf(entry.getValue()))
                            .build();
                } else {
                    previousInterval = Interval.builder()
                            .start(previousInterval.getEnd())
                            .end(previousInterval.getEnd().add(BigDecimal.valueOf(entry.getValue())))
                            .build();
                }
                intervals.put(entry.getKey(), previousInterval);
            }

            int nextChar;
            StringBuilder builder = new StringBuilder(); //часть закодированного текста
            BigDecimal start = BigDecimal.valueOf(0.0); //начало текущего интервала
            BigDecimal end = BigDecimal.valueOf(1.0);  //конец текущего интервала
            BigDecimal intervalSize = end.subtract(start); // размер текущего интервала

            int maxShift = probabilities.values().stream() // максимальное увеличение длины числа на следующем шаге
                    .map(aDouble -> aDouble.toString().length()).max(Integer::compare).orElse(0) - 2;
            int decimalLimit = sizeLimit - 80 - 4 * maxShift; // 80 байт - информация помимо хранимого массива символов
            Interval currentInterval;
            int counter = 0;

            long checkBlock = (long) Math.log(sizeLimit);
            long currentCheckSize = checkBlock;

            while ((nextChar = reader.read()) != -1) {

                char ch = (char) nextChar;
                counter++;
                if (counter % 100 == 0){ // отображение, что ведётсяя кодирование
                    System.out.println("Progress : " + counter + " symbols.");
                }
                currentInterval = intervals.get(ch);
                if (currentInterval != null){
                    end = start.add(intervalSize.multiply(currentInterval.getEnd()));
                    start = start.add(intervalSize.multiply(currentInterval.getStart()));
                    intervalSize = end.subtract(start);
                }

                // не привысили ли мы потолок
                if (start.toString().length() >= currentCheckSize){

                    //проверяем наличие общих частей у чисел, чтобы можно было их обработать и не держать в памяти
                    String startStr = start.toString();
                    String endStr = end.toString();
                    StringBuilder generalPart = new StringBuilder(); // общая часть
                    for (int i = 2; i < startStr.length(); i++){
                        if (startStr.charAt(i) != endStr.charAt(i)){
                            break;
                        }
                        generalPart.append(startStr.charAt(i));
                    }

                    if (generalPart.length() == 0){
                        if (currentCheckSize >= sizeLimit){
                            System.out.println("Для кодирования данной строки недостаточно выделенной памяти :с");
                            writer.close();
                            reader.close();
                            return;
                        } else {
                            currentCheckSize+=checkBlock;
                        }
                    } else {
                        //вырезаем общие части
                        start = new BigDecimal("0." + startStr.substring(2 + generalPart.length()));
                        end = new BigDecimal("0." + endStr.substring(2 + generalPart.length()));
                        intervalSize = end.subtract(start);

                        //сохраняем общую часть
                        builder.append(generalPart);
                        currentCheckSize-=checkBlock;
                    }
                }
            }


            String smallZeroSequencePattern = "0.(0+)";
            String bigZeroSequencePattern = "0E-(\\d+)";
            StringBuilder code = new StringBuilder();

            if (start.toString().matches(smallZeroSequencePattern) || (start.toString().matches(bigZeroSequencePattern))){
                code.append(0);
            } else if (end.toString().charAt(0) == '1'){
                code.append(1);
            } else {
                start = new BigDecimal("0." + builder.toString() + start.toString().substring(2));
                end = new BigDecimal("0." + builder.toString() + end.toString().substring(2));

                BigDecimal fraction = new BigDecimal("1.0");
                BigDecimal currentSum = new BigDecimal("0.0");
                BigDecimal newSum;
                int startCompare;
                int endCompare;
                counter = 0;

                while (true) {
                    fraction = fraction.divide(BigDecimal.valueOf(2.0));
                    newSum = currentSum.add(fraction);
                    startCompare = start.compareTo(newSum);
                    endCompare = end.compareTo(newSum);
                    if (endCompare <= 0){ //конец равен новой сумме или меньше её
                        code.append(0);
                    } else {
                        code.append(1);
                        if (startCompare > 0){ //начало строго больше новой суммы
                            currentSum = newSum;
                        } else { // сумма попала в интервал
                            break;
                        }
                    }
                    if (counter % 150 == 0){
                        System.out.print("^u^ ");
                    }
                    counter++;
                }
                System.out.println("");

            }

            writer.write(code.toString());
            writer.flush();

            if (textLogging){
                System.out.print("Полученный код: '");
                System.out.print(code);
                System.out.println("';");
            }

            writer.close();
        } catch (Exception ex){
            throw new IllegalArgumentException(ex);
        }
    }

    public void decode(String codePath, String outputPath, String metaFilePath){
        ArithmeticMetaInformation arithmeticMetaInformation = readMetaData(metaFilePath);
        Map<Character, BigDecimal> dist = arithmeticMetaInformation.getProbabilities(); // считываем информацию для декодирования
        Integer length = arithmeticMetaInformation.getLength();
        if (dictionariesLogging){
            System.out.println("Полученные вероятности: ");
            for (Map.Entry<Character, BigDecimal> entry : dist.entrySet()){
                if (entry.getKey().equals('\n')){
                    System.out.println("'" + "\\n" + "' -> " + entry.getValue());
                } else if (entry.getKey().equals('\r')){
                    System.out.println("'" + "\\r" + "' -> " + entry.getValue());
                } else {
                    System.out.println("'" + entry.getKey() + "' -> " + entry.getValue());
                }
            }
        }
        writeText(codePath, outputPath, dist, length);
    }

    protected ArithmeticMetaInformation readMetaData(String metaFilePath){
        int readerLimit = sizeLimit / 4;  //максимум символ будет весить 4 байта

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(
                        new FileInputStream(metaFilePath), StandardCharsets.UTF_8
                ), readerLimit
        )) {
            Map<Character, BigDecimal> probabilities = new HashMap<>();
            String nextLine;
            Integer length = null;
            while ((nextLine = reader.readLine()) != null) {
                String[] lineParts = nextLine.split("=");
                if (lineParts[0].length() == 1){
                    probabilities.put(lineParts[0].charAt(0), new BigDecimal(lineParts[1]));
                } else if (lineParts[0].equals("\\r")){
                    probabilities.put('\r', new BigDecimal(lineParts[1]));
                } else if (lineParts[0].equals("\\n")){
                    probabilities.put('\n', new BigDecimal(lineParts[1]));
                } else if (lineParts[0].equals("length")){
                    length = Integer.parseInt(lineParts[1]);
                }
            }
            return ArithmeticMetaInformation.builder()
                    .probabilities(probabilities)
                    .length(length)
                    .build();
        } catch (Exception ex){
            throw new IllegalArgumentException(ex);
        }
    }

    protected void writeText(String codePath, String outputPath, Map<Character, BigDecimal> probabilities, Integer length){
        int bufferLimit = sizeLimit / 4;  //максимум символ будет весить 4 байта

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(
                        new FileInputStream(codePath), StandardCharsets.UTF_8
                ), sizeLimit
        ))
        {
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(
                            new FileOutputStream(outputPath), StandardCharsets.UTF_8
                    ), bufferLimit
            );

            probabilities =
                    probabilities.entrySet().stream()
                            .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                            .collect(Collectors.toMap(
                                    Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new)); // сортируем вероятности по убыванию

            List<ArithmeticEntry> intervals = new ArrayList<>(); // список символов и соответствующих им интервалов (упорядочен по интервалам)

            ArithmeticEntry previousEntry = null;
            for (Map.Entry<Character, BigDecimal> entry: probabilities.entrySet()){ //строим список интервалов и символов
                if (previousEntry == null){
                    previousEntry = ArithmeticEntry.builder()
                            .start(BigDecimal.valueOf(0.0))
                            .end(entry.getValue())
                            .symbol(entry.getKey())
                            .build();
                } else {
                    previousEntry = ArithmeticEntry.builder()
                            .start(previousEntry.getEnd())
                            .end(previousEntry.getEnd().add(entry.getValue()))
                            .symbol(entry.getKey())
                            .build();
                }
                intervals.add(previousEntry);
            }

            int counter = 0;

            //считываем число в двоичном виде и переводим его в десятичный
            String codeString = reader.readLine();
            BigDecimal codeNum = new BigDecimal("0.0");
            BigDecimal currentNum = new BigDecimal("1.0");
            for (int i = 0; i < codeString.length(); i++){
                currentNum = currentNum.divide(BigDecimal.valueOf(2));
                if (codeString.charAt(i) == '1'){
                    codeNum = codeNum.add(currentNum);
                }
                if (counter % 100 == 0){
                    System.out.print("^i^ ");
                }
                counter++;
            }
            System.out.println("");

            StringBuilder builder = new StringBuilder(); //часть декодированного текста
            BigDecimal start = BigDecimal.valueOf(0.0); //начало текущего интервала
            BigDecimal end = BigDecimal.valueOf(1.0);  //конец текущего интервала
            BigDecimal intervalSize = end.subtract(start); // размер текущего интервала

            int maxShift = probabilities.values().stream() // максимальное увеличение длины числа на следующем шаге
                    .map(aDouble -> aDouble.toString().length()).max(Integer::compare).orElse(0) - 2;
            int decimalLimit = sizeLimit - 80 - 4 * maxShift; // 80 байт - информация помимо хранимого массива символов

            ArithmeticEntry currentEntry;
            int searchIntervalStart;
            int searchIntervalEnd;
            BigDecimal currentStart;
            BigDecimal currentEnd;
            int startCompare;
            int endCompare;

            //Декодируем length символов
            for (int i = 0; i < length; i++){

                //бинарно ищем нужный нам отрезок
                searchIntervalStart = 0;
                searchIntervalEnd = intervals.size() - 1;
                currentEntry = intervals.get((searchIntervalEnd + searchIntervalStart)/2);

                //пробуем сместиться в отрезок и проверяем, находился ли наше число в нём
                currentEnd = start.add(intervalSize.multiply(currentEntry.getEnd()));
                currentStart = start.add(intervalSize.multiply(currentEntry.getStart()));
                startCompare = codeNum.compareTo(currentStart);
                endCompare = codeNum.compareTo(currentEnd);

                //пока не попали - ищем, сужая область поиска
                while (!((startCompare >= 0) && (endCompare <0))){
                    if (startCompare < 0) {
                        searchIntervalEnd = ((searchIntervalEnd + searchIntervalStart) / 2) - 1;
                    } else {
                        searchIntervalStart = ((searchIntervalEnd + searchIntervalStart) / 2) + 1;
                    }
                    currentEntry = intervals.get((searchIntervalEnd + searchIntervalStart)/2);

                    currentEnd = start.add(intervalSize.multiply(currentEntry.getEnd()));
                    currentStart = start.add(intervalSize.multiply(currentEntry.getStart()));
                    startCompare = codeNum.compareTo(currentStart);
                    endCompare = codeNum.compareTo(currentEnd);
                }
                start = currentStart;
                end = currentEnd;
                intervalSize = end.subtract(start);

                builder.append(currentEntry.getSymbol());

                if (i % 100 == 0){
                    System.out.println("Progress : " + i + " symbols.");
                }
            }

            writer.write(builder.toString());
            writer.flush();

            if (textLogging){
                System.out.print("Полученный текст: '");
                System.out.print(builder);
                System.out.println("';");
            }

            writer.close();
        } catch (Exception ex){
            throw new IllegalArgumentException(ex);
        }
    }

}
