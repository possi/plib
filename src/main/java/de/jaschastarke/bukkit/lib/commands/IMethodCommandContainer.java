package de.jaschastarke.bukkit.lib.commands;

import java.util.List;

import de.jaschastarke.bukkit.lib.commands.parser.TabCompletion;
import de.jaschastarke.minecraft.lib.permissions.IPermission;

public interface IMethodCommandContainer {
    public IPermission getPermission(String subPerm);
    public List<TabCompletion> getTabCompleter(MethodCommand cmd);
}
