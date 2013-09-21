package de.jaschastarke.bukkit.lib.configuration;

import java.util.Collection;

import de.jaschastarke.configuration.InvalidValueException;

public interface ConfigurableList<T> extends Collection<T> {
    public boolean addSetting(String e) throws InvalidValueException;
    public boolean removeSetting(String e);
    public void clearSettings();
}
