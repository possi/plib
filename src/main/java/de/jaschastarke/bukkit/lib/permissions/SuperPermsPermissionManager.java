package de.jaschastarke.bukkit.lib.permissions;

import org.bukkit.command.CommandSender;

import de.jaschastarke.minecraft.lib.permissions.IAbstractPermission;
import de.jaschastarke.minecraft.lib.permissions.IPermissionChild;

public class SuperPermsPermissionManager extends PermissionManager {
    public boolean hasPermission(final CommandSender player, final IAbstractPermission perm) {
        if (player.hasPermission(perm.getFullString()))
            return true;
        if (perm instanceof IPermissionChild)
            if (hasSomePermission(player, ((IPermissionChild) perm).getParentPermissions()))
                return true;
        return false;
    }
}
