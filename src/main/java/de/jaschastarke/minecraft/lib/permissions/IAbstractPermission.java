package de.jaschastarke.minecraft.lib.permissions;

abstract public interface IAbstractPermission {
    public final static String SEP = ".";
    
    public String toString();
    public IAbstractPermission getParent();
    public String getFullString();
}
