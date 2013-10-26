package de.jaschastarke.bukkit.lib.configuration;

import java.util.ArrayList;
import java.util.List;

import de.jaschastarke.bukkit.lib.configuration.command.ITabComplete;
import de.jaschastarke.bukkit.lib.configuration.command.ListConfigValue;
import de.jaschastarke.configuration.InvalidValueException;

public class StringList extends ArrayList<String> implements ConfigurableList<String>, ITabComplete {
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
    
    public boolean containsIgnoreCase(final String val) {
        for (String str : this) {
            if (str.equalsIgnoreCase(val))
                return true;
        }
        return false;
    }
    
    public List<String> tabComplete(final String[] args, final String[] chain) {
        if (args.length > 0 && chain.length > 0) {
            if (chain[chain.length - 1].equalsIgnoreCase(ListConfigValue.REMOVE)) {
                List<String> hints = new ArrayList<String>();
                for (String s : this) {
                    if (s.toLowerCase().startsWith(args[0].toLowerCase()))
                        hints.add(s);
                }
                return hints;
            }
        }
        return null;
    }
}
