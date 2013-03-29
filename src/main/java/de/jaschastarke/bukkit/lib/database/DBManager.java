package de.jaschastarke.bukkit.lib.database;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.PersistenceException;

import org.bukkit.plugin.Plugin;

import com.avaje.ebean.EbeanServer;

import de.jaschastarke.bukkit.lib.Core;

public class DBManager {
    private Core plugin;
    
    private List<Class<?>> classes = new ArrayList<Class<?>>();
    
    public static boolean isDatabaseEnabled(final Plugin plugin) {
        return plugin.getDescription().isDatabaseEnabled();
    }
    
    public DBManager(final Core plugin) {
        this.plugin = plugin;
    }
    public void registerDatabaseClass(final Class<?> cls) {
        classes.add(cls);
    }
    public List<Class<?>> getDatabaseClasses() {
        return classes;
    }
    public boolean usesDatabase() {
        return classes.size() > 0;
    }
    public EbeanServer getDatabase() {
        return plugin.getDatabase();
    }
    public boolean checkAllTablesPresent() {
        for (Class<?> cls : classes) {
            try {
                getDatabase().find(cls).findRowCount();
            } catch (PersistenceException ex) {
                return false;
            }
        }
        return true;
    }
}
