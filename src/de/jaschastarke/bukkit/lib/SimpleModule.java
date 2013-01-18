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
abstract public class SimpleModule <E extends Core> extends AbstractModule implements IHasModules {
    protected E plugin;
    public boolean debug = false;
    protected EventHandlerList listeners;
    private ModuleLogger log = null;
    protected boolean enabled = false;
    
    public SimpleModule(E plugin) {
        this.plugin = plugin;
        listeners = new EventHandlerList(plugin);
        if (this instanceof Listener)
            listeners.addListener((Listener) this);
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
    public void OnEnable() {
        enabled = true;
        modules.activateAll();
        listeners.registerAllEvents();
    }
    @Override
    public void OnDisable() {
        enabled = false;
        modules.disableAll();
        listeners.unregisterAllEvents();
    }
    public String getName() {
        return this.getClass().getName();
    }

    /* IHasModules */
    protected ModuleManager modules = new ModuleManager();
    @Override
    public <T extends IModule> ModuleEntry<T> addModule(T module) {
        return modules.addModule(module);
    }
    @Override
    public <T extends IModule> T getModule(Class<T> module) {
        return modules.getModuleType(module);
    }
}
