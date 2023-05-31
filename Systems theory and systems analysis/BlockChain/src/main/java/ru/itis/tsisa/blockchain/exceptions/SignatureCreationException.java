package ru.itis.tsisa.blockchain.exceptions;

public class SignatureCreationException extends RuntimeException {

    public SignatureCreationException() {
        super();
    }

    public SignatureCreationException(String message) {
        super(message);
    }

    public SignatureCreationException(String message, Throwable cause) {
        super(message, cause);
    }

    public SignatureCreationException(Throwable cause) {
        super(cause);
    }

    protected SignatureCreationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
