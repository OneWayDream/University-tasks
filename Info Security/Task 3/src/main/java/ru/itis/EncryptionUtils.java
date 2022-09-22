package ru.itis.diffie_hellman;

import javax.crypto.spec.DHParameterSpec;
import java.math.BigInteger;
import java.security.*;
import java.util.Random;

public class EncryptionUtils {

    public static BigInteger pow(BigInteger base, BigInteger degree, BigInteger mode){

        if (degree.equals(BigInteger.ONE)) {
            return base;
        }

        if (degree.mod(BigInteger.TWO).equals(BigInteger.ZERO)) {
            BigInteger bigInteger = pow(base, degree.divide(BigInteger.TWO), mode);
            return bigInteger.multiply(bigInteger).mod(mode);
        } else {
            return pow(base, degree.subtract(BigInteger.ONE), mode).multiply(base).mod(mode);
        }
    }

    public static DHParameterSpec generateDhParams(int size){
        try{
            AlgorithmParameterGenerator paramGen = AlgorithmParameterGenerator.getInstance("DH");
            paramGen.init(size);

            AlgorithmParameters params = paramGen.generateParameters();
            return params.getParameterSpec(DHParameterSpec.class);
        } catch (Exception ex){
            throw new IllegalArgumentException(ex);
        }
    }

    public static BigInteger generateSecretKey(int size){
        Random rand = new Random();
        return new BigInteger(size, rand);
    }

    public static BigInteger generateOpenKey(BigInteger secretKey, BigInteger p, BigInteger g){
        return pow(g, secretKey, p);
    }

    public static BigInteger generateSecretSharedKey(BigInteger openKey, BigInteger secretKey, BigInteger p){
        return pow(openKey, secretKey, p);
    }
}
