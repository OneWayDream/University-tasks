package ru.itis.diffie_hellman;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.math.BigInteger;

@Getter
@RequiredArgsConstructor
public class Bob {

    protected BigInteger p;
    protected BigInteger g;
    protected BigInteger A;
    protected BigInteger B;
    protected BigInteger b;
    protected BigInteger key;

    @NonNull
    protected final int pNumberSize;
    @NonNull
    protected final int secretKeySize;

    public void receiveOpenKey(Channel channel){
        p = channel.getP();
        g = channel.getG();
        A = channel.getA();
    }

    public void sendOpenKey(Channel channel){
        b = EncryptionUtils.generateSecretKey(secretKeySize);
        B = EncryptionUtils.generateOpenKey(b, p, g);

        channel.setB(B);
    }

    public void generateSecretSharedKey(){
        key = EncryptionUtils.generateSecretSharedKey(A, b, p);
    }

}
