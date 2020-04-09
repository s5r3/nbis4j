package io.github.nbis4j;

public class NFIQComputeException extends RuntimeException {
    public final int errorCode;

    public NFIQComputeException(String message, int errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public NFIQComputeException(String message, Throwable error, int errorCode) {
        super(message, error);
        this.errorCode = errorCode;
    }
}
