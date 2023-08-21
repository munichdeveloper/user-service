package de.munichdeveloper.user.exception;

public class ExpiredTokenException extends RuntimeException {
    public ExpiredTokenException() {
        super();
    }

    public ExpiredTokenException(String message, Throwable cause) {
        super(message, cause);
    }

    public ExpiredTokenException(String message) {
        super(message);
    }

    public ExpiredTokenException(Throwable cause) {
        super(cause);
    }
}
