package de.jaschastarke.bukkit.lib.configuration.command;

import de.jaschastarke.bukkit.lib.chat.ChatFormattings;
import de.jaschastarke.bukkit.lib.chat.IFormatter;
import de.jaschastarke.bukkit.lib.chat.IPagination;
import de.jaschastarke.bukkit.lib.commands.CommandContext;
import de.jaschastarke.configuration.IConfigurationNode;
import de.jaschastarke.configuration.InvalidValueException;
import de.jaschastarke.utils.StringUtil;

public class SimpleConfigValue extends AbstractConfigValue {
    public SimpleConfigValue(final ConfigList configList, final IConfigurationNode node) {
        super(configList, node);
    }

    @Override
    public boolean process(final CommandContext context, final String[] args, final String[] chain) {
        if (args.length == 0) {
            context.response(displayOption(context, chain));
            return true;
        } else {
            final IFormatter f = context.getFormatter();
            String value = StringUtil.join(args);
            try {
                config.setValue(node, value);
                context.response(f.formatString(ChatFormattings.SUCCESS, f.getString("bukkit.help.configuration.setted", node.getName())));
            } catch (InvalidValueException e) {
                context.response(f.formatString(ChatFormattings.ERROR, e.getMessage()));
            }
            return true;
        }
    }
    
    protected String displayOption(final CommandContext context, final String[] chain) {
        final IFormatter f = context.getFormatter();
        IPagination desc = buildOptionDescription(context, chain);
        desc.append(f.formatString(ChatFormattings.LABEL, f.getString("bukkit.help.configuration.current_value")));
        desc.append(config.getValue(node).toString());
        return desc.toString();
    }
}
