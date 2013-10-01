package de.jaschastarke.modularize;

public class LinkedModuleEntry<E extends IModule> extends ModuleEntry<SharedModuleLink<E>> {
    public LinkedModuleEntry(final SharedModuleLink<E> module, final ModuleManager manager) {
        super(module, manager);
    }
    public LinkedModuleEntry(final Class<E> cls, final ModuleManager manager) {
        super(new SharedModuleLink<E>(cls, manager), manager);
    }
    public Class<?> getType() {
        return getModule().getLinkedType();
    }
}
