package de.jaschastarke.modularize;

public class InvalidStateException extends RuntimeException {
    public InvalidStateException(final String string) {
        super(string);
    }

    private static final long serialVersionUID = 1L;

}