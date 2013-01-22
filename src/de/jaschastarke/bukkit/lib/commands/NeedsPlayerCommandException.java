package de.jaschastarke.bukkit.lib.commands;

public class NeedsPlayerCommandException extends CommandException {
    private static final long serialVersionUID = 46182438162452913L;

    public NeedsPlayerCommandException() {
        super("This command can not be executed from console");
    }
}
