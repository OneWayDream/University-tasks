package ru.itis.server.exceptions;

public class ClientThreadException extends IllegalStateException {
    public ClientThreadException() {
    }

    public ClientThreadException(String s) {
        super(s);
    }

    public ClientThreadException(String message, Throwable cause) {
        super(message, cause);
    }

    public ClientThreadException(Throwable cause) {
        super(cause);
    }
}
