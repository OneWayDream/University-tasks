package ru.itis.tsisa.blockchain.exceptions;

public class AuditorConnectionException extends RuntimeException{

    public AuditorConnectionException() {
        super();
    }

    public AuditorConnectionException(String message) {
        super(message);
    }

    public AuditorConnectionException(String message, Throwable cause) {
        super(message, cause);
    }

    public AuditorConnectionException(Throwable cause) {
        super(cause);
    }
}
