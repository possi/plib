package de.jaschastarke.bukkit.lib.configuration.command;

import de.jaschastarke.configuration.IConfigurationNode;
import de.jaschastarke.utils.StringUtil;

import java.util.ArrayList;
import java.util.List;

public class EnumConfigValue extends SimpleConfigValue {
    public EnumConfigValue(final ConfigList configList, final IConfigurationNode node) {
        super(configList, node);
    }

    @Override
    public List<String> tabComplete(final String[] args, final String[] chain) {
        List<String> hints = new ArrayList<String>();
        @SuppressWarnings("unchecked") Enum[] enumConstants = ((Class<Enum>) node.getType()).getEnumConstants();

        if (args.length > 0) {
            String val = StringUtil.join(args);
            for (Enum e : enumConstants) {
                if (e.name().toLowerCase().startsWith(val.toLowerCase()))
                    hints.add(e.name());
            }
        } else {
            for (Enum e : enumConstants)
                hints.add(e.name());
        }
        return hints;
    }
}
