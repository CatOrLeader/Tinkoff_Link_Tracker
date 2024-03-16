package edu.java.scrapper.rest.api.exceptions;

public class EntryAlreadyExistException extends RuntimeException {
    public EntryAlreadyExistException(String message, Throwable cause) {
        super(message, cause);
    }
}
