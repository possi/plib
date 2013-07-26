package de.jaschastarke.database.definition;

import java.util.ArrayList;
import java.util.List;

public class Table {
    protected String name;
    protected List<Column<?>> columns = new ArrayList<Column<?>>();
    
    public Table(final String name) {
        this.name = name;
    }
    
    public void addColumn(final Column<?> col) {
        columns.add(col);
    }
    public List<Column<?>> getColumns() {
        return columns;
    }
    
    public static Table fromClass(final Class<?> cls) {
        javax.persistence.Table t = cls.getAnnotation(javax.persistence.Table.class);
        String n = t.name();
        if (n.isEmpty())
            n = cls.getName();
        
        Table table = new Table(n);
        return table;
    }
}
