package de.jaschastarke.minecraft.lib.permissions;

abstract public interface IAbstractPermission {
    public final static String SEP = ".";
    
    public IAbstractPermission getParent();
    public String getFullString();
}
