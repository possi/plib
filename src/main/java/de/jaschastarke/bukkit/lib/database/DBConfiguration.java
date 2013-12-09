package de.jaschastarke.bukkit.lib.database;

import java.sql.Connection;

import de.jaschastarke.bukkit.lib.configuration.Configuration;
import de.jaschastarke.bukkit.lib.configuration.ConfigurationContainer;
import de.jaschastarke.configuration.ConfigurationStyle;
import de.jaschastarke.configuration.IConfigurationSubGroup;
import de.jaschastarke.configuration.annotations.IsConfigurationNode;
import de.jaschastarke.maven.ArchiveDocComments;
import de.jaschastarke.utils.ClassDescriptorStorage;

/**
 * Database-Connection-Configuration
 * 
 * See http://wiki.bukkit.org/Bukkit.yml#database for reference.
 */
@ArchiveDocComments
public class DBConfiguration extends Configuration implements IConfigurationSubGroup {
    private String name = "database";
    private int order = 100;
    
    public DBConfiguration(ClassDescriptorStorage docCommentStorage) {
        super(docCommentStorage);
        // TODO Auto-generated constructor stub
    }

    public DBConfiguration(ConfigurationContainer container) {
        super(container);
        // TODO Auto-generated constructor stub
    }

    public void setName(String name) {
        this.name = name;
    }
    public void setOrder(int order) {
        this.order = order;
    }

    @Override
    public String getName() {
        return name;
    }
    @Override
    public int getOrder() {
        return order;
    }
    
    
    @IsConfigurationNode(order = 10)
    public String getUsername() {
        return config.getString("username", "bukkit");
    }
    @IsConfigurationNode(order = 20, style = ConfigurationStyle.GROUPED_PREVIOUS)
    public String getIsolation() {
        return config.getString("isolation", "SERIALIZABLE");
    }
    @IsConfigurationNode(order = 30, style = ConfigurationStyle.GROUPED_PREVIOUS)
    public String getDriver() {
        return config.getString("driver", "org.sqlite.JDBC");
    }
    @IsConfigurationNode(order = 40, style = ConfigurationStyle.GROUPED_PREVIOUS)
    public String getPassword() {
        return config.getString("password", "walrus");
    }
    @IsConfigurationNode(order = 50, style = ConfigurationStyle.GROUPED_PREVIOUS)
    public String getUrl() {
        return config.getString("url", "jdbc:sqlite:{DIR}{NAME}.db");
    }

    public int getIsolationLevel() {
        String s = getIsolation();
        if (s.equalsIgnoreCase("NONE")) {
            return Connection.TRANSACTION_NONE;
        } else if (s.equalsIgnoreCase("READ_COMMITTED")) {
            return Connection.TRANSACTION_READ_COMMITTED;
        } else if (s.equalsIgnoreCase("READ_UNCOMMITTED")) {
            return Connection.TRANSACTION_READ_UNCOMMITTED;
        } else if (s.equalsIgnoreCase("REPEATABLE_READ")) {
            return Connection.TRANSACTION_REPEATABLE_READ;
        } else {
            return Connection.TRANSACTION_SERIALIZABLE;
        }
    }
}
