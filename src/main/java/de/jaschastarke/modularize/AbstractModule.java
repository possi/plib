package de.jaschastarke.modularize;

public abstract class AbstractModule implements IModule {
    protected ModuleEntry<IModule> entry;
    @Override
    public void initialize(final ModuleEntry<IModule> pEntry) {
        if (pEntry.getModule() != this) {
            throw new IllegalArgumentException("Wrong Module-Entry given to initialize " + this.getClass().getName());
        }
        this.entry = pEntry;
    }
    public ModuleEntry<IModule> getModuleEntry() {
        return entry;
    }
    
}
