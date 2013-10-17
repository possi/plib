package de.jaschastarke.bukkit.lib.commands;

import java.util.List;

import de.jaschastarke.minecraft.lib.permissions.IPermission;

public interface IMethodCommandContainer {
    public IPermission getPermission(String subPerm);
    public List<TabCompletionHelper> getTabCompleter(MethodCommand cmd);
}
