package de.jaschastarke.bukkit.lib.configuration.command;

import de.jaschastarke.configuration.IConfigurationNode;
import de.jaschastarke.utils.StringUtil;

import java.util.ArrayList;
import java.util.List;

public class BooleanConfigValue extends SimpleConfigValue {
    public BooleanConfigValue(final ConfigList configList, final IConfigurationNode node) {
        super(configList, node);
    }

    private static final String TRUE = "true";
    private static final String FALSE = "false";

    @Override
    public List<String> tabComplete(final String[] args, final String[] chain) {
        if (args.length > 0) {
            String val = StringUtil.join(args);
            if (val.equals(TRUE) || val.equals("on") || val.equals("1") || val.equals(FALSE) || val.equals("off") || val.equals("0")) {
                return null;
            } else {
                List<String> hints = new ArrayList<String>();
                if (TRUE.startsWith(val.toLowerCase())) {
                    hints.add(TRUE);
                }
                if (FALSE.startsWith(val.toLowerCase())) {
                    hints.add(FALSE);
                }
                return hints;
            }
        }
        return null;
    }
}
