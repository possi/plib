package de.jaschastarke.bukkit.lib.permissions;

import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

import de.jaschastarke.bukkit.lib.Core;
import de.jaschastarke.minecraft.lib.permissions.IAbstractPermission;
import de.jaschastarke.minecraft.lib.permissions.IPermissionChild;

public class SuperPermsPermissionManager extends PermissionManager {
    private Plugin plugin;
    public SuperPermsPermissionManager(final Plugin plugin) {
        this.plugin = plugin;
    }
    
    public boolean hasPermission(final CommandSender player, final IAbstractPermission perm) {
        boolean has = player.hasPermission(perm.getFullString());
        if (plugin instanceof Core && ((Core) plugin).isDebug())
            ((Core) plugin).getLog().debug("Permission-Check: " + player.getName() + " - " + perm.getFullString() + ": " + has);
        if (has)
            return true;
        if (perm instanceof IPermissionChild)
            if (hasSomePermission(player, ((IPermissionChild) perm).getParentPermissions()))
                return true;
        return false;
    }
}
