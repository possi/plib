package de.jaschastarke.bukkit.lib.permissions;

import java.util.Arrays;
import java.util.Collection;

import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

import de.jaschastarke.minecraft.lib.permissions.IAbstractPermission;
import de.jaschastarke.minecraft.lib.permissions.IDynamicPermission;

public abstract class PermissionManager {
    public abstract boolean hasPermission(CommandSender player, IAbstractPermission perm);
    public boolean hasSomePermission(final CommandSender player, final IAbstractPermission[] perms) {
        return hasSomePermission(player, Arrays.asList(perms));
    }
    public boolean hasSomePermission(final CommandSender player, final Collection<IAbstractPermission> perms) {
        for (IAbstractPermission perm : perms) {
            if (hasPermission(player, perm))
                return true;
        }
        return false;
    }
    public boolean hasPermission(final CommandSender player, final IDynamicPermission perm) {
        return hasSomePermission(player, perm.getPermissions());
    }
    
    public static PermissionManager getDefaultPermissionManager(final Plugin plugin) {
        return new SuperPermsPermissionManager(plugin);
    }
}
