package ru.itis.protocol.exceptions;

public class FrameContentException extends Exception {

    public FrameContentException() {
    }

    public FrameContentException(String message) {
        super(message);
    }

    public FrameContentException(String message, Throwable cause) {
        super(message, cause);
    }

    public FrameContentException(Throwable cause) {
        super(cause);
    }

    public FrameContentException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
