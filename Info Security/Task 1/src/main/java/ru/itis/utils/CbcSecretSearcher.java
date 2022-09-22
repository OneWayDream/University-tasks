package ru.itis.utils;

import lombok.Builder;
import lombok.Data;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

@Data
@Builder
public class CbcSecretSearcher {

    protected EncryptionExecutor executor;
    protected int blockSize;
    protected Boolean logging;

    public byte[] findSecret(){

        int secretLength = findLength(); // длина секретной последовательности
        byte[] result = new byte[secretLength]; // итоговая секретная последовательность
        int messageLength = calculatePaddingSize(secretLength); // длина сообщений, которые мы будем отправлять
        //P.s. Нам нужно, чтобы она была не меньше секретной последовательности
        int workingIndex = messageLength - 1; // Индекс сообщения, который будет использоваться для перебора
        byte[] currentMessage; // текущее сообщение для отправки
        byte[] searchingEncryptedBytes = new byte[messageLength]; // байты, которые мы получили до workingIndex позиции в зашифрованном ответе
        byte currentByte; // текущий байт перебора
        byte[] currentEncryptedBytes; // текущий зашифрованный байт, который мы получили на позиции workingIndex подстановкой currentByte
        byte[] receivedEncryptedMessage; // для дебага

        for (int i = 0; i < secretLength; i++){ // проходимся снизу вверх
            currentMessage = new byte[messageLength - 1 - i]; // формируем сообщение, которое поможет узнать зашифрованное отображение нужного бита
            receivedEncryptedMessage = executor.encryptString(new String(currentMessage, StandardCharsets.ISO_8859_1))
                    .getBytes(StandardCharsets.ISO_8859_1); //принимаем зашифрованный ответ
            //Записываем первую часть массива как ту, которую мы будем искать (её мы оставим неизменной)
            System.arraycopy(receivedEncryptedMessage, 0, searchingEncryptedBytes, 0, searchingEncryptedBytes.length);

            System.out.println(Arrays.toString(currentMessage));
            System.out.println(Arrays.toString(receivedEncryptedMessage));

            currentByte = -127; // задаём нижнюю границу перебора
            currentEncryptedBytes = searchingEncryptedBytes.clone(); // создаём переменную под текущий вектор на основе необходимого
            currentEncryptedBytes[0] = (byte) (currentEncryptedBytes[0] - 1); // меняем его, чтобы войти в цикл

            currentMessage = new byte[messageLength]; // создаём новое сообщение для перебора, состоящее из пустых символов, уже известных нам и переборного
            if (i != 0){ // проверяем, есть ли у нас известные символы и нужно ли их копировать
                System.arraycopy(result, 0, currentMessage, messageLength - 1 - i, i); // копируем известные байты
            }
            currentMessage[workingIndex] = currentByte; // вставляем переборный байт

            while (!Arrays.equals(currentEncryptedBytes, searchingEncryptedBytes)){ // ищем до совпадения
                receivedEncryptedMessage = executor.encryptString(new String(currentMessage, StandardCharsets.ISO_8859_1))
                        .getBytes(StandardCharsets.ISO_8859_1);
                System.arraycopy(receivedEncryptedMessage, 0, currentEncryptedBytes, 0, currentEncryptedBytes.length);
                // двигаемся в переборе
                currentByte++;
                currentMessage[workingIndex] = currentByte;
            }

            System.out.println(Arrays.toString(currentMessage));
            System.out.println(Arrays.toString(receivedEncryptedMessage));

            result[i] = (byte) (currentByte - 1); // фиксируем

            System.out.println("-----------------------------------------");
        }
        return result;
    }

    protected int findLength(){
        byte[] sequenceToCheck = new byte[1];
        int encryptResultLength = executor.encryptString(new String(sequenceToCheck, StandardCharsets.ISO_8859_1))
                .getBytes(StandardCharsets.ISO_8859_1)
                .length;
        int blocksAmount = encryptResultLength / blockSize;
        int currentEncryptResultLength = encryptResultLength;
        int n = 2;
        while (currentEncryptResultLength == encryptResultLength){
            sequenceToCheck = new byte[n];
            currentEncryptResultLength = executor.encryptString(new String(sequenceToCheck, StandardCharsets.ISO_8859_1))
                    .getBytes(StandardCharsets.ISO_8859_1)
                    .length;
            n++;
        }
        return blocksAmount * blockSize - (n-1);
    }

    protected int calculatePaddingSize(int messageLength){
        return messageLength + (blockSize - (messageLength % blockSize));
    }

}
