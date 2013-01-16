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
abstract public class CoreModule extends AbstractModule implements IHasModules {
    protected Core plugin;
    public boolean debug = false;
    protected EventHandlerList listeners;
    private ModuleLogger log = null;
    
    public CoreModule(Core plugin) {
        this.plugin = plugin;
        listeners = new EventHandlerList(plugin);
        if (this instanceof Listener)
            listeners.registerEvents((Listener) this);
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
        modules.activateAll();
    }
    @Override
    public void OnDisable() {
        modules.disableAll();
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
