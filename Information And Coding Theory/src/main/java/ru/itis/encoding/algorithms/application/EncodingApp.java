package ru.itis.encoding.algorithms.application;

import ru.itis.encoding.algorithms.core.executors.*;
import ru.itis.encoding.algorithms.loaders.SettingsLoader;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class EncodingApp {

    protected static final String WRONG_COMMAND_MESSAGE = "Wrong command. Enter /help if you need the command list.";

    protected static String inputFilePath;
    protected static String outputFilePath;
    protected static String codeFilePath;
    protected static String extraCodeFilePath;
    protected static String metaFilePath;
    protected static String extraMetaFilePath;
    protected static String probabilitiesFilePath;
    protected static boolean textLogging;
    protected static boolean dictionariesLogging;
    protected static int sizeLimit;
    protected static int encodingAlphabetSize;
    protected static int blockSize;

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String command;
        String[] commandParts;

        System.out.println("Введите рабочую папку (в ней будет создан набор текстовых документов)");
        String projectFolder = sc.nextLine();
//        String projectFolder = "C:\\Users\\v\\Desktop\\New Folder";
        try {
            init(projectFolder);
        } catch (Exception ex){
            System.out.println("Невозможно создать файлы.");
            return;
        }

        boolean isWork = true;
        HuffmanEncodingExecutor huffmanEncodingExecutor;
        ArithmeticEncodingExecutor arithmeticEncodingExecutor;
        BwtTransformationExecutor bwtTransformationExecutor;
        MtfEncodingExecutor mtfEncodingExecutor;
        HammingEncodingExecutor hammingEncodingExecutor;
        System.out.println("Enter the command to choose algorithm to encode your input file. Enter /help if you need the command list.");
        while (isWork){
            command = sc.nextLine().trim();
            commandParts = command.split(" ");
            switch (commandParts[0]){
                case "/e+d":
                    switch (commandParts[1]){
                        case "huf":
                            huffmanEncodingExecutor = HuffmanEncodingExecutor.builder()
                                    .textLogging(textLogging)
                                    .dictionariesLogging(dictionariesLogging)
                                    .sizeLimit(sizeLimit)
                                    .encodingAlphabetSize(encodingAlphabetSize)
                                    .build();
                            huffmanEncodingExecutor.encode(inputFilePath, probabilitiesFilePath, codeFilePath, metaFilePath);
                            if (textLogging && dictionariesLogging){
                                System.out.println("------------------");
                            }
                            huffmanEncodingExecutor.decode(codeFilePath, outputFilePath, metaFilePath);
                            break;
                        case "ari":
                            arithmeticEncodingExecutor = ArithmeticEncodingExecutor.builder()
                                    .textLogging(textLogging)
                                    .dictionariesLogging(dictionariesLogging)
                                    .sizeLimit(sizeLimit)
                                    .build();
                            arithmeticEncodingExecutor.encode(inputFilePath, probabilitiesFilePath, codeFilePath, metaFilePath);
                            if (textLogging || dictionariesLogging){
                                System.out.println("------------------");
                            }
                            arithmeticEncodingExecutor.decode(codeFilePath, outputFilePath, metaFilePath);
                            break;
                        case "b+m":
                            bwtTransformationExecutor = BwtTransformationExecutor.builder()
                                    .textLogging(textLogging)
                                    .dictionariesLogging(dictionariesLogging)
                                    .sizeLimit(sizeLimit)
                                    .encodingAlphabetSize(encodingAlphabetSize)
                                    .build();
                            mtfEncodingExecutor = MtfEncodingExecutor.builder()
                                    .textLogging(textLogging)
                                    .dictionariesLogging(dictionariesLogging)
                                    .sizeLimit(sizeLimit)
                                    .encodingAlphabetSize(encodingAlphabetSize)
                                    .build();
                            bwtTransformationExecutor.convert(inputFilePath, codeFilePath, metaFilePath);
                            if (textLogging || dictionariesLogging){
                                System.out.println("------------------");
                            }
                            mtfEncodingExecutor.encode(codeFilePath, probabilitiesFilePath, extraCodeFilePath, extraMetaFilePath);
                            if (textLogging || dictionariesLogging){
                                System.out.println("------------------");
                            }
                            mtfEncodingExecutor.decode(extraCodeFilePath, codeFilePath, extraMetaFilePath);
                            if (textLogging || dictionariesLogging){
                                System.out.println("------------------");
                            }
                            bwtTransformationExecutor.reconvert(codeFilePath, outputFilePath, metaFilePath);
                            break;
                        case "ham":
                            hammingEncodingExecutor = HammingEncodingExecutor.builder()
                                    .textLogging(textLogging)
                                    .dictionariesLogging(dictionariesLogging)
                                    .sizeLimit(sizeLimit)
                                    .build();
                            hammingEncodingExecutor.encode(inputFilePath, codeFilePath, metaFilePath, blockSize);
                            if (textLogging || dictionariesLogging){
                                System.out.println("------------------");
                            }
                            hammingEncodingExecutor.decode(codeFilePath, outputFilePath, metaFilePath);
                            break;
                        default:
                            System.out.println("Тип алгоритма задан неверно.");
                            break;
                    }
                    break;
                case "/e":
                    switch (commandParts[1]){
                        case "huf":
                            huffmanEncodingExecutor = HuffmanEncodingExecutor.builder()
                                    .textLogging(textLogging)
                                    .dictionariesLogging(dictionariesLogging)
                                    .sizeLimit(sizeLimit)
                                    .encodingAlphabetSize(encodingAlphabetSize)
                                    .build();
                            huffmanEncodingExecutor.encode(inputFilePath, probabilitiesFilePath, codeFilePath, metaFilePath);
                            break;
                        case "ari":
                            arithmeticEncodingExecutor = ArithmeticEncodingExecutor.builder()
                                    .textLogging(textLogging)
                                    .dictionariesLogging(dictionariesLogging)
                                    .sizeLimit(sizeLimit)
                                    .build();
                            arithmeticEncodingExecutor.encode(inputFilePath, probabilitiesFilePath, codeFilePath, metaFilePath);
                            break;
                        case "b+m":
                            bwtTransformationExecutor = BwtTransformationExecutor.builder()
                                    .textLogging(textLogging)
                                    .dictionariesLogging(dictionariesLogging)
                                    .sizeLimit(sizeLimit)
                                    .encodingAlphabetSize(encodingAlphabetSize)
                                    .build();
                            mtfEncodingExecutor = MtfEncodingExecutor.builder()
                                    .textLogging(textLogging)
                                    .dictionariesLogging(dictionariesLogging)
                                    .sizeLimit(sizeLimit)
                                    .encodingAlphabetSize(encodingAlphabetSize)
                                    .build();
                            bwtTransformationExecutor.convert(inputFilePath, codeFilePath, metaFilePath);
                            if (textLogging || dictionariesLogging){
                                System.out.println("------------------");
                            }
                            mtfEncodingExecutor.encode(codeFilePath, probabilitiesFilePath, extraCodeFilePath, extraMetaFilePath);
                            break;
                        case "ham":
                            hammingEncodingExecutor = HammingEncodingExecutor.builder()
                                    .textLogging(textLogging)
                                    .dictionariesLogging(dictionariesLogging)
                                    .sizeLimit(sizeLimit)
                                    .build();
                            hammingEncodingExecutor.encode(inputFilePath, codeFilePath, metaFilePath, blockSize);
                            break;
                        default:
                            System.out.println("Тип алгоритма задан неверно.");
                            break;
                    }
                    break;
                case "/d":
                    switch (commandParts[1]){
                        case "huf":
                            huffmanEncodingExecutor = HuffmanEncodingExecutor.builder()
                                    .textLogging(textLogging)
                                    .dictionariesLogging(dictionariesLogging)
                                    .sizeLimit(sizeLimit)
                                    .encodingAlphabetSize(encodingAlphabetSize)
                                    .build();
                            huffmanEncodingExecutor.decode(codeFilePath, outputFilePath, metaFilePath);
                            break;
                        case "ari":
                            arithmeticEncodingExecutor = ArithmeticEncodingExecutor.builder()
                                    .textLogging(textLogging)
                                    .dictionariesLogging(dictionariesLogging)
                                    .sizeLimit(sizeLimit)
                                    .build();
                            arithmeticEncodingExecutor.decode(codeFilePath, outputFilePath, metaFilePath);
                            break;
                        case "b+m":
                            bwtTransformationExecutor = BwtTransformationExecutor.builder()
                                    .textLogging(textLogging)
                                    .dictionariesLogging(dictionariesLogging)
                                    .sizeLimit(sizeLimit)
                                    .encodingAlphabetSize(encodingAlphabetSize)
                                    .build();
                            mtfEncodingExecutor = MtfEncodingExecutor.builder()
                                    .textLogging(textLogging)
                                    .dictionariesLogging(dictionariesLogging)
                                    .sizeLimit(sizeLimit)
                                    .encodingAlphabetSize(encodingAlphabetSize)
                                    .build();
                            mtfEncodingExecutor.encode(codeFilePath, probabilitiesFilePath, extraCodeFilePath, extraMetaFilePath);
                            if (textLogging || dictionariesLogging){
                                System.out.println("------------------");
                            }
                            bwtTransformationExecutor.reconvert(codeFilePath, outputFilePath, metaFilePath);
                            break;
                        case "ham":
                            hammingEncodingExecutor = HammingEncodingExecutor.builder()
                                    .textLogging(textLogging)
                                    .dictionariesLogging(dictionariesLogging)
                                    .sizeLimit(sizeLimit)
                                    .build();
                            hammingEncodingExecutor.decode(codeFilePath, outputFilePath, metaFilePath);
                            break;
                        default:
                            System.out.println("Тип алгоритма задан неверно.");
                            break;
                    }
                    break;
                case "/set":
                    try{
                        switch (commandParts[1]){
                            case "text-logging-flag":
                                textLogging = Boolean.parseBoolean(commandParts[2]);
                                break;
                            case "dictionaries-logging-flag":
                                dictionariesLogging = Boolean.parseBoolean(commandParts[2]);
                                break;
                            case "size-limit":
                                int newSizeLimit = Integer.parseInt(commandParts[2]);
                                if (newSizeLimit > 0){
                                    sizeLimit = Integer.parseInt(commandParts[2]);
                                } else {
                                    System.out.println(WRONG_COMMAND_MESSAGE);
                                }
                                break;
                            case "encoding-alphabet-size":
                                int newEncodingAlphabetSize = Integer.parseInt(commandParts[2]);
                                if  ((newEncodingAlphabetSize >= 2) && (newEncodingAlphabetSize <=10)) {
                                    encodingAlphabetSize = newEncodingAlphabetSize;
                                } else {
                                    System.out.println(WRONG_COMMAND_MESSAGE);
                                }
                                break;
                            case "block-size":
                                int newBlockSize = Integer.parseInt(commandParts[2]);
                                if (newBlockSize > 0) {
                                    blockSize = newBlockSize;
                                } else {
                                    System.out.println(WRONG_COMMAND_MESSAGE);
                                }
                                break;
                            case "input-path":
                                inputFilePath = command.substring(commandParts[0].length() + commandParts[1].length() + 2);
                                break;
                            case "output-path":
                                outputFilePath = command.substring(commandParts[0].length() + commandParts[1].length() + 2);
                                break;
                            case "code-path":
                                codeFilePath = command.substring(commandParts[0].length() + commandParts[1].length() + 2);
                                break;
                            case "meta-path":
                                metaFilePath = command.substring(commandParts[0].length() + commandParts[1].length() + 2);
                                break;
                            default:
                                System.out.println(WRONG_COMMAND_MESSAGE);
                                break;
                        }
                    } catch (Exception ex){
                        System.out.println(WRONG_COMMAND_MESSAGE);
                    }
                    break;
                case "/help":
                    System.out.println("/set [param] [param-value] - установить значение параметру кодирования");
                    System.out.println("Изменяемые параметры:");
                    System.out.println("\t- 'text-logging-flag' (true / false, Default = false) - флаг, включающий вывод" +
                            " в консоль считанного текста, кода и декодированного текста");
                    System.out.println("\t- 'dictionaries-logging-flag' (true / false, Default = false) - флаг, включающий" +
                            " вывод в консоль промежуточных составленных структур для кодирования");
                    System.out.println("\t- 'size-limit' (целое положительное число, Default = 65536) - количество байт, ограничивающее" +
                            " размер структур алгоритмов, которые можно ограничить");
                    System.out.println("\t- 'encoding-alphabet-size' (число в отрезке [2-10], Default = 2) - алфавит для кодирования / декодирования." +
                            "Внимание! Алгоритмы Арифметического кодирование и Хемминга работают всегда с двоичной системой");
                    System.out.println("\t- 'block-size' (целое положительное число, Default = 4) - размер блока для алгоритма Хемминга");
                    System.out.println("\t- 'input-path' - абсолютный путь до входного файла");
                    System.out.println("\t- 'code-path' - абсолютный путь до кодового файла");
                    System.out.println("\t- 'output-path' - абсолютный путь до выходного файла");
                    System.out.println("\t- 'meta-path' - абсолютный путь до мета-файла");
                    System.out.println("/e [algorithm] - закодировать input.txt файл алгоритмом в code.txt");
                    System.out.println("/d [algorithm] - декодировать code.txt файл алгоритмом в output.txt");
                    System.out.println("/e+d [algorithm] - декодировать code.txt файл алгоритмом в output.txt");
                    System.out.println("Алгоритмы:");
                    System.out.println("\t- 'huf' - алгоритм Хаффмана");
                    System.out.println("\t- 'ari' - арифметическое кодирование");
                    System.out.println("\t- 'b+m' - BWT + MTF");
                    System.out.println("\t- 'ham' - алгоритм Хемминга");
                    System.out.println("/exit - завершить работу программы");
                    break;
                case "/exit":
                    isWork = false;
                    break;
                default:
                    System.out.println(WRONG_COMMAND_MESSAGE);
                    break;
            }
            System.out.println("Done c:");
        }
    }

    protected static void init(String projectFolder){
        try{
            SettingsLoader settingsLoader = new SettingsLoader();
            inputFilePath = projectFolder + "\\" + settingsLoader.getInputFilePath();
            createFileIfNotExists(inputFilePath);
            outputFilePath = projectFolder + "\\" + settingsLoader.getOutputFilePath();
            createFileIfNotExists(outputFilePath);
            codeFilePath = projectFolder + "\\" + settingsLoader.getCodeFilePath();
            createFileIfNotExists(codeFilePath);
            extraCodeFilePath = projectFolder + "\\" + settingsLoader.getExtraCodeFilePath();
            createFileIfNotExists(extraCodeFilePath);
            metaFilePath = projectFolder + "\\" + settingsLoader.getMetaFilePath();
            createFileIfNotExists(metaFilePath);
            extraMetaFilePath = projectFolder + "\\" + settingsLoader.getExtraMetaFilePath();
            createFileIfNotExists(extraMetaFilePath);
            probabilitiesFilePath = projectFolder + "\\" + settingsLoader.getProbabilitiesFilePath();
            createFileIfNotExists(probabilitiesFilePath);
            textLogging = settingsLoader.getTextLoggingFlag();
            dictionariesLogging = settingsLoader.getDictionariesLoggingFlag();
            sizeLimit = settingsLoader.getSizeLimit();
            encodingAlphabetSize = settingsLoader.getEncodingAlphabetSize();
            blockSize = settingsLoader.getBlockSize();
        } catch (Exception ex){
            throw new IllegalArgumentException(ex);
        }
    }

    protected static void createFileIfNotExists(String filePath) throws IOException {
        File newFile = new File(filePath);
        System.out.println("Создаю файл: "+ filePath);
        newFile.createNewFile();
    }

}
