package de.jaschastarke.minecraft.lib.permissions;

public interface IPermissionChild {
    public void addParentPermission(IPermission parent);
    public IPermission[] getParentPermissions();
}
