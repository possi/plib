package de.jaschastarke.database.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import de.jaschastarke.database.DatabaseConfigurationException;
import de.jaschastarke.database.IDatabase;
import de.jaschastarke.utils.ISimpleLogger;
import de.jaschastarke.utils.NullLogger;

public abstract class Database implements IDatabase {
    private Class<?> driver = null;
    private Connection connection = null;
    private String driverClass;
    protected ISimpleLogger log = new NullLogger();
    
    public Database() {
    }
    public Database(final String driver) {
        driverClass = driver;
    }
    public void setLogger(final ISimpleLogger newLog) {
        this.log = newLog;
    }
    public ISimpleLogger getLogger() {
        return this.log;
    }
    
    protected void initDriver() throws DatabaseConfigurationException {
        if (driver == null) {
            try {
                driver = Class.forName(driverClass);
            } catch (Exception e) {
                throw new DatabaseConfigurationException("Couldn't find DatabaseDriver-Class " + driverClass + ": " + e.getMessage(), e);
            }
        }
    }

    public void connect(final String url, final Properties properties) throws DatabaseConfigurationException {
        initDriver();
        try {
            connection = DriverManager.getConnection(url, properties);
        } catch (SQLException e) {
            throw new DatabaseConfigurationException("Invalid Database-Configuration: " + e.getMessage(), e);
        }
    }
    /*
    @Override
    public Driver getDriver() {
        return driver;
    }*/
    @Override
    public Connection getConnection() {
        return connection;
    }
    
    public Statement execute(final String sql) throws SQLException {
        Statement statement = connection.createStatement();
        statement.execute(sql);
        return statement;
    }
    public PreparedStatement prepare(final String sql) throws SQLException {
        return connection.prepareStatement(sql);
    }
    
    private boolean defaultAutoCommitState = true;
    private boolean inTransaction = false;
    public boolean startTransaction() throws SQLException {
        if (inTransaction)
            throw new SQLException("Already in Transaction");
        defaultAutoCommitState = connection.getAutoCommit();
        connection.setAutoCommit(false);
        inTransaction = true;
        return true;
    }
    public boolean endTransaction() throws SQLException {
        connection.commit();
        connection.setAutoCommit(defaultAutoCommitState);
        inTransaction = false;
        return true;
    }
    public boolean revertTransaction() throws SQLException {
        connection.rollback();
        connection.setAutoCommit(defaultAutoCommitState);
        inTransaction = false;
        return true;
    }
    public boolean isInTransaction() {
        return inTransaction;
    }
}
