package de.jaschastarke.bukkit.lib.configuration;

import de.jaschastarke.LocaleString;
import de.jaschastarke.bukkit.lib.chat.ChatFormattings;
import de.jaschastarke.bukkit.lib.chat.IFormatter;
import static de.jaschastarke.bukkit.lib.chat.IFormatter.NEWLINE;
import de.jaschastarke.bukkit.lib.commands.CommandContext;
import de.jaschastarke.bukkit.lib.commands.CommandException;
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
    
    public ConfigCommand(final Configuration config, final IAbstractPermission perm) {
        conf = config;
        perms = new IAbstractPermission[]{perm};
    }

    @Override
    public String getUsage() {
        return "[subgroups...] <option> [newvalue]";
    }
    @Override
    public CharSequence getDescription() {
        return new LocaleString("bukkit.help.configuration.desc");
    }
    @Override
    public CharSequence getPackageName() {
        return new LocaleString("bukkit.help.configuration.title");
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
        return process(context, conf, args);
    }

    @Override
    public IAbstractPermission[] getRequiredPermissions() {
        return perms;
    }
    
    protected boolean process(final CommandContext context, final Configuration config, final String[] args) throws MissingPermissionCommandException {
        for (IAbstractPermission perm : perms) {
            if (!context.checkPermission(perm))
                throw new MissingPermissionCommandException(perm);
        }
        IFormatter f = context.getFormatter();
        if (args.length == 0) {
            context.response(displayOptions(config, context.getFormatter()));
            return true;
        }
        IConfigurationNode node = getConfNode(config, args[0]);
        if (node == null) {
            context.response(f.formatString(ChatFormattings.ERROR, f.getString("bukkit.help.configuration.node_not_found")));
            context.response(displayOptions(config, context.getFormatter()));
            return true;
        } else if (node instanceof Configuration) {
            return process(context, (Configuration) node, ArrayUtil.getRange(args, 1));
        } else if (args.length == 1) {
            context.response(displayOption(config, node, context.getFormatter()));
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
    
    protected String displayOptions(final Configuration config, final IFormatter f) {
        StringBuilder desc = new StringBuilder();
        desc.append(f.formatString(ChatFormattings.TEXT_HEADER, getPackageName()));
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
    
    protected String displayOption(final Configuration config, final IConfigurationNode node, final IFormatter f) {
        StringBuilder desc = new StringBuilder();
        desc.append(f.formatString(ChatFormattings.TEXT_HEADER, node.getName()));
        desc.append(NEWLINE);
        desc.append(f.formatString(ChatFormattings.DESCRIPTION, node.getDescription()));
        desc.append(NEWLINE);
        desc.append(f.formatString(ChatFormattings.LABEL, f.getString("bukkit.help.configuration.current_value")));
        desc.append(config.getValue(node));
        return desc.toString();
    }
}
