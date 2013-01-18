package de.jaschastarke.bukkit.lib;

import java.util.logging.Level;
import java.util.logging.Logger;

import de.jaschastarke.utils.ISimpleLogger;

public class PluginLogger implements ISimpleLogger {
    protected Logger log;
    protected Core plugin;
    
    public PluginLogger(Core plugin) {
        this.plugin = plugin;
        log = plugin.getLogger();
    }
    protected String extendMessage(String msg) {
        return msg;
    }
    
    public void info(String msg) {
        log.log(Level.INFO, extendMessage(msg));
    }
    public void debug(String msg) {
        if (plugin.isDebug()) {
            log.log(Level.FINEST, "[DEBUG] " + extendMessage(msg));
        }
    }
    public void severe(String msg) {
        log.log(Level.SEVERE, "[SEVERE] " + extendMessage(msg));
    }
    public void warn(String msg) {
        log.log(Level.WARNING, "[WARNING] " + extendMessage(msg));
    }
}
