package de.jaschastarke.bukkit.lib;

import org.bukkit.event.Listener;

import de.jaschastarke.modularize.AbstractModule;
import de.jaschastarke.modularize.IHasModules;
import de.jaschastarke.modularize.IModule;
import de.jaschastarke.modularize.ModuleEntry;
import de.jaschastarke.modularize.ModuleManager;

/**
 * 
 * 
 * When the Module is instanceof Bukkit Event Listener-Interface, it is automatically registered.
 *
 */
public abstract class SimpleModule <E extends Core> extends AbstractModule implements IHasModules {
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
        modules.activateAll();
        listeners.registerAllEvents();
    }
    @Override
    public void onDisable() {
        enabled = false;
        modules.disableAll();
        listeners.unregisterAllEvents();
    }
    public String getName() {
        return this.getClass().getSimpleName();
    }

    /* IHasModules */
    protected ModuleManager modules = new ModuleManager();
    @Override
    public <T extends IModule> ModuleEntry<T> addModule(final T module) {
        return modules.addModule(module);
    }
    @Override
    public <T extends IModule> T getModule(final Class<T> module) {
        return modules.getModuleType(module);
    }
}
