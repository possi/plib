package de.jaschastarke.bukkit.lib.permissions;

import org.bukkit.command.CommandSender;

import de.jaschastarke.minecraft.lib.permissions.IPermission;

public class PermissionManager {
    public static boolean hasPermission(CommandSender player, IPermission perm) {
        return player.hasPermission(perm.getFullString());
    }
}
