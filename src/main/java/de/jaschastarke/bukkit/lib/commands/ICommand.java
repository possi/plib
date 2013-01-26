package de.jaschastarke.bukkit.lib.commands;

public interface ICommand {
    public static final String PREFIX = "/";
    
    public String getName();
    public String[] getAliases();
    public boolean execute(CommandContext context, String[] args) throws MissingPermissionCommandException, CommandException;
}
