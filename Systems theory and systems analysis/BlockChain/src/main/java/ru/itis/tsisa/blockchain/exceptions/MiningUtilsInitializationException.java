package ru.itis.tsisa.blockchain.exceptions;

public class MiningUtilsInitializationException extends RuntimeException {

    public MiningUtilsInitializationException() {
        super();
    }

    public MiningUtilsInitializationException(String message) {
        super(message);
    }

    public MiningUtilsInitializationException(String message, Throwable cause) {
        super(message, cause);
    }

    public MiningUtilsInitializationException(Throwable cause) {
        super(cause);
    }

    protected MiningUtilsInitializationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
