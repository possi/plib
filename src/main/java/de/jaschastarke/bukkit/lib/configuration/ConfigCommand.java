package de.jaschastarke.bukkit.lib.configuration;

import java.util.Map;

import de.jaschastarke.LocaleString;
import de.jaschastarke.bukkit.lib.chat.ChatFormattings;
import de.jaschastarke.bukkit.lib.chat.IFormatter;
import static de.jaschastarke.bukkit.lib.chat.IFormatter.NEWLINE;
import de.jaschastarke.bukkit.lib.commands.CommandContext;
import de.jaschastarke.bukkit.lib.commands.CommandException;
import de.jaschastarke.bukkit.lib.commands.HelpCommand;
import de.jaschastarke.bukkit.lib.commands.ICommand;
import de.jaschastarke.bukkit.lib.commands.IHelpDescribed;
import de.jaschastarke.bukkit.lib.commands.MissingPermissionCommandException;
import de.jaschastarke.configuration.IConfigurationNode;
import de.jaschastarke.configuration.InvalidValueException;
import de.jaschastarke.minecraft.lib.permissions.IAbstractPermission;
import de.jaschastarke.utils.ArrayUtil;
import de.jaschastarke.utils.StringUtil;

public class ConfigCommand implements ICommand, IHelpDescribed {
    //ConfigHelpCommand help = new ConfigHelpCommand();
    private Configuration conf;
    private IAbstractPermission[] perms;
    private CharSequence packageName = new LocaleString("bukkit.help.configuration.title");
    private static final String SPACE = " ";
    
    public ConfigCommand(final Configuration config, final IAbstractPermission perm) {
        conf = config;
        perms = new IAbstractPermission[]{perm};
    }

    public String getUsage(final String option) {
        return option == null ? getUsage() : (option + " [newvalue]");
    }
    @Override
    public String getUsage() {
        return "[subgroups...] <option> [newvalue]";
    }
    @Override
    public CharSequence getDescription() {
        return new LocaleString("bukkit.help.configuration.desc");
    }

    public void setPackageName(final String string) {
        packageName = string;
    }
    @Override
    public CharSequence getPackageName() {
        return packageName;
    }
    @Override
    public String getName() {
        return "config";
    }
    @Override
    public String[] getAliases() {
        return new String[] {"conf", "set"};
    }
    @Override
    public boolean execute(final CommandContext context, final String[] args) throws MissingPermissionCommandException, CommandException {
        return process(context, conf, args, new String[0]);
    }

    @Override
    public IAbstractPermission[] getRequiredPermissions() {
        return perms;
    }
    
    protected boolean process(final CommandContext context, final Configuration config, final String[] args, final String[] chain) throws MissingPermissionCommandException {
        for (IAbstractPermission perm : perms) {
            if (!context.checkPermission(perm))
                throw new MissingPermissionCommandException(perm);
        }
        IFormatter f = context.getFormatter();
        if (args.length == 0) {
            context.response(displayOptions(config, context, chain));
            return true;
        }
        IConfigurationNode node = getConfNode(config, args[0]);
        if (node == null) {
            context.response(f.formatString(ChatFormattings.ERROR, f.getString("bukkit.help.configuration.node_not_found")));
            context.response(displayOptions(config, context, chain));
            return true;
        } else if (node instanceof Configuration) {
            return process(context, (Configuration) node, ArrayUtil.getRange(args, 1), ArrayUtil.push(chain, args[0]));
        } else if (args.length == 1) {
            context.response(displayOption(config, node, context, chain));
            return true;
        } else {
            String value = StringUtil.join(args, 1);
            try {
                config.setValue(node, value);
                context.response(f.formatString(ChatFormattings.SUCCESS, f.getString("bukkit.help.configuration.setted", node.getName())));
            } catch (InvalidValueException e) {
                context.response(f.formatString(ChatFormattings.ERROR, e.getMessage()));
            }
            return true;
        }
    }

    protected IConfigurationNode getConfNode(final Configuration config, final String nodename) {
        for (IConfigurationNode confnode : config.getConfigNodes()) {
            if (confnode.getName().equals(nodename)) {
                return confnode;
            }
        }
        return null;
    }
    
    protected String displayOptions(final Configuration config, final CommandContext context, final String[] chain) {
        final IFormatter f = context.getFormatter();
        StringBuilder desc = new StringBuilder();
        desc.append(f.formatString(ChatFormattings.TEXT_HEADER, getPackageName()));
        desc.append(NEWLINE);
        desc.append(getUsageLine(context, chain, null));
        desc.append(NEWLINE);
        if (config.getConfigNodes().size() > 0) {
            desc.append(f.formatString(ChatFormattings.LABEL, f.getString("bukkit.help.configuration.options")));
            for (IConfigurationNode confnode : config.getConfigNodes()) {
                desc.append(f.formatString(ChatFormattings.REQUIRED_ARGUMENT, confnode.getName()));
                desc.append(", ");
            }
            desc.replace(desc.length() - 2, desc.length(), ""); // remove last , 
        }
        return desc.toString();
    }
    
    protected String getUsageLine(final CommandContext context, final String[] chain, final String option) {
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
        for (String group : chain) {
            desc.append(f.formatString(ChatFormattings.USED_COMMAND, group));
            desc.append(SPACE);
        }
        desc.append(f.formatString(ChatFormattings.ARGUMENTS, HelpCommand.formatUsage(f, this.getUsage(option))));
        return desc.toString();
    }
    
    protected String displayOption(final Configuration config, final IConfigurationNode node, final CommandContext context, final String[] chain) {
        final IFormatter f = context.getFormatter();
        StringBuilder desc = new StringBuilder();
        desc.append(f.formatString(ChatFormattings.TEXT_HEADER, this.getPackageName()));
        desc.append(NEWLINE);
        if (node.isReadOnly()) {
            desc.append(f.formatString(ChatFormattings.LABEL, f.getString("bukkit.help.configuration.node")));
            if (chain.length > 0)
                desc.append(f.formatString(ChatFormattings.USED_COMMAND, StringUtil.join(chain)));
            desc.append(f.formatString(ChatFormattings.ARGUMENTS, node.getName()));
            desc.append(NEWLINE);
        } else {
            desc.append(getUsageLine(context, chain, node.getName()));
            desc.append(NEWLINE);
        }
        if (node.getDescription() != null) {
            desc.append(f.formatString(ChatFormattings.DESCRIPTION, node.getDescription()));
            desc.append(NEWLINE);
        }
        desc.append(f.formatString(ChatFormattings.LABEL, f.getString("bukkit.help.configuration.current_value")));
        desc.append(config.getValue(node));
        return desc.toString();
    }
}
