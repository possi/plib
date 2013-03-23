package de.jaschastarke.bukkit.lib.configuration.command;

import static de.jaschastarke.bukkit.lib.chat.AbstractFormatter.NEWLINE;

import java.util.Map;

import de.jaschastarke.bukkit.lib.chat.ChatFormattings;
import de.jaschastarke.bukkit.lib.chat.IFormatter;
import de.jaschastarke.bukkit.lib.chat.IPagination;
import de.jaschastarke.bukkit.lib.chat.NoPager;
import de.jaschastarke.bukkit.lib.commands.CommandContext;
import de.jaschastarke.bukkit.lib.commands.HelpCommand;
import de.jaschastarke.bukkit.lib.commands.ICommand;
import de.jaschastarke.bukkit.lib.configuration.Configuration;
import de.jaschastarke.configuration.IConfigurationNode;
import de.jaschastarke.utils.StringUtil;

public abstract class AbstractConfigValue implements IConfigValueCommand {
    private static final String SPACE = " ";
    protected ConfigList configList;
    protected Configuration config;
    protected IConfigurationNode node;
    
    public AbstractConfigValue(final ConfigList configList, final IConfigurationNode node) {
        this.configList = configList;
        this.node = node;
        config = configList.getConfiguration();
    }
    
    public String getUsage() {
        return node.getName() + " [newvalue]";
    }

    protected String getUsageLine(final CommandContext context, final String[] chain) {
        final IFormatter f = context.getFormatter();
        StringBuilder desc = new StringBuilder();
        desc.append(f.formatString(ChatFormattings.LABEL, f.getString("bukkit.help.usage")));
        if (context.isPlayer())
            desc.append(ICommand.PREFIX);
        if (context.getCommandChain().size() > 0) {
            for (Map.Entry<ICommand, String> entry : context.getCommandChain().entrySet()) {
                if (entry.getKey() == this) {
                    desc.append(f.formatString(ChatFormattings.COMMAND, entry.getValue()));
                    desc.append(SPACE);
                    break;
                } else {
                    desc.append(f.formatString(ChatFormattings.USED_COMMAND, entry.getValue()));
                    desc.append(SPACE);
                }
            }
        } else {
            desc.append(SPACE);
        }
        for (int i = 0; i < chain.length - 1; i++) {
            desc.append(f.formatString(ChatFormattings.USED_COMMAND, chain[i]));
            desc.append(SPACE);
        }
        desc.append(f.formatString(ChatFormattings.ARGUMENTS, HelpCommand.formatUsage(f, this.getUsage())));
        return desc.toString();
    }
    
    protected IPagination buildOptionDescription(final CommandContext context, final String[] chain) {
        final IFormatter f = context.getFormatter();
        //IPagination desc = f.newPaginiation();
        IPagination desc = new NoPager();
        desc.append(f.formatString(ChatFormattings.TEXT_HEADER, configList.getCommand().getPackageName()));
        desc.append(NEWLINE);
        if (node.isReadOnly()) {
            desc.append(f.formatString(ChatFormattings.LABEL, f.getString("bukkit.help.configuration.node")));
            if (chain.length > 0)
                desc.append(f.formatString(ChatFormattings.USED_COMMAND, StringUtil.join(chain)));
            desc.append(f.formatString(ChatFormattings.ARGUMENTS, node.getName()));
            desc.append(NEWLINE);
        } else {
            desc.append(getUsageLine(context, chain));
            desc.append(NEWLINE);
        }
        if (node.getDescription() != null) {
            desc.append(f.formatString(ChatFormattings.DESCRIPTION, node.getDescription()));
            desc.append(NEWLINE);
        }
        return desc;
    }

}
