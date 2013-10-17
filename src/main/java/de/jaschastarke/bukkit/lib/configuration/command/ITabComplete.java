package de.jaschastarke.bukkit.lib.configuration.command;

import java.util.List;

public interface ITabComplete {
    public List<String> tabComplete(String[] args, String[] chain);
}
