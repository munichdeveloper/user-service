package de.munichdeveloper.user.exception;

public class IncorrectSignerException extends RuntimeException {
    public IncorrectSignerException() {
        super();
    }

    public IncorrectSignerException(String message, Throwable cause) {
        super(message, cause);
    }

    public IncorrectSignerException(String message) {
        super(message);
    }

    public IncorrectSignerException(Throwable cause) {
        super(cause);
    }
}
