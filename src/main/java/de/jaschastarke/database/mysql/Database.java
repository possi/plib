package de.jaschastarke.database.mysql;

import de.jaschastarke.database.Type;

public class Database extends de.jaschastarke.database.db.Database {
    public Database() {
        super("com.mysql.jdbc.Driver");
    }
    public Database(final String driver) {
        super(driver);
    }

    @Override
    public Type getType() {
        return Type.MySQL;
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
