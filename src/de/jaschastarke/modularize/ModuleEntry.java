package de.jaschastarke.modularize;

public class ModuleEntry <T extends IModule> {
    enum ModuleState {
        ENABLED,
        DISABLED,
        INITIALIZED,
        NOT_INITIALIZED
    }
    protected ModuleState state = ModuleState.NOT_INITIALIZED;
    public ModuleState initialState = ModuleState.ENABLED;
    
    protected ModuleManager manager = null;
    protected T module;
    
    public ModuleEntry(T module) {
        this.module = module;
    }
    public ModuleEntry(T module, ModuleManager manager) {
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
            module.Initialize((ModuleEntry<IModule>) this);
            state = ModuleState.INITIALIZED;
            return true;
        }
        return false;
    }
    public boolean activate() {
        if (state == ModuleState.NOT_INITIALIZED) {
            this.initialize();
        } else if (state != ModuleState.INITIALIZED) {
            throw new InvalidStateException("Module "+module.getClass().getName()+" has already been initialized.");
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
            module.OnEnable();
            this.state = ModuleState.ENABLED;
            return true;
        } else if (state == ModuleState.NOT_INITIALIZED) {
            throw new InvalidStateException("Module "+module.getClass().getName()+" not yet initialized.");
        }
        return false;
    }
    public boolean disable() {
        if (state == ModuleState.ENABLED) {
            module.OnDisable();
            this.state = ModuleState.DISABLED;
            return true;
        } else if (state == ModuleState.NOT_INITIALIZED) {
            throw new InvalidStateException("Module "+module.getClass().getName()+" not yet initialized.");
        }
        return false;
    }
}
