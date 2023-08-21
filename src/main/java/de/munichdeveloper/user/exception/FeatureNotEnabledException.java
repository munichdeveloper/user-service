package de.munichdeveloper.user.exception;

public class FeatureNotEnabledException extends RuntimeException {
    public FeatureNotEnabledException() {
        super();
    }

    public FeatureNotEnabledException(String message, Throwable cause) {
        super(message, cause);
    }

    public FeatureNotEnabledException(String message) {
        super(message);
    }

    public FeatureNotEnabledException(Throwable cause) {
        super(cause);
    }
}
