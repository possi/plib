package de.jaschastarke.bukkit.lib.commands;

public class IllegalCommandMethodException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public IllegalCommandMethodException(final Throwable parent) {
        super(parent);
    }
}
