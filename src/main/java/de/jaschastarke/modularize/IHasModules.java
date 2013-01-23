package de.jaschastarke.modularize;

public interface IHasModules {
    public <T extends IModule> ModuleEntry<T> addModule(T module);
    public <T extends IModule> T getModule(Class<T> module);
}
