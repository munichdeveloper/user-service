package de.munichdeveloper.user.exception;

public class MalformedTokenException extends RuntimeException {
    public MalformedTokenException() {
        super();
    }

    public MalformedTokenException(String message, Throwable cause) {
        super(message, cause);
    }

    public MalformedTokenException(String message) {
        super(message);
    }

    public MalformedTokenException(Throwable cause) {
        super(cause);
    }
}