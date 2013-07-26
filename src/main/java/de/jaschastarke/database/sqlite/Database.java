package de.jaschastarke.database.sqlite;

import de.jaschastarke.database.Type;

public class Database extends de.jaschastarke.database.db.Database {
    public Database() {
        super("org.sqlite.JDBC");
    }
    public Database(final String driver) {
        super(driver);
    }

    @Override
    public Type getType() {
        return Type.SQLite;
    }

    private DDL ddl;
    @Override
    public DDL getDDL() {
        if (ddl == null) {
            ddl = new DDL(this);
        }
        return ddl;
    }
}
