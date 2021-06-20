package ru.itis.homework;


import lombok.*;

@Getter
@Setter
@ToString
@EqualsAndHashCode
//@Builder
public class User {

    protected byte myByte;
    protected Byte myLinkByte;
    protected short myShort;
    protected Short myLinkShort;
    protected int myInt;
    protected Integer myLinkInt;
    protected long myLong;
    protected Long id;
    protected boolean myBoolean;
    protected Boolean myLinkBoolean;
    protected float myFloat;
    protected Float myLinkFloat;
    protected double myDouble;
    protected Double myLinkDouble;

    protected String userName;

    public User(){};
}
