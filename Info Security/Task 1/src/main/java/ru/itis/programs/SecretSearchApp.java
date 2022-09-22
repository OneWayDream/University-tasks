package ru.itis.programs;

import ru.itis.utils.CbcSecretSearcher;
import ru.itis.utils.CustomAESExecutor;

import java.util.Arrays;

public class SecretSearchApp {

    protected static final int KEY_LENGTH = 256;
    protected static final String PASSWORD = null;
    protected static final String SALT = null;
    protected static final String ALGORITHM = "AES/CBC/PKCS5Padding";
    protected static final byte[] SECRET = new byte[]{0, -1, 2, -3, 4, -5, 6, -7, 8, -9, 10, -11, 12, 126, 14, -15, 16, -17, 18, -19, 20, -21, 22, -23};

    public static void main(String[] args) throws Throwable{
        CustomAESExecutor executor = new CustomAESExecutor(KEY_LENGTH, PASSWORD, SALT, ALGORITHM, SECRET);
        CbcSecretSearcher searcher = CbcSecretSearcher.builder()
                .executor(executor)
                .logging(true)
                .blockSize(16)
                .build();
        System.out.println(Arrays.toString(searcher.findSecret()));
    }

}
