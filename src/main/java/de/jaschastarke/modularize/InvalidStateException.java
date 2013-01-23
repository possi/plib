package de.jaschastarke.modularize;

public class InvalidStateException extends RuntimeException {
    public InvalidStateException(String string) {
        super(string);
    }

    private static final long serialVersionUID = 1L;

}