package de.jaschastarke.bukkit.lib.configuration.command;

import java.util.ArrayList;
import java.util.List;

import de.jaschastarke.bukkit.lib.chat.ChatFormattings;
import de.jaschastarke.bukkit.lib.chat.IFormatter;
import de.jaschastarke.bukkit.lib.chat.IPagination;
import de.jaschastarke.bukkit.lib.commands.CommandContext;
import de.jaschastarke.bukkit.lib.configuration.ConfigurableList;
import de.jaschastarke.configuration.IConfigurationNode;
import de.jaschastarke.configuration.InvalidValueException;
import de.jaschastarke.utils.ArrayUtil;
import de.jaschastarke.utils.StringUtil;

public class ListConfigValue extends AbstractConfigValue implements ITabComplete {
    public static final String ADD = "add";
    public static final String REMOVE = "remove";
    
    public ListConfigValue(final ConfigList configList, final IConfigurationNode node) {
        super(configList, node);
        if (!ConfigurableList.class.isAssignableFrom(node.getType()))
            throw new IllegalArgumentException("ConfigurationNode-Type isn't a ConfigurableList");
    }
    
    @Override
    public String getUsage() {
        return node.getName() + " <" + ADD + "|" + REMOVE + "> <value>";
    }

    @Override
    public boolean process(final CommandContext context, final String[] args, final String[] chain) {
        if (args.length == 0) {
            context.response(displayOption(context, chain));
            return true;
        } else {
            if (args.length >= 2 && (args[0].equals(ADD) || args[0].equals(REMOVE))) {
                final IFormatter f = context.getFormatter();
                String value = StringUtil.join(args, 1);
                try {
                    ConfigurableList<?> values = (ConfigurableList<?>) config.getValue(node);
                    boolean success = true;
                    if (args[0].equals(ADD))
                        values.addSetting(value);
                    else if (args[0].equals(REMOVE))
                        success = values.removeSetting(value);
                    config.setValue(node, value);
                    if (success)
                        context.response(f.formatString(ChatFormattings.SUCCESS, f.getString("bukkit.help.configuration.setted", node.getName())));
                    else
                        context.response(f.formatString(ChatFormattings.ERROR, f.getString("bukkit.help.configuration.value_not_found", node.getName())));
                } catch (InvalidValueException e) {
                    context.response(f.formatString(ChatFormattings.ERROR, e.getMessage()));
                }
                return true;
            } else {
                context.response(displayOption(context, chain));
                return true;
            }
        }
    }
    
    protected String displayOption(final CommandContext context, final String[] chain) {
        final IFormatter f = context.getFormatter();
        IPagination desc = buildOptionDescription(context, chain);
        desc.appendln(f.formatString(ChatFormattings.LABEL, f.getString("bukkit.help.configuration.current_values")));
        
        ConfigurableList<?> data = (ConfigurableList<?>) config.getValue(node);
        for (Object entry : data) {
            desc.append(" - ");
            desc.appendln(entry.toString());
        }
        
        return desc.toString();
    }

    @Override
    public List<String> tabComplete(final String[] args, final String[] chain) {
        if (args.length > 0) {
            if ((args[0].equals(ADD) || args[0].equals(REMOVE)) && args.length > 1) {
                ConfigurableList<?> values = (ConfigurableList<?>) config.getValue(node);
                if (values instanceof ITabComplete) {
                    return ((ITabComplete) values).tabComplete(ArrayUtil.getRange(args, 1), ArrayUtil.push(chain, args[0]));
                }
            } else if (args.length == 1) {
                List<String> hints = new ArrayList<String>();
                if (ADD.toLowerCase().startsWith(args[0].toLowerCase())) {
                    hints.add(ADD);
                } else if (REMOVE.toLowerCase().startsWith(args[0].toLowerCase())) {
                    hints.add(REMOVE);
                }
                return hints;
            }
        }
        return null;
    }

}
