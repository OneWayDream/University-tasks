package ru.itis.diffie_hellman;

import lombok.Getter;
import lombok.Setter;

import java.math.BigInteger;

@Getter
@Setter
public class Channel {

    protected BigInteger p;
    protected BigInteger g;
    protected BigInteger A;
    protected BigInteger B;

}
