package ru.itis.diffie_hellman;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.math.BigInteger;

@Getter
@RequiredArgsConstructor
public class Alice {

    protected BigInteger p;
    protected BigInteger g;
    protected BigInteger A;
    protected BigInteger B;
    protected BigInteger a;
    protected BigInteger key;

    @NonNull
    protected final int pNumberSize;
    @NonNull
    protected final int secretKeySize;

    public void sendOpenKey(Channel channel){
        p = EncryptionUtils.generateDhParams(pNumberSize).getP();
        g = p.subtract(BigInteger.ONE);
        a = EncryptionUtils.generateSecretKey(secretKeySize);
        A = EncryptionUtils.generateOpenKey(a, p, g);

        channel.setP(p);
        channel.setG(g);
        channel.setA(A);
    }

    public void receiveOpenKey(Channel channel){
        B = channel.getB();
    }

    public void generateSecretSharedKey(){
        key = EncryptionUtils.generateSecretSharedKey(B, a, p);
    }


}
