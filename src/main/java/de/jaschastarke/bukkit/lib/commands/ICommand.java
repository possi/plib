package de.jaschastarke.bukkit.lib.commands;

public interface ICommand {
    public String getName();
    public String[] getAliases();
    public boolean execute(CommandContext context, String[] args) throws MissingPermissionCommandException, CommandException;
}
