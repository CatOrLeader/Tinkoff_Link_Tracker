package edu.java.scrapper.rest.api.exception;

public class LinkUnsupportedException extends RuntimeException {
    public LinkUnsupportedException() {
        super("Link is unsupported");
    }
}
