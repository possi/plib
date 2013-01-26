package de.jaschastarke.minecraft.lib.permissions;

public abstract interface IAbstractPermission {
    public static final String SEP = ".";
    
    public String toString();
    public IAbstractPermission getParent();
    public String getFullString();
}
