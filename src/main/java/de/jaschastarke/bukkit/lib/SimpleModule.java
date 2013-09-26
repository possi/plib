package de.jaschastarke.bukkit.lib;

import org.bukkit.event.Listener;

import de.jaschastarke.modularize.AbstractModule;
import de.jaschastarke.utils.IDebugLogHolder;

/**
 * 
 * 
 * When the Module is instanceof Bukkit Event Listener-Interface, it is automatically registered.
 *
 */
public abstract class SimpleModule <E extends Core> extends AbstractModule implements IDebugLogHolder {
    protected E plugin;
    public boolean debug = false;
    protected EventHandlerList listeners;
    private ModuleLogger log = null;
    protected boolean enabled = false;
    
    public SimpleModule(final E plugin) {
        this.plugin = plugin;
        listeners = new EventHandlerList(plugin);
        if (this instanceof Listener)
            listeners.addListener((Listener) this);
    }
    public E getPlugin() {
        return plugin;
    }
    public boolean isDebug() {
        return debug || plugin.isDebug();
    }
    public ModuleLogger getLog() {
        if (log == null)
            log = new ModuleLogger(plugin, this);
        return log;
    }
    
    @Override
    public void onEnable() {
        enabled = true;
        listeners.registerAllEvents();
    }
    @Override
    public void onDisable() {
        enabled = false;
        listeners.unregisterAllEvents();
    }
    public String getName() {
        return this.getClass().getSimpleName();
    }
}
