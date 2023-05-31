package ru.itis.tsisa.blockchain.exceptions;

public class EncryptionUtilsInitializationException extends RuntimeException {

    public EncryptionUtilsInitializationException() {
        super();
    }

    public EncryptionUtilsInitializationException(String message) {
        super(message);
    }

    public EncryptionUtilsInitializationException(String message, Throwable cause) {
        super(message, cause);
    }

    public EncryptionUtilsInitializationException(Throwable cause) {
        super(cause);
    }

    protected EncryptionUtilsInitializationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
