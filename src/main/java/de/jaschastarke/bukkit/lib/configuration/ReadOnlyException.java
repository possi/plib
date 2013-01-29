package de.jaschastarke.bukkit.lib.configuration;

import de.jaschastarke.configuration.InvalidValueException;

public class ReadOnlyException extends InvalidValueException {
    private static final long serialVersionUID = 8022683440962797857L;

    public ReadOnlyException() {
        super("This property can't be changed");
    }

}
