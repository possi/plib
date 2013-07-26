package de.jaschastarke.database;

public enum Type {
    MySQL,
    SQLite,
    UNKNOWN;
    
    private static final String JDBC_PREFIX = "jdbc:";
    public static Type getType(final String url) {
        String u = url.toLowerCase();
        if (u.startsWith(JDBC_PREFIX))
            u = u.substring(JDBC_PREFIX.length());
        if (u.startsWith("mysql"))
            return MySQL;
        else if (u.startsWith("sqlite"))
            return SQLite;
        else
            return UNKNOWN;
    }
}
