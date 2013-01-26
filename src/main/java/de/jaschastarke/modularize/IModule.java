package de.jaschastarke.modularize;

public interface IModule {
    public void initialize(ModuleEntry<IModule> entry);
    public void onEnable();
    public void onDisable();
}
