package ru.itis.tsisa.blockchain.exceptions;

public class BlockVerificationException extends RuntimeException {

    public BlockVerificationException() {
        super();
    }

    public BlockVerificationException(String message) {
        super(message);
    }

    public BlockVerificationException(String message, Throwable cause) {
        super(message, cause);
    }

    public BlockVerificationException(Throwable cause) {
        super(cause);
    }

    protected BlockVerificationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
