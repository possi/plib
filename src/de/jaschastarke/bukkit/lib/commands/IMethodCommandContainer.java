package de.jaschastarke.bukkit.lib.commands;

import de.jaschastarke.minecraft.lib.permissions.IPermission;

public interface IMethodCommandContainer {
    public IPermission getPermission(String subPerm);
}
