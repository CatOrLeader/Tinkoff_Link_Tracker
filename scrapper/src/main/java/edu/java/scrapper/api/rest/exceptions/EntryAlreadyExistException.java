package edu.java.scrapper.api.rest.exceptions;

public class EntryAlreadyExistException extends RuntimeException {
    public EntryAlreadyExistException(String message, Throwable cause) {
        super(message, cause);
    }
}
