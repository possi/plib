package de.jaschastarke.modularize;

public abstract class AbstractModule implements IModule {
    protected ModuleEntry<IModule> entry;
    @Override
    public void Initialize(ModuleEntry<IModule> entry) {
        if (entry.getModule() != this) {
            throw new RuntimeException("Wrong Module-Entry given to initialize "+this.getClass().getName());
        }
        this.entry = entry;
    }
    
}
