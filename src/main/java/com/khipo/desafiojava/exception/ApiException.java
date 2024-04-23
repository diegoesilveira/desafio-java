package com.khipo.desafiojava.exception;

public class ApiException extends RuntimeException {
    private final String message;
    private final Throwable cause;

    public ApiException(String message) {
        this(message, null);
    }

    public ApiException(String message, Throwable cause) {
        super(message, cause);
        this.message = message;
        this.cause = cause;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public Throwable getCause() {
        return cause;
    }
}
