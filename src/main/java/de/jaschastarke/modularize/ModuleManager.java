package de.jaschastarke.modularize;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ModuleManager implements Iterable<ModuleEntry<IModule>> {
    private Map<Class<?>, ModuleEntry<IModule>> modules = new HashMap<Class<?>, ModuleEntry<IModule>>();

    public <T extends IModule> ModuleEntry<T> addModule(final ModuleEntry<T> entry, final boolean activate) {
        addModule(entry);
        if (activate)
            entry.activate();
        return entry;
    }
    public <T extends IModule> ModuleEntry<T> addModule(final T module, final boolean activate) {
        ModuleEntry<T> entry = addModule(module);
        if (activate)
            entry.activate();
        return entry;
    }
    public <T extends IModule> ModuleEntry<T> addModule(final T module) {
        ModuleEntry<T> entry = new ModuleEntry<T>(module, this);
        return addModule(entry);
    }
    @SuppressWarnings("unchecked")
    public <T extends IModule> ModuleEntry<T> addModule(final ModuleEntry<T> entry) {
        Class<?> cls = entry.getType();
        if (modules.containsKey(cls))
            throw new IllegalArgumentException("A module of class " + cls.getName() + " is already registered");
        modules.put(cls, (ModuleEntry<IModule>) entry);
        entry.initialize();
        return entry;
    }
    public <T extends IModule> ModuleEntry<T> addSharedModule(final T module) {
        ModuleEntry<T> entry = new SharedModuleEntry<T>(module, this);
        return addModule(entry);
    }
    public <T extends IModule> T linkSharedModule(final Class<T> module, final ModuleManager parentModuleManager) {
        LinkedModuleEntry<T> entry = new LinkedModuleEntry<T>(module, parentModuleManager);
        addModule(entry);
        return entry.getModule().getLinkedModule();
    }
    
    /**
     * Retrieves the object by its exact class from the module manager
     */
    public <T extends IModule> T getModule(final Class<T> moduleClass) {
        return moduleClass.cast(modules.get(moduleClass).getModule());
    }
    /**
     * Retrieves the first object thats a sub-type of the given class from the module manager
     */
    public <T extends IModule> T getModuleType(final Class<T> moduleClass) {
        for (ModuleEntry<IModule> entry : modules.values()) {
            if (moduleClass.isInstance(entry.getModule())) {
                return moduleClass.cast(entry.getModule());
            }
        }
        return null;
    }
    @SuppressWarnings("unchecked")
    public <T extends IModule> ModuleEntry<T> getModuleEntry(final Class<T> moduleClass) {
        return (ModuleEntry<T>) modules.get(moduleClass);
    }
    @SuppressWarnings("unchecked")
    public <T extends IModule> ModuleEntry<T> getModuleTypeEntry(final Class<T> moduleClass) {
        for (ModuleEntry<IModule> entry : modules.values()) {
            if (moduleClass.isInstance(entry.getModule())) {
                return (ModuleEntry<T>) entry;
            }
        }
        return null;
    }
    /*public void notifyUpdate() {
        for (ModuleEntry<IModule> entry : modules.values()) {
            if (entry instanceof SharedModuleEntry<?>)
                ((SharedModuleEntry<?>) entry).updateState();
        }
    }*/
    
    /**
     * Activates all modules that like to get activated.
     * 
     * Use this instead of enableAll, to enable all Modules for use, that doesn't changed initialState to Disabled.
     */
    public void activateAll() {
        for (ModuleEntry<IModule> entry : modules.values()) {
            entry.activate();
        }
    }
    
    @Deprecated
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
    
    @Override
    public Iterator<ModuleEntry<IModule>> iterator() {
        return modules.values().iterator();
    }
    public int count() {
        return modules.size();
    }
}
