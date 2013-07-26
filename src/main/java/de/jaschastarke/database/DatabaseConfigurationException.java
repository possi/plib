package de.jaschastarke.database;

public class DatabaseConfigurationException extends Exception {
    private static final long serialVersionUID = 6112431610403765488L;

    public DatabaseConfigurationException(final String string) {
        super(string);
    }
    public DatabaseConfigurationException(final String string, final Exception e) {
        super(string, e);
    }
}
