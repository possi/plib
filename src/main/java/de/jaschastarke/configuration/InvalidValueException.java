package de.jaschastarke.configuration;

public class InvalidValueException extends Exception {
    private static final long serialVersionUID = -3148372319420118230L;
    
    public InvalidValueException(final String arg0) {
        super(arg0);
    }
    public InvalidValueException(final Throwable e) {
        super(e);
    }
    public InvalidValueException(final String arg0, final Throwable e) {
        super(arg0, e);
    }
}
