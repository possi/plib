package de.jaschastarke.minecraft.lib.permissions;

public interface IPermissionContainer extends IAbstractPermission {
    public IPermission[] getPermissions();
    public String getFullString();
}
