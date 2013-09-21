package de.jaschastarke.bukkit.lib.configuration;

import java.util.ArrayList;
import java.util.List;

import de.jaschastarke.configuration.InvalidValueException;

public class StringList extends ArrayList<String> implements ConfigurableList<String> {
    private static final long serialVersionUID = -3332804314546536373L;
    
    public StringList(final List<String> asList) {
        super(asList);
    }

    @Override
    public boolean removeSetting(final String e) {
        return remove((Object) e);
    }

    @Override
    public boolean addSetting(final String e) throws InvalidValueException {
        return add(e);
    }

    @Override
    public void clearSettings() {
        clear();
    }
}
