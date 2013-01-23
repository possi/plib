package de.jaschastarke.bukkit.lib.commands;

public class IllegalCommandMethodException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public IllegalCommandMethodException(Throwable parent) {
        super(parent);
    }
}
