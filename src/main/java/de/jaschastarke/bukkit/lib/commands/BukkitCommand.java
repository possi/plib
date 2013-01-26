package de.jaschastarke.bukkit.lib.commands;

import org.bukkit.command.Command;

public abstract class BukkitCommand extends AbstractCommand {
    Command bukkitcommand;

    public void setBukkitCommand(final Command bukkitcmd) {
        bukkitcommand = bukkitcmd;
    }
}
