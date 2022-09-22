package ru.itis.encoding.algorithms.core.executors;

import lombok.Builder;
import lombok.Data;
import ru.itis.encoding.algorithms.core.DictionaryTreeNode;
import ru.itis.encoding.algorithms.core.DictionaryTreeWorker;
import ru.itis.encoding.algorithms.core.HuffmanEntry;
import ru.itis.encoding.algorithms.utils.ProbabilitiesWorker;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

@Data
@Builder
public class HuffmanEncodingExecutor {

    protected boolean textLogging;
    protected boolean dictionariesLogging;
    protected int sizeLimit;
    protected int encodingAlphabetSize;
    protected static final char SPECIAL_SYMBOL = '/';

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
        } // Вывод таблицы вероятностей, по которой будут строиться коды Хаффмана
        if (probabilitiesMap.isEmpty()){
            return;
        }
        HuffmanEntry rootHuffmanEntry = getRootHuffmanEntry(probabilitiesMap); // Строим дерево Хаффмана снизу вверх, собрав все вершины в корневую.
        Map<Character, String> huffmanCodes = getHuffmanCodes(rootHuffmanEntry, new HashMap<>(), ""); // Разбираем дерево на коды
        if (dictionariesLogging){
            System.out.println("Полученная таблица кодов: ");
            System.out.println("Character -> Code");
            for (Map.Entry<Character, String> entry : huffmanCodes.entrySet()){
                if (entry.getKey().equals('\n')){
                    System.out.println("'" + "\\n" + "' -> " + entry.getValue());
                } else if (entry.getKey().equals('\r')){
                    System.out.println("'" + "\\r" + "' -> " + entry.getValue());
                } else {
                    System.out.println("'" + entry.getKey() + "' -> " + entry.getValue());
                }
            }
        } // Вывод таблицы кодов, по которой будет происходить кодирование
        if (textLogging){
            System.out.print("Полученный код: '");
        } // Шапка вывода полученного кода
        writeMetaData(metaFilePath, huffmanCodes); // Вывод мета-данных в отдельный файл
        writeCode(huffmanCodes, inputPath, codePath); // Кодирование текста и его вывод в файл для кода
    }

    protected HuffmanEntry getRootHuffmanEntry(Map<Character, Double> probabilitiesMap){
        int rest = encodingAlphabetSize - 2 - ((probabilitiesMap.size() - 2) % (encodingAlphabetSize - 1));

        List<HuffmanEntry> leaves = probabilitiesMap.entrySet().stream()
                .map(entry -> HuffmanEntry.builder()
                        .subLeaves(null)
                        .symbol(entry.getKey())
                        .probability(entry.getValue()).build())
                .sorted(Comparator.comparingDouble(HuffmanEntry::getProbability))
                .collect(Collectors.toList());
        Collections.reverse(leaves);
        leaves = new ArrayList<>(leaves);

        HuffmanEntry huffmanEntry;
        int layerSize = encodingAlphabetSize - rest;
        while (leaves.size() > 1){

            huffmanEntry = HuffmanEntry.builder()
                    .subLeaves(new ArrayList<>(leaves.subList(leaves.size() - layerSize, leaves.size())))
                    .build();
            huffmanEntry.setProbability(
                    huffmanEntry.getSubLeaves().stream().map(HuffmanEntry::getProbability).reduce(Double::sum).orElse(0.0)
            );

            leaves.removeAll(huffmanEntry.getSubLeaves());
            boolean isSet = false;
            for (int i = 0; i < leaves.size(); i++){
                if (Double.compare(leaves.get(i).getProbability(), huffmanEntry.getProbability()) == -1){
                    leaves.add(i, huffmanEntry);
                    isSet = true;
                    break;
                }
            }
            if (!isSet){
                leaves.add(huffmanEntry);
            }
            layerSize = encodingAlphabetSize;
        }
        return leaves.get(0);
    }

    protected Map<Character, String> getHuffmanCodes(HuffmanEntry huffmanEntry, Map<Character, String> huffmanCodes, String behindCode){
        if (huffmanEntry.getSubLeaves() == null){
            if (behindCode.length() == 0){
                behindCode = "0";
            }
            huffmanCodes.put(huffmanEntry.getSymbol(), behindCode);
        } else {
            for (int i = 0; i < huffmanEntry.getSubLeaves().size(); i++){
                huffmanCodes = getHuffmanCodes(huffmanEntry.getSubLeaves().get(i), huffmanCodes, behindCode + i);
            }
        }
        return huffmanCodes;
    }

    protected void writeMetaData(String metaFilePath, Map<Character, String > dict){
        try {
            int writerLimit = sizeLimit / 4;  //максимум символ будет весить 4 байта

            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(
                            new FileOutputStream(metaFilePath), StandardCharsets.UTF_8
                    ), writerLimit
            );
            StringBuilder builder = new StringBuilder();
            for (Map.Entry<Character, String> entry : dict.entrySet()){
                if (isSpecial(entry.getKey())){
                    builder.append(SPECIAL_SYMBOL);
                }
                builder.append(entry.getKey()).append(entry.getValue());
            }
            if (textLogging){
                System.out.print(builder.toString().replace("\r", "\\r").replace("\n", "\\n"));
            }
            writer.write(builder.toString());
            writer.flush();
        } catch (Exception ex){
            throw new IllegalArgumentException(ex);
        }
    }

    protected boolean isSpecial(char ch){
        return ((ch >= 48) && (ch <=57)) || //'0' - '9'
                (ch == 47);// '/'
    }

    protected void writeCode(Map<Character, String> dict, String inputPath, String codePath){
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
            StringBuilder builder = new StringBuilder();
            int maxCodeLength = dict.values().stream().map(String::length).max(Integer::compare).orElse(0);
            long sizeToFlush = sizeLimit - maxCodeLength; // size лимит берём 1 к 1, т.к. каждый наш кодирующий символ в UTF-8 будет весить 1 байт - Unicode

            while ((nextChar = reader.read()) != -1) {
                char ch = (char) nextChar;
                builder.append(dict.get(ch));
                if (builder.length() >= sizeToFlush){
                    writer.write(builder.toString());
                    writer.flush();
                    if (textLogging){
                        System.out.print(builder);
                    }
                    builder = new StringBuilder();
                }
            }
            writer.write(builder.toString());
            writer.flush();
            if (textLogging){
                System.out.print(builder);
                System.out.println("}'; ({} - биты)");
            }
            writer.close();
        } catch (Exception ex){
            throw new IllegalArgumentException(ex);
        }
    }

    public void decode(String codePath, String outputPath, String metaFilePath){
        Map<Character, String> dist = readMetaData(metaFilePath); // считываем информацию для декодирования
        if (dist.isEmpty()){
            return;
        }
        if (dictionariesLogging){
            System.out.println("Полученный словарь: ");
            for (Map.Entry<Character, String> entry : dist.entrySet()){
                if (entry.getKey().equals('\n')){
                    System.out.println("'" + "\\n" + "' -> " + entry.getValue());
                } else if (entry.getKey().equals('\r')){
                    System.out.println("'" + "\\r" + "' -> " + entry.getValue());
                } else {
                    System.out.println("'" + entry.getKey() + "' -> " + entry.getValue());
                }
            }
        }
        writeText(codePath, outputPath, dist);
    }

    protected Map<Character, String> readMetaData(String metaFilePath){
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(
                        new FileInputStream(metaFilePath), StandardCharsets.UTF_8
                )
        ))
        {
            Map<Character, String> dist = new HashMap<>();
            int nextChar;
            boolean isNextIsSymbol = false;
            Character currentCharacter = null;
            StringBuilder currentCode = new StringBuilder();

            while ((nextChar = reader.read()) != -1) {
                char ch = (char) nextChar;

                if ((ch >= 48) && (ch <=57)){ //цифра
                    if (!isNextIsSymbol){  //цифра - часть кодового слова
                        currentCode.append(ch);
                    } else { //цифра - символ текста
                        currentCharacter = ch;
                        isNextIsSymbol = false;
                    }
                } else if (ch == 47){ // '/'
                    if (!isNextIsSymbol){ // '/' экранирует следующий символ
                        isNextIsSymbol = true;
                        dist.put(currentCharacter, currentCode.toString());
                        currentCode = new StringBuilder();
                    } else { // '/' символ текста
                        currentCharacter = ch;
                        isNextIsSymbol = false;
                    }
                } else { // обычный символ текста
                    if (currentCharacter != null){
                        dist.put(currentCharacter, currentCode.toString());
                        currentCode = new StringBuilder();
                    }
                    currentCharacter = ch;
                }
            }
            if (currentCharacter != null){
                dist.put(currentCharacter, currentCode.toString());
            }
            return dist;

        } catch (Exception ex){
            throw new IllegalArgumentException(ex);
        }
    }

    protected void writeText(String codePath, String outputPath, Map<Character, String> dict){
        int bufferLimit = sizeLimit / 4;  //максимум символ будет весить 4 байта

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(
                        new FileInputStream(codePath), StandardCharsets.UTF_8
                ), sizeLimit // считываем символы длины 1 байт
        ))
        {
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(
                            new FileOutputStream(outputPath), StandardCharsets.UTF_8
                    ), bufferLimit // пишем символы произвольной длины
            );

            if (textLogging){
                System.out.print("Раскодированная строка: '");
            }

            int nextChar;
            StringBuilder builder = new StringBuilder();

            DictionaryTreeNode dictionaryTreeRoot = buildDictionaryTree(dict);
            DictionaryTreeWorker worker = new DictionaryTreeWorker(dictionaryTreeRoot);
            Character decodeChar;

            while ((nextChar = reader.read()) != -1) {
                char ch = (char) nextChar;

                decodeChar = worker.moveDown(Integer.parseInt("" + ch));
                if (decodeChar != null){
                    worker.reset();
                    builder.append(decodeChar);
                }

                if (builder.length() >= sizeLimit){
                    writer.write(builder.toString());
                    writer.flush();
                    if (textLogging){
                        System.out.print(builder);
                    }
                    builder = new StringBuilder();
                }
            }
            writer.write(builder.toString());
            writer.flush();
            if (textLogging){
                System.out.print(builder);
                System.out.println("';");
            }
            writer.close();
        } catch (Exception ex){
            throw new IllegalArgumentException(ex);
        }
    }

    protected DictionaryTreeNode buildDictionaryTree(Map<Character, String > dict){
        DictionaryTreeNode root = DictionaryTreeNode.builder()
                .subLeaves(new ArrayList<>(Arrays.asList(new DictionaryTreeNode[encodingAlphabetSize])))
                .build();
        DictionaryTreeNode currentNode = root;
        DictionaryTreeNode nextNode = null;
        for (Map.Entry<Character, String> entry : dict.entrySet()){
            for (int i = 0; i < entry.getValue().length() - 1; i++){
                nextNode = currentNode.getSubLeaves().get(Integer.parseInt("" + entry.getValue().charAt(i)));
                if (nextNode == null){
                    nextNode = DictionaryTreeNode.builder().subLeaves(new ArrayList<>(Arrays.asList(new DictionaryTreeNode[encodingAlphabetSize]))).build();
                    currentNode.getSubLeaves().set(Integer.parseInt("" + entry.getValue().charAt(i)), nextNode);
                }
                currentNode = nextNode;
            }
            nextNode = DictionaryTreeNode.builder().symbol(entry.getKey()).build();
            currentNode.getSubLeaves().set(Integer.parseInt("" + entry.getValue().charAt(entry.getValue().length() - 1)), nextNode);
            currentNode = root;
        }
        return root;
    }
}
