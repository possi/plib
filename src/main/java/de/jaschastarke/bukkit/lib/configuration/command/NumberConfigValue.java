package de.jaschastarke.bukkit.lib.configuration.command;

import de.jaschastarke.bukkit.lib.chat.IFormatter;
import de.jaschastarke.bukkit.lib.commands.CommandContext;
import de.jaschastarke.configuration.IConfigurationNode;
import de.jaschastarke.utils.StringUtil;

public class NumberConfigValue extends SimpleConfigValue {
    public NumberConfigValue(final ConfigList configList, final IConfigurationNode node) {
        super(configList, node);
    }

    @Override
    public boolean process(final CommandContext context, final String[] args, final String[] chain) {
        if (args.length == 0) {
            context.response(displayOption(context, chain));
            return true;
        } else {
            final IFormatter f = context.getFormatter();
            String strValue = StringUtil.join(args);

            if (Integer.class.isAssignableFrom(node.getType())) {
                setValue(Integer.valueOf(strValue), context, args, chain);
            } else {
                setValue(Double.valueOf(strValue), context, args, chain);
            }
            return true;
        }
    }
}
