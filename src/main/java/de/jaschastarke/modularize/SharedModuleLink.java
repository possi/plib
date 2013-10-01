package de.jaschastarke.modularize;

import java.util.concurrent.Callable;

public class SharedModuleLink<T extends IModule> implements IModule {
    //private ModuleEntry<IModule> myEntry;
    private ModuleManager manager;
    private Class<T> target;
    private Callable<T> cb = null;
    
    public SharedModuleLink(final Class<T> moduleClass, final ModuleManager parentManager) {
        target = moduleClass;
        manager = parentManager;
    }
    public SharedModuleLink(final Class<T> moduleClass, final ModuleManager parentManager, final Callable<T> moduleConstruct) {
        this(moduleClass, parentManager);
        cb = moduleConstruct;
    }
    
    public SharedModuleEntry<T> getLinkedEntry() {
        ModuleEntry<T> entry = manager.getModuleEntry(target);
        if (entry != null) {
            if (!(entry instanceof SharedModuleEntry<?>)) {
                throw new InvalidStateException("SharedModuleLink has to link to a SharedModuleEntry");
            }
            cb = null;
            return (SharedModuleEntry<T>) entry;
        } else if (cb != null) {
            try {
                T module = cb.call();
                SharedModuleEntry<T> newEntry = new SharedModuleEntry<T>(module, manager);
                manager.addModule(newEntry);
                cb = null;
                return newEntry;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else {
            throw new InvalidStateException("Module of class " + target.getName() + " not found");
        }
    }

    public Class<?> getLinkedType() {
        return target;
    }
    public T getLinkedModule() {
        return getLinkedEntry().getModule();
    }

    @Override
    public void initialize(final ModuleEntry<IModule> entry) {
        getLinkedEntry().addDepending(entry);
    }

    @Override
    public void onEnable() {
        getLinkedEntry().updateState(true);
    }

    @Override
    public void onDisable() {
        getLinkedEntry().updateState(false);
    }
}
