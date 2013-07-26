package de.jaschastarke.database.definition;

public class Column<T> {
    protected String name;
    protected Class<T> javaType;
    protected int len;
    
    public Column(final String name, final Class<T> javaType) {
        this.name = name;
        this.javaType = javaType;
    }
}
