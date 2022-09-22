package ru.itis.encoding.algorithms.core.executors;

import lombok.Builder;
import lombok.Data;
import lombok.SneakyThrows;

import java.io.*;
import java.nio.charset.StandardCharsets;

@Data
@Builder
public class HammingEncodingExecutor {

    protected boolean textLogging;
    protected boolean dictionariesLogging;
    protected int sizeLimit;

    public void encode(String inputPath, String codePath, String metaFilePath, int blockSize){
        writeMetaData(metaFilePath, blockSize);
        writeCode(inputPath, codePath, blockSize);
    }

    protected void writeMetaData(String metaFilePath, int blockSize){
        try{
            int writerLimit = sizeLimit / 4;  //максимум символ будет весить 4 байта

            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(
                            new FileOutputStream(metaFilePath), StandardCharsets.UTF_8
                    ), writerLimit
            );
            writer.write("" + blockSize);
            writer.flush();
            writer.close();
        } catch (Exception ex){
            throw new IllegalArgumentException(ex);
        }
    }

    protected void writeCode(String inputPath, String codePath, int blockSize){
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

            int nextChar;
            StringBuilder stringToConvert = new StringBuilder();
            StringBuilder stringToSend = new StringBuilder();

            //считаем длину блока, чтобы до переполнения буфера делать отправку
            long sizeToFlush = sizeLimit - blockSize;
            int s = 1;
            while (s <= blockSize){
                sizeToFlush++;
                s*=2;
            }

            //считываем текст
            while ((nextChar = reader.read()) != -1) {
                char ch = (char) nextChar;
                byte[] bytes = ("" + ch).getBytes(StandardCharsets.UTF_8);
                for (int i = 0; i < bytes.length; i++){
                    stringToConvert.append(conv(bytes[i]));
                }
                if (stringToConvert.length() >= blockSize){

                    while (stringToConvert.length() >= blockSize){
                        stringToSend.append(getHammingCode(stringToConvert.substring(0, blockSize)));
                        stringToConvert.delete(0, blockSize);

                        if (stringToSend.length() >= sizeToFlush){
                            writer.write(stringToSend.toString());
                            writer.flush();
                            if (textLogging){
                                System.out.print(stringToSend);
                            }
                            stringToSend = new StringBuilder();
                        }
                    }
                }
            }
            if (stringToConvert.length() != 0){
                stringToSend.append(getHammingCode(stringToConvert.toString()));
            }

            writer.write(stringToSend.toString());
            writer.flush();
            if (textLogging){
                System.out.print(stringToSend);
                System.out.println("");
            }
            writer.close();
        } catch (Exception ex){
            throw new IllegalArgumentException(ex);
        }
    }

    protected String getHammingCode(String str){
        StringBuilder currentBlock = new StringBuilder();
        currentBlock.append(str);
        int indexToInsert = 1;
        while (indexToInsert <= currentBlock.length()){ // вставляем пустые служебные символы
            currentBlock.insert(indexToInsert - 1, "x"); // х для себя - чтобы проверить было удобно)
            indexToInsert = indexToInsert * 2;
        }
        indexToInsert = 1;
        while (indexToInsert <= currentBlock.length()){
            boolean odd = false; //чётность-нечётность наблюдаемых битов для данного бита
            int k = indexToInsert; // начальная позиция
            int counter = 0; //сколько блоков было считано с блока
            for (int i = 0; i < currentBlock.length(); i++){
                if ((i == k - 1) || (counter != 0)){ //если мы в начале блока ИЛИ в блоке, который под наблюдением
                    if (currentBlock.charAt(i) == '1'){
                        odd = !odd; // меняем чётность
                    }
                    counter++; // увеличиваем количество символов, считанных в блоке
                    if (counter == indexToInsert){ // если мы считали все символы в блоке
                        counter = 0; // обнуляем счётчик
                        i = i + (indexToInsert); // сдвигаемся на следующий блок сразу же
                        k+=indexToInsert*2;
                    }
                }
            }
            currentBlock.setCharAt(indexToInsert - 1, odd ? '1' : '0'); //вставляем блок в зависимости от нечётности
            indexToInsert = indexToInsert * 2; // переходим к следующему служебному биту
        }
        return currentBlock.toString();
    }

    protected String conv(byte num) {
//        StringBuilder result = new StringBuilder();
//        String minus = "";
//        if (num < 0){
//            num = num * -1;
//            minus = "1";
//        }
//        while(num>0) {
//            result.insert(0, num % to);
//            num=num/to;
//        }
//        int extraZeroes = 8 - result.length();
//        if (minus.length() == 1){
//            extraZeroes--;
//        }
//        for (int j = 0; j < extraZeroes; j++){
//            result.insert(0, '0');
//        }
        StringBuilder result = new StringBuilder();
        result.append(Integer.toBinaryString((num+256)%256));
        int extraZeroes = 8 - result.length();
        for (int j = 0; j < extraZeroes; j++){
            result.insert(0, '0');
        }
        return result.toString();
    }

    public void decode(String codeFilePath, String outputPath, String metaFilePath){
        Integer blockSize = readBlockSize(metaFilePath);
        writeText(codeFilePath, outputPath, blockSize);
    }

    protected Integer readBlockSize(String metaFilePath){
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(
                        new FileInputStream(metaFilePath), StandardCharsets.UTF_8
                )
        ))
        {
            String nextString = reader.readLine();
            return Integer.parseInt(nextString);

        } catch (Exception ex){
            throw new IllegalArgumentException(ex);
        }
    }

    protected void writeText(String codePath, String output, Integer blockSize) {
        int bufferLimit = sizeLimit / 4;  //максимум символ будет весить 4 байта

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(
                        new FileInputStream(codePath), StandardCharsets.UTF_8
                ), sizeLimit
        )) {

            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(
                            new FileOutputStream(output), StandardCharsets.UTF_8
                    ), bufferLimit
            );

            int nextChar;
            StringBuilder stringToConvert = new StringBuilder();
            StringBuilder stringToSend = new StringBuilder();
            int counter = 0; //счётчик блоков

            //считаем длину блока, чтобы до переполнения буфера делать отправку
            long sizeToFlush = sizeLimit - blockSize;
            int s = 1;
            while (s <= blockSize){ //увеличиваем количество символов в блоке, т.к. он расширен
                blockSize++;
                s*=2;
            }

            //считываем текс
            while ((nextChar = reader.read()) != -1) {
                char ch = (char) nextChar;
                stringToConvert.append(ch);
                if (stringToConvert.length() >= blockSize){
                    while (stringToConvert.length() >= blockSize){
                        stringToSend.append(decodeHammingCode(stringToConvert.substring(0, blockSize), counter));
                        stringToConvert.delete(0, blockSize);
                        counter++;

                        if (stringToSend.length() >= sizeToFlush){
                            int rest = stringToSend.length() % 8;
                            int bytesToLeave = checkLastBytes(stringToSend.substring(stringToSend.length() - 32 - rest, stringToSend.length() - rest));
                            writer.write(decodeToUtf8(stringToSend.substring(0, stringToSend.length() - bytesToLeave * 8 - rest)));
                            writer.flush();
                            if (textLogging){
                                System.out.print(stringToSend);
                            }
                            stringToSend = new StringBuilder(stringToSend.substring(stringToSend.length() - bytesToLeave * 8 - rest));
                        }
                    }
                }
            }
            if (stringToConvert.length() != 0){
                stringToSend.append(decodeHammingCode(stringToConvert.toString(), counter));
            }

            writer.write(decodeToUtf8(stringToSend.toString()));
            writer.flush();
            if (textLogging){
                System.out.print(stringToSend);
            }
            writer.close();
        } catch (Exception ex){
            throw new IllegalArgumentException(ex);
        }
    }

    protected String decodeHammingCode(String code, int blocksCounter){
        StringBuilder currentBlock = new StringBuilder();
        currentBlock.append(code);
        int serviceIndex = 1;
        int errorIndex = 0;
        while (serviceIndex <= currentBlock.length()){
            boolean odd = false; //чётность-нечётность наблюдаемых битов для данного бита
            int k = serviceIndex; // начальная позиция
            int counter = 0; //сколько блоков было считано с блока
            for (int i = 0; i < currentBlock.length(); i++){
                if ((i == k - 1) || (counter != 0)){ //если мы в начале блока ИЛИ в блоке, который под наблюдением
                    if (i != serviceIndex - 1){ // учитываем, что не нужно добавлять в сумму служебные байты
                        if (currentBlock.charAt(i) == '1'){
                            odd = !odd; // меняем чётность
                        }
                    }
                    counter++; // увеличиваем количество символов, считанных в блоке
                    if (counter == serviceIndex){ // если мы считали все символы в блоке
                        counter = 0; // обнуляем счётчик
                        i = i + (serviceIndex); // сдвигаемся на следующий блок сразу же
                        k+=serviceIndex*2;
                    }
                }
            }
            if ((odd ? '1' : '0') != currentBlock.charAt(serviceIndex - 1)){
                errorIndex+=serviceIndex;
            }
            serviceIndex = serviceIndex * 2; // переходим к следующему служебному биту
        }
        if (errorIndex != 0){
            int newSymbol = 1 - Integer.parseInt("" + currentBlock.charAt(errorIndex - 1));
            System.out.print("Произошла ошибка в блоке " + blocksCounter + ". Замена: " + currentBlock + " -> ");
            currentBlock.setCharAt(errorIndex - 1, ("" + newSymbol).charAt(0));
            System.out.println(currentBlock);
        }
        serviceIndex = serviceIndex / 2;
        while (serviceIndex > 0){
            currentBlock.deleteCharAt(serviceIndex - 1);
            serviceIndex = serviceIndex / 2;
        }
        return currentBlock.toString();
    }


    protected String decodeToUtf8(String str){
        byte[] arr = new byte[str.length() / 8];
        for (int i = 0; i < str.length() / 8; i++){
            arr[i] = (byte)(int)Integer.valueOf(str.substring(i*8, (i+1)*8), 2);
        }
        return new String(arr, 0, arr.length, StandardCharsets.UTF_8);
    }

    protected int checkLastBytes (String str){
        int counter = 0;
        int bytesToSave = 0;
        while (counter < 4){
            //начало символа, на который требуется один октет или один из цепочных октетов
            if ((str.charAt(counter * 8) == '0')||(str.substring(counter * 8, counter* 8 + 2).equals("10"))){
                counter++;
            } else if (str.substring(counter * 8, counter * 8 + 3).equals("110")){ //начало символа, на который требуется два октета
                if (3 - counter < 1){
                    bytesToSave = 1;
                }
                counter+=2;
            } else if (str.substring(counter * 8, counter * 8 + 4).equals("1110")){ //начало символа, на который требуется три октета
                if (3 - counter < 2){
                    bytesToSave = 2 - (3 - counter);
                }
                counter+=3;
            } else if (str.substring(counter * 8, counter * 8 + 5).equals("11110")){ //начало символа, на который требуется четыре октета
                if (3 - counter < 3){
                    bytesToSave = 3 - (3 - counter);
                }
                counter+=4;
            }
        }
        return bytesToSave;
    }
}
