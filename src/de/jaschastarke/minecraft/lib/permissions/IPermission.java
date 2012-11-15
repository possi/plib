package de.jaschastarke.minecraft.lib.permissions;

import org.bukkit.permissions.PermissionDefault;

public interface IPermission extends IAbstractPermission {
    public PermissionDefault getDefault();
}
