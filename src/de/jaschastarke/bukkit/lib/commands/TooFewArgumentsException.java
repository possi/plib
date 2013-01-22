package de.jaschastarke.bukkit.lib.commands;

public class TooFewArgumentsException extends CommandException {
    public TooFewArgumentsException() {
        super("Too few arguments given");
    }

    private static final long serialVersionUID = 7858204513525077833L;

}
