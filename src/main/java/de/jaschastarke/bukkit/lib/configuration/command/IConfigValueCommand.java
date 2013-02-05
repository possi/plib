package de.jaschastarke.bukkit.lib.configuration.command;

import de.jaschastarke.bukkit.lib.commands.CommandContext;

public interface IConfigValueCommand {
    public boolean process(CommandContext context, String[] args, String[] chain);
}
