package de.jaschastarke.bukkit.lib.permissions;

import net.milkbowl.vault.permission.Permission;

import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;

import de.jaschastarke.bukkit.lib.Core;
import de.jaschastarke.minecraft.lib.permissions.IAbstractPermission;
import de.jaschastarke.minecraft.lib.permissions.IPermissionChild;

/**
 * @deprecated THIS ISN'T A REPLACEMENT FOR SuperPerms! It only checks permissions setted in the perm-system
 */
@Deprecated
public final class VaultPermissionManager extends PermissionManager {
    private Plugin plugin;
    private Permission handler;
    public VaultPermissionManager(final Plugin plugin) throws IllegalAccessException {
        this.plugin = plugin;
        RegisteredServiceProvider<Permission> permissionProvider = plugin.getServer().getServicesManager().getRegistration(Permission.class);
        if (permissionProvider == null) {
            throw new IllegalAccessError("Missing RegisteredServiceProvider");
        }
        handler = permissionProvider.getProvider();
        throw new IllegalAccessException();
    }
    
    @Override
    public boolean hasPermission(final CommandSender player, final IAbstractPermission perm) {
        boolean has = handler.has(player, perm.getFullString());
        if (plugin instanceof Core && ((Core) plugin).isDebug())
            ((Core) plugin).getLog().debug("Vault-Permission-Check: " + player.getName() + " - " + perm.getFullString() + ": " + has);
        if (has)
            return true;
        if (perm instanceof IPermissionChild)
            if (hasSomePermission(player, ((IPermissionChild) perm).getParentPermissions()))
                return true;
        return false;
    }
}
