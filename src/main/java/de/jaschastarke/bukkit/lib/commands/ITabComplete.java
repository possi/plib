package de.jaschastarke.bukkit.lib.commands;

import java.util.List;

public interface ITabComplete {
    public List<String> tabComplete(CommandContext context, String[] args);
}
