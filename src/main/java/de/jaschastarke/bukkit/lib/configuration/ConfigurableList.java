package de.jaschastarke.bukkit.lib.configuration;

import java.util.Collection;

import de.jaschastarke.configuration.InvalidValueException;

public interface ConfigurableList<T> extends Collection<T> {
    public boolean add(String e) throws InvalidValueException;
    public boolean remove(String e);
    public void clear();
}
