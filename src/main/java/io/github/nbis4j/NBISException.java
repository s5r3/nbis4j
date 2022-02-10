package io.github.nbis4j;

public class NBISException extends RuntimeException {
    public final int errorCode;

    public NBISException(String message, int errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public NBISException(String message, Throwable error, int errorCode) {
        super(message, error);
        this.errorCode = errorCode;
    }
}
