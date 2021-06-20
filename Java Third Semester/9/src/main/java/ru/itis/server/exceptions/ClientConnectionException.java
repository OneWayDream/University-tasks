package ru.itis.server.exceptions;

public class ClientConnectionException extends Exception {
    public ClientConnectionException() {
    }

    public ClientConnectionException(String message) {
        super(message);
    }

    public ClientConnectionException(String message, Throwable cause) {
        super(message, cause);
    }

    public ClientConnectionException(Throwable cause) {
        super(cause);
    }

    public ClientConnectionException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
