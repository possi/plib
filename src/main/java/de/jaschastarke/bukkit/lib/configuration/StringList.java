package de.jaschastarke.bukkit.lib.configuration;

import java.util.ArrayList;
import java.util.List;

public class StringList extends ArrayList<String> implements ConfigurableList<String> {
    private static final long serialVersionUID = -3332804314546536373L;
    
    public StringList(final List<String> asList) {
        super(asList);
    }

    @Override
    public boolean remove(final String e) {
        return this.remove((Object) e);
    }
}
