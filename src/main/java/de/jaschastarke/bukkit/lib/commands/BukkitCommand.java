package de.jaschastarke.bukkit.lib.commands;

import org.bukkit.command.Command;

abstract public class BukkitCommand extends AbstractCommand {
    Command bukkitcommand;

    public void setBukkitCommand(Command bukkitcmd) {
        bukkitcommand = bukkitcmd;
    }
}
