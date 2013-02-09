package de.jaschastarke.bukkit.lib.permissions;

import net.milkbowl.vault.permission.Permission;

import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;

import de.jaschastarke.minecraft.lib.permissions.IAbstractPermission;

public class VaultPermissionManager extends PermissionManager {
    //private Plugin plugin;
    private Permission handler;
    public VaultPermissionManager(final Plugin plugin) {
        //this.plugin = plugin;
        RegisteredServiceProvider<Permission> permissionProvider = plugin.getServer().getServicesManager().getRegistration(Permission.class);
        if (permissionProvider == null) {
            throw new IllegalAccessError("Missing RegisteredServiceProvider");
        }
        handler = permissionProvider.getProvider();
    }
    
    @Override
    public boolean hasPermission(final CommandSender player, final IAbstractPermission perm) {
        return handler.has(player, perm.getFullString());
    }

}
