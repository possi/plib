package de.jaschastarke.bukkit.lib.commands;

public class CommandException extends Exception {
    private static final long serialVersionUID = -1263951188912835089L;
    private Object[] arguments = null;
    
    public CommandException(final String msgKey) {
        super(msgKey);
    }
    public CommandException(final String msgKey, final Object... args) {
        super(msgKey);
        arguments = args;
    }
    public Object[] getAdditionalArguments() {
        return arguments;
    }
}
