package de.jaschastarke.bukkit.lib.configuration.command;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import de.jaschastarke.bukkit.lib.chat.ChatFormattings;
import de.jaschastarke.bukkit.lib.chat.IFormatter;
import de.jaschastarke.bukkit.lib.chat.IPagination;
import de.jaschastarke.bukkit.lib.chat.NoPager;
import de.jaschastarke.bukkit.lib.commands.CommandContext;
import de.jaschastarke.bukkit.lib.commands.HelpCommand;
import de.jaschastarke.bukkit.lib.commands.ICommand;
import de.jaschastarke.bukkit.lib.configuration.Configuration;
import de.jaschastarke.configuration.ConfigurationStyle;
import de.jaschastarke.configuration.IBaseConfigurationNode;
import de.jaschastarke.configuration.IConfigurationNode;
import de.jaschastarke.utils.ArrayUtil;

public class ConfigList {
    private static final String SPACE = " ";
    
    private Configuration config;
    private ConfigCommand command;
    
    public ConfigList(final Configuration conf, final ConfigCommand command) {
        this.config = conf;
        this.command = command;
    }
    
    public Configuration getConfiguration() {
        return config;
    }
    public ConfigCommand getCommand() {
        return command;
    }
    
    public String getUsage() {
        return "<option> [newvalue]";
    }
    
    public boolean process(final CommandContext context, final String[] args, final String[] chain) {
        IFormatter f = context.getFormatter();
        if (args.length == 0) {
            context.response(displayOptions(context, chain));
            return true;
        }
        IBaseConfigurationNode node = getConfNode(args[0]);
        if (node == null) {
            context.response(f.formatString(ChatFormattings.ERROR, f.getString("bukkit.help.configuration.node_not_found")));
            context.response(displayOptions(context, chain));
            return true;
        } else if (node instanceof Configuration) {
            return new ConfigList((Configuration) node, command).process(context, ArrayUtil.getRange(args, 1), ArrayUtil.push(chain, args[0]));
        } else if (node instanceof IConfigurationNode) {
            return getValueCommand((IConfigurationNode) node).process(context, ArrayUtil.getRange(args, 1), ArrayUtil.push(chain, args[0]));
        } else {
            throw new IllegalArgumentException("ConfigNode is neither a IConfiguratioNode nor a Configuration");
        }
    }
    
    protected String displayOptions(final CommandContext context, final String[] chain) {
        final IFormatter f = context.getFormatter();
        IPagination desc = new NoPager();
        desc.appendln(f.formatString(ChatFormattings.TEXT_HEADER, command.getPackageName()));
        desc.appendln(getUsageLine(context, chain));
        if (config.getConfigNodes().size() > 0) {
            desc.append(f.formatString(ChatFormattings.LABEL, f.getString("bukkit.help.configuration.options")));
            List<IBaseConfigurationNode> configNodes = config.getConfigNodes();
            for (int i = 0; i < configNodes.size(); i++) {
                if (!(configNodes.get(i) instanceof IConfigurationNode) || ((IConfigurationNode) configNodes.get(i)).getStyle() != ConfigurationStyle.HIDDEN) {
                    if (i > 0)
                        desc.append(", ");
                    desc.append(f.formatString(ChatFormattings.REQUIRED_ARGUMENT, configNodes.get(i).getName()));
                }
            } 
        }
        return desc.toString();
    }
    
    protected IBaseConfigurationNode getConfNode(final String nodename) {
        for (IBaseConfigurationNode confnode : config.getConfigNodes()) {
            if (confnode.getName().equals(nodename)) {
                return confnode;
            }
        }
        return null;
    }
    
    protected String getUsageLine(final CommandContext context, final String[] chain) {
        final IFormatter f = context.getFormatter();
        StringBuilder desc = new StringBuilder();
        desc.append(f.formatString(ChatFormattings.LABEL, f.getString("bukkit.help.usage")));
        if (context.isPlayer())
            desc.append(ICommand.PREFIX);
        if (context.getCommandChain().size() > 0) {
            for (Map.Entry<ICommand, String> entry : context.getCommandChain().entrySet()) {
                if (entry.getKey() == command) {
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
        for (String group : chain) {
            desc.append(f.formatString(ChatFormattings.USED_COMMAND, group));
            desc.append(SPACE);
        }
        desc.append(f.formatString(ChatFormattings.ARGUMENTS, HelpCommand.formatUsage(f, this.getUsage())));
        return desc.toString();
    }
    
    protected IConfigValueCommand getValueCommand(final IConfigurationNode node) {
        if (List.class.isAssignableFrom(node.getType())) {
            return new ListConfigValue(this, node);
        } else {
            return new SimpleConfigValue(this, node);
        }
    }

    public List<String> tabComplete(final CommandContext context, final String[] args, final String[] chain) {
        if (args.length > 0) {
            List<String> hints = new ArrayList<String>();
            IBaseConfigurationNode node = getConfNode(args[0]);
            if (node != null && node instanceof Configuration) {
                return new ConfigList((Configuration) node, command).tabComplete(context, ArrayUtil.getRange(args, 1), ArrayUtil.push(chain, args[0]));
            } else if (node != null && node instanceof IConfigurationNode) {
                IConfigValueCommand conf = getValueCommand((IConfigurationNode) node);
                if (conf instanceof ITabComplete) {
                    return ((ITabComplete) conf).tabComplete(ArrayUtil.getRange(args, 1), new String[]{args[0]});
                }
            } else if (node == null && args.length == 1) {
                for (IBaseConfigurationNode confnode : config.getConfigNodes()) {
                    if (confnode.getName().toLowerCase().startsWith(args[0].toLowerCase())) {
                        hints.add(confnode.getName());
                    }
                }
            }
            return hints;
        }
        return null;
    }
}
