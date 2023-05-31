package ru.itis.tsisa.blockchain.exceptions;

public class BlockChainBottomException extends RuntimeException{

    public BlockChainBottomException() {
        super();
    }

    public BlockChainBottomException(String message) {
        super(message);
    }

    public BlockChainBottomException(String message, Throwable cause) {
        super(message, cause);
    }

    public BlockChainBottomException(Throwable cause) {
        super(cause);
    }

    protected BlockChainBottomException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
