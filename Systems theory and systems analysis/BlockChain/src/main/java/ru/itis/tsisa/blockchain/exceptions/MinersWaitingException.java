package ru.itis.tsisa.blockchain.exceptions;

public class MinersWaitingException extends RuntimeException {

    public MinersWaitingException() {
        super();
    }

    public MinersWaitingException(String message) {
        super(message);
    }

    public MinersWaitingException(String message, Throwable cause) {
        super(message, cause);
    }

    public MinersWaitingException(Throwable cause) {
        super(cause);
    }

    protected MinersWaitingException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
