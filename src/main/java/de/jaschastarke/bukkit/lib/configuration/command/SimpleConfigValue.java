package de.jaschastarke.bukkit.lib.configuration.command;

import java.util.ArrayList;
import java.util.List;

import de.jaschastarke.bukkit.lib.chat.ChatFormattings;
import de.jaschastarke.bukkit.lib.chat.IFormatter;
import de.jaschastarke.bukkit.lib.chat.IPagination;
import de.jaschastarke.bukkit.lib.commands.CommandContext;
import de.jaschastarke.configuration.IConfigurationNode;
import de.jaschastarke.configuration.InvalidValueException;
import de.jaschastarke.utils.StringUtil;

public class SimpleConfigValue extends AbstractConfigValue implements ITabComplete {
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
                if (config instanceof ICommandConfigCallback) {
                    ICommandConfigCallback.Callback cb = new ICommandConfigCallback.Callback(node, value, context, args, chain);
                    ((ICommandConfigCallback) config).onConfigCommandChange(cb);
                    if (!cb.isCancelled())
                        config.setValue(node, cb.getValue());
                } else {
                    config.setValue(node, value);
                }
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

    @Override
    public List<String> tabComplete(String[] args, String[] chain) {
        if (args.length > 0) {
            if (Boolean.TYPE.isAssignableFrom(node.getType())) {
                String val = StringUtil.join(args);
                if (val.equals("true") || val.equals("on") || val.equals("1") || val.equals("false") || val.equals("off") || val.equals("0")) {
                    return null;
                } else {
                    List<String> hints = new ArrayList<String>();
                    if ("true".startsWith(val.toLowerCase())) {
                        hints.add("true");
                    }
                    if ("false".startsWith(val.toLowerCase())) {
                        hints.add("false");
                    }
                    return hints;
                }
            }
        }
        return null;
    }
}
