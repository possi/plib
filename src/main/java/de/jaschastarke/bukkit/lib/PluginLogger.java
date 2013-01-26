package de.jaschastarke.bukkit.lib;

import java.util.logging.Level;
import java.util.logging.Logger;

import de.jaschastarke.utils.ISimpleLogger;

public class PluginLogger implements ISimpleLogger {
    protected Logger log;
    protected Core plugin;
    
    public PluginLogger(final Core plugin) {
        this.plugin = plugin;
        log = plugin.getLogger();
    }
    protected String extendMessage(final String msg) {
        return msg;
    }
    
    public void info(final String msg) {
        log.log(Level.INFO, extendMessage(msg));
    }
    public void debug(final String msg) {
        if (plugin.isDebug()) {
            log.log(Level.INFO, "[DEBUG] " + extendMessage(msg));
        }
    }
    public void severe(final String msg) {
        log.log(Level.SEVERE, extendMessage(msg));
    }
    public void warn(final String msg) {
        log.log(Level.WARNING, extendMessage(msg));
    }
}
