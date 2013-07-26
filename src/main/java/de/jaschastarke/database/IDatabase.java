package de.jaschastarke.database;

import java.sql.Connection;

import de.jaschastarke.database.db.DDL;

/**
 * @TODO add all needed methods
 */
public interface IDatabase {
    //public Driver getDriver();
    public Type getType();
    public DDL getDDL();
    public Connection getConnection();
}
