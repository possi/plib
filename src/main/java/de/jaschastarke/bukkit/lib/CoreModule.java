package de.jaschastarke.bukkit.lib;

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
public abstract class CoreModule <E extends Core> extends SimpleModule<E> implements IHasModules {
    public CoreModule(final E plugin) {
        super(plugin);
    }
    
    @Override
    public void onEnable() {
        modules.activateAll();
        super.onEnable();
    }
    @Override
    public void onDisable() {
        modules.disableAll();
        super.onDisable();
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
