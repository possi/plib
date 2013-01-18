package de.jaschastarke.modularize;

import java.util.HashMap;
import java.util.Map;

public class ModuleManager {
    private Map<Class<?>, ModuleEntry<IModule>> modules = new HashMap<Class<?>, ModuleEntry<IModule>>();

    public <T extends IModule> ModuleEntry<T> addModule(ModuleEntry<T> entry, boolean activate) {
        addModule(entry);
        if (activate)
            entry.activate();
        return entry;
    }
    public <T extends IModule> ModuleEntry<T> addModule(T module, boolean activate) {
        ModuleEntry<T> entry = addModule(module);
        if (activate)
            entry.activate();
        return entry;
    }
    public <T extends IModule> ModuleEntry<T> addModule(T module) {
        ModuleEntry<T> entry = new ModuleEntry<T>(module, this);
        entry.initialize();
        return addModule(entry);
    }
    @SuppressWarnings("unchecked")
    public <T extends IModule> ModuleEntry<T> addModule(ModuleEntry<T> entry) {
        modules.put(entry.getModule().getClass(), (ModuleEntry<IModule>) entry);
        return entry;
    }
    
    public <T extends IModule> T getModule(Class<T> moduleClass) {
        return moduleClass.cast(modules.get(moduleClass).getModule());
    }
    public <T extends IModule> T getModuleType(Class<T> moduleClass) {
        for (ModuleEntry<IModule> entry : modules.values()) {
            if (moduleClass.isInstance(entry.getModule())) {
                return moduleClass.cast(entry.getModule());
            }
        }
        return null;
    }
    @SuppressWarnings("unchecked")
    public <T extends IModule> ModuleEntry<T> getModuleEntry(Class<T> moduleClass) {
        return (ModuleEntry<T>) modules.get(moduleClass);
    }
    @SuppressWarnings("unchecked")
    public <T extends IModule> ModuleEntry<T> getModuleTypeEntry(Class<T> moduleClass) {
        for (ModuleEntry<IModule> entry : modules.values()) {
            if (moduleClass.isInstance(entry.getModule())) {
                return (ModuleEntry<T>) entry;
            }
        }
        return null;
    }
    
    /**
     * Activates all modules that like to get activated.
     * 
     * Use this instead of enableAll, to enable all Modules for use, that doesn't change initialState to Disabled.
     */
    public void activateAll() {
        for (ModuleEntry<IModule> entry : modules.values()) {
            entry.activate();
        }
    }
    
    public void enableAll() {
        for (ModuleEntry<IModule> entry : modules.values()) {
            entry.enable();
        }
    }
    
    public void disableAll() {
        for (ModuleEntry<IModule> entry : modules.values()) {
            entry.disable();
        }
    }
}
