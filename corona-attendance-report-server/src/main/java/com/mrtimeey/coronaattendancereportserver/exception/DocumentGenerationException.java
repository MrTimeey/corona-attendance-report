package com.mrtimeey.coronaattendancereportserver.exception;

public class DocumentGenerationException extends RuntimeException {

    public DocumentGenerationException(String message, Throwable cause) {
        super(message, cause);
    }
}
