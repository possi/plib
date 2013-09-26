package de.jaschastarke.bukkit.lib.commands;

import org.bukkit.command.Command;

import de.jaschastarke.bukkit.lib.Core;

public abstract class BukkitCommand extends AbstractCommand {
    Command bukkitcommand;

    public BukkitCommand() {
    }
    public BukkitCommand(final Core plugin) {
        super();
        for (ICommand cmd : handler.commands) {
            if (cmd instanceof MethodCommand)
                ((MethodCommand) cmd).setDescription(plugin.getDocCommentStorage());
        }
    }

    public void setBukkitCommand(final Command bukkitcmd) {
        bukkitcommand = bukkitcmd;
    }
}
