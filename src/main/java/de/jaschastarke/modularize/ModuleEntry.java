package de.jaschastarke.modularize;

import java.text.MessageFormat;

public class ModuleEntry <T extends IModule> {
    public static enum ModuleState {
        ENABLED,
        DISABLED,
        INITIALIZED,
        NOT_INITIALIZED
    }
    private ModuleState state = ModuleState.NOT_INITIALIZED;
    private ModuleState initialState = ModuleState.ENABLED;
    
    private ModuleManager manager = null;
    private T module;
    
    public ModuleEntry(final T module) {
        this.module = module;
    }
    public ModuleEntry(final T module, final ModuleManager manager) {
        this.module = module;
        this.manager = manager;
    }
    public Class<?> getType() {
        return module.getClass();
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
        if (isDeactivated())
            return false;
        this.initialize();
        if (initialState == ModuleState.ENABLED) {
            return this.enable();
        } else {
            state = initialState;
            return false;
        }
    }
    public ModuleState getState() {
        return state;
    }
    
    public boolean enable() {
        if (isDeactivated())
            return false;
        if (state == ModuleState.DISABLED || state == ModuleState.INITIALIZED) {
            try {
                this.state = ModuleState.ENABLED;
                module.onEnable();
            } catch (RuntimeException ex) {
                this.state = ModuleState.DISABLED;
                throw ex;
            }
            return true;
        } else if (state == ModuleState.NOT_INITIALIZED) {
            throw new InvalidStateException(MessageFormat.format("Module {0} not yet initialized.", module.getClass().getName()));
        }
        return false;
    }
    public boolean disable() {
        if (state == ModuleState.ENABLED) {
            try {
                this.state = ModuleState.DISABLED;
                module.onDisable();
            } catch (RuntimeException ex) {
                this.state = ModuleState.ENABLED;
                throw ex;
            }
            return true;
        }
        return false;
    }
    public boolean isEnabled() {
        return state == ModuleState.ENABLED;
    }
    
    public void setDefaultEnabled(final boolean newState) {
        if (isDeactivated())
            return;
        initialState = newState ? ModuleState.ENABLED : ModuleState.DISABLED;
    }
    /**
     * Enables or disables a module, but when the module isn't initialized yet, it changes the initialState instead.
     * @return true if the actual state of the module was changed (not the initialState)
     */
    public boolean setEnabled(final boolean newState) {
        if (isDeactivated())
            return false;
        if (state == ModuleState.NOT_INITIALIZED) {
            setDefaultEnabled(newState);
            return false;
        } else if (state == ModuleState.INITIALIZED) {
            throw new InvalidStateException(MessageFormat.format("Module {0} was initialized but not enabled/disabled", module.getClass().getName()));
        } else if (newState) {
            return enable();
        } else {
            return disable();
        }
    }
    public void deactivateUsage() {
        initialState = ModuleState.NOT_INITIALIZED;
        disable();
    }
    public boolean isDeactivated() {
        return initialState == ModuleState.NOT_INITIALIZED;
    }
}
