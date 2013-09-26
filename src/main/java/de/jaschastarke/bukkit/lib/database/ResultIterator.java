package de.jaschastarke.bukkit.lib.database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;

public abstract class ResultIterator<E> implements Iterable<E> {
    private ResultSet rs;
    
    public ResultIterator(final ResultSet set) {
        rs = set;
    }
    
    protected abstract E fetch(ResultSet resultSet) throws SQLException;
    
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            private boolean fetched = false;
            private boolean has = false;
            @Override
            public boolean hasNext() {
                if (!fetched) {
                    fetched = true;
                    try {
                        has = rs.next();
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                }
                return has;
            }
            
            @Override
            public void remove() {
                throw new IllegalAccessError("Not supported");
            }
            
            public E next() {
                if (hasNext()) {
                    fetched = false;
                    try {
                        return fetch(rs);
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    try {
                        rs.close();
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                    return null;
                }
            }
        };
    }
}
