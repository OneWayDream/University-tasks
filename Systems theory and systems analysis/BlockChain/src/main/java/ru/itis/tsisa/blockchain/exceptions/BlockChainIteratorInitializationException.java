package ru.itis.tsisa.blockchain.exceptions;

public class BlockChainIteratorInitializationException extends RuntimeException {

    public BlockChainIteratorInitializationException() {
        super();
    }

    public BlockChainIteratorInitializationException(String message) {
        super(message);
    }

    public BlockChainIteratorInitializationException(String message, Throwable cause) {
        super(message, cause);
    }

    public BlockChainIteratorInitializationException(Throwable cause) {
        super(cause);
    }

    protected BlockChainIteratorInitializationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
