package de.jaschastarke.database.db;

import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class DDL {
    private Database db;
    public DDL(final Database db) {
        this.db = db;
    }
    protected Database getAbstractDatabase() {
        return db;
    }
    public boolean tableExists(final String tableName) throws SQLException {
        ResultSet tables = db.getConnection().getMetaData().getTables(null, null, tableName, null);
        return tables.next();
    }
}
