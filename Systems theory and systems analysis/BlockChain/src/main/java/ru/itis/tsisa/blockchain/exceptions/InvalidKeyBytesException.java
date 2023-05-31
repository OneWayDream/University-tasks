package ru.itis.tsisa.blockchain.exceptions;

public class InvalidKeyBytesException extends RuntimeException {

    public InvalidKeyBytesException() {
        super();
    }

    public InvalidKeyBytesException(String message) {
        super(message);
    }

    public InvalidKeyBytesException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidKeyBytesException(Throwable cause) {
        super(cause);
    }

    protected InvalidKeyBytesException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
