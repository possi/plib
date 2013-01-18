package de.jaschastarke.bukkit.lib;

import org.bukkit.plugin.java.JavaPlugin;

import de.jaschastarke.minecraft.lib.PluginCore;
import de.jaschastarke.modularize.IHasModules;
import de.jaschastarke.modularize.IModule;
import de.jaschastarke.modularize.ModuleEntry;
import de.jaschastarke.modularize.ModuleManager;

public class Core extends JavaPlugin implements PluginCore, IHasModules {
    public boolean debug = false;
    protected boolean initialized = false;
    protected EventHandlerList listeners = new EventHandlerList(this);
    private PluginLogger log;
    
    public boolean isDebug() {
        return debug;
    }
    
    public void OnInitialize() {
        log = new PluginLogger(this);
    }
    
    @Override
    public void onEnable() {
        super.onEnable();
        if (!initialized) {
            this.OnInitialize();
        }
        
        modules.activateAll();
        listeners.registerAllEvents();
    }
    @Override
    public void onDisable() {
        modules.disableAll();
        listeners.unregisterAllEvents();
    }
    
    public PluginLogger getLog() {
        return log;
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
