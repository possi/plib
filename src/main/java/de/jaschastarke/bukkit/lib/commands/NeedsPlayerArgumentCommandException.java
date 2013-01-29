package de.jaschastarke.bukkit.lib.commands;

public class NeedsPlayerArgumentCommandException extends NeedsPlayerCommandException {
    private static final long serialVersionUID = 5342892848984971439L;
    
    public NeedsPlayerArgumentCommandException(final String msgKey) {
        super(msgKey);
    }

    public NeedsPlayerArgumentCommandException() {
        super("To use the command from console a player name has to be passed");
    }
}
