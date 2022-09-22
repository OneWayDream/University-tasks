package ru.itis.diffie_hellman;

import lombok.Data;

import java.math.BigInteger;

@Data
public class Eva {

    protected BigInteger p;
    protected BigInteger g;
    protected BigInteger A;
    protected BigInteger B;
    protected BigInteger key;

    public void eavesdropChannel(Channel channel){
        p = channel.getP();
        g = channel.getG();
        A = channel.getA();
        B = channel.getB();
    }

    public void findSecretSharedKey(){
        if (g.equals(p.subtract(BigInteger.ONE))){
            if (A.equals(BigInteger.ONE) || B.equals(BigInteger.ONE)){
                key = BigInteger.ONE;
            } else {
                key = g;
            }
        } else {
            System.out.println("There's nothing to be done here :c");
        }
    }

}
