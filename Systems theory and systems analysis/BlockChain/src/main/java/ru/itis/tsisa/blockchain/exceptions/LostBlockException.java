package ru.itis.tsisa.blockchain.exceptions;

public class LostBlockException extends RuntimeException {

    public LostBlockException() {
        super();
    }

    public LostBlockException(String message) {
        super(message);
    }

    public LostBlockException(String message, Throwable cause) {
        super(message, cause);
    }

    public LostBlockException(Throwable cause) {
        super(cause);
    }

    protected LostBlockException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
