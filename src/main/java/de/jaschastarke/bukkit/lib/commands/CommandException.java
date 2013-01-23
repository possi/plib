package de.jaschastarke.bukkit.lib.commands;

public class CommandException extends Exception {
    private static final long serialVersionUID = -1263951188912835089L;
    private Object[] arguments = null;
    
    public CommandException(String msgKey) {
        super(msgKey);
    }
    public CommandException(String msgKey, Object... args) {
        super(msgKey);
        arguments = args;
    }
    public Object[] getAdditionalArguments() {
        return arguments;
    }
}
