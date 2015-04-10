package de.jaschastarke.bukkit.lib.database;

import java.io.File;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.Properties;

import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import de.jaschastarke.database.DatabaseConfigurationException;
import de.jaschastarke.database.Type;
import de.jaschastarke.database.db.Database;

public final class DBHelper {
    private static final String MYSQL_PROP_AUTORECONNECT = "autoReconnect";
    private static final String PROP_TRUE = "true";
    private static final String MYSQL_USER_FIELD = "user";
    private static final String MYSQL_PASSWORD_FIELD = "password";
    private static final String BUKKIT_USER_FIELD = "username";
    private static final String BUKKIT_PASSWORD_FIELD = MYSQL_PASSWORD_FIELD; // I'm not sure with this, but i won't to pleasure checkstyle
    private DBHelper() {
    }
    
    private static YamlConfiguration configuration = null;
    public static YamlConfiguration getBukkitConfig() {
        if (configuration == null) {
            Server server = Bukkit.getServer();
            configuration = YamlConfiguration.loadConfiguration(new File("bukkit.yml"));
            configuration.options().copyDefaults(true);
            InputStreamReader inputStreamReader = new InputStreamReader(server.getClass().getClassLoader().getResourceAsStream("configurations/bukkit.yml"));
            configuration.setDefaults(YamlConfiguration.loadConfiguration(inputStreamReader));
        }
        return configuration;
    }
    
    private static final String DATABASE_URL_NODE = "url";
    public static Database connect(final Plugin plugin) throws DatabaseConfigurationException {
        ConfigurationSection dbc = getBukkitConfig().getConfigurationSection("database");
        if (!dbc.contains(DATABASE_URL_NODE))
            throw new DatabaseConfigurationException("No Database-URL configured in bukkit.yml");
        String url = dbc.getString(DATABASE_URL_NODE);
        String driver = dbc.getString("driver");
        Type type = Type.getType(url);
        
        Properties prop = new Properties();
        
        Database db;
        switch (type) {
            case MySQL:
                prop.put(MYSQL_PROP_AUTORECONNECT, PROP_TRUE);
                prop.put(MYSQL_USER_FIELD, dbc.getString(BUKKIT_USER_FIELD));
                prop.put(MYSQL_PASSWORD_FIELD, dbc.getString(BUKKIT_PASSWORD_FIELD));
                db = new de.jaschastarke.database.mysql.Database(driver);
                db.connect(url, prop);
                break;
            case SQLite:
                url = url.replace("{DIR}", plugin.getDataFolder().getPath() + File.separatorChar);
                url = url.replace("{NAME}", plugin.getName());
                db = new de.jaschastarke.database.sqlite.Database(driver);
                db.connect(url, prop);
                break;
            default:
                throw new DatabaseConfigurationException("Database-Type for Connection \"" + url + "\" not supported.");
        }
        try {
            if (db.getConnection().getMetaData().supportsTransactions())
                db.getConnection().setAutoCommit(true);
        } catch (SQLException e) {
            plugin.getLogger().info("Failed to enable autocommit: " + e.getMessage());
        }
        return db;
    }
    
    public static Database createConnection(final String dsn) throws DatabaseConfigurationException {
        return createConnection(dsn, null, null);
    }
    public static Database createConnection(final String dsn, final String username, final String password) throws DatabaseConfigurationException {
        Type type = Type.getType(dsn);
        
        Properties prop = new Properties();
        
        Database db;
        switch (type) {
            case MySQL:
                prop.put(MYSQL_PROP_AUTORECONNECT, PROP_TRUE);
                if (username != null)
                    prop.put(MYSQL_USER_FIELD, username);
                if (password != null)
                    prop.put(MYSQL_PASSWORD_FIELD, password);
                db = new de.jaschastarke.database.mysql.Database();
                db.connect(dsn, prop);
                break;
            case SQLite:
                db = new de.jaschastarke.database.sqlite.Database();
                db.connect(dsn, prop);
                break;
            default:
                throw new DatabaseConfigurationException("Database-Type for Connection \"" + dsn + "\" not supported.");
        }
        try {
            if (db.getConnection().getMetaData().supportsTransactions())
                db.getConnection().setAutoCommit(true);
        } catch (SQLException e) {
            throw new DatabaseConfigurationException("Failed to enable autocommit: " + e.getMessage());
        }
        return db;
    }
}
