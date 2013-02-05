package de.jaschastarke.modularize;

import java.text.MessageFormat;

public class ModuleEntry <T extends IModule> {
    public static enum ModuleState {
        ENABLED,
        DISABLED,
        INITIALIZED,
        NOT_INITIALIZED
    }
    protected ModuleState state = ModuleState.NOT_INITIALIZED;
    public ModuleState initialState = ModuleState.ENABLED;
    
    protected ModuleManager manager = null;
    protected T module;
    
    public ModuleEntry(final T module) {
        this.module = module;
    }
    public ModuleEntry(final T module, final ModuleManager manager) {
        this.module = module;
        this.manager = manager;
    }
    public T getModule() {
        return module;
    }
    public ModuleManager getManager() {
        return manager;
    }
    
    @SuppressWarnings("unchecked")
    public boolean initialize() {
        if (state == ModuleState.NOT_INITIALIZED) {
            module.initialize((ModuleEntry<IModule>) this);
            state = ModuleState.INITIALIZED;
            return true;
        }
        return false;
    }
    public boolean activate() {
        if (state == ModuleState.NOT_INITIALIZED) {
            this.initialize();
        } else if (state != ModuleState.INITIALIZED) {
            throw new InvalidStateException(MessageFormat.format("Module {0} has already been initialized.", module.getClass().getName()));
        }
        if (initialState == ModuleState.ENABLED) {
            return this.enable();
        } else {
            return false;
        }
    }
    public ModuleState getState() {
        return state;
    }
    
    public boolean enable() {
        if (state == ModuleState.DISABLED || state == ModuleState.INITIALIZED) {
            module.onEnable();
            this.state = ModuleState.ENABLED;
            return true;
        } else if (state == ModuleState.NOT_INITIALIZED) {
            throw new InvalidStateException(MessageFormat.format("Module {0} not yet initialized.", module.getClass().getName()));
        }
        return false;
    }
    public boolean disable() {
        if (state == ModuleState.ENABLED) {
            module.onDisable();
            this.state = ModuleState.DISABLED;
            return true;
        }
        return false;
    }
}
