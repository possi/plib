package de.jaschastarke.modularize;

public interface IModule {
    public void Initialize(ModuleEntry<IModule> entry);
    public void OnEnable();
    public void OnDisable();
}
