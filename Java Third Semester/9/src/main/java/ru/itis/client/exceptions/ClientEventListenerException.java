package ru.itis.client.exceptions;

public class ClientEventListenerException extends Exception {
    public ClientEventListenerException() {
    }

    public ClientEventListenerException(String message) {
        super(message);
    }

    public ClientEventListenerException(String message, Throwable cause) {
        super(message, cause);
    }

    public ClientEventListenerException(Throwable cause) {
        super(cause);
    }

    public ClientEventListenerException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
