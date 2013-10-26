package de.jaschastarke.bukkit.lib.configuration.command;

import java.util.ArrayList;
import java.util.List;

import de.jaschastarke.LocaleString;
import de.jaschastarke.bukkit.lib.chat.ChatFormattings;
import de.jaschastarke.bukkit.lib.chat.IFormatter;
import de.jaschastarke.bukkit.lib.commands.CommandContext;
import de.jaschastarke.bukkit.lib.commands.CommandException;
import de.jaschastarke.bukkit.lib.commands.IHelpDescribed;
import de.jaschastarke.bukkit.lib.commands.ITabCommand;
import de.jaschastarke.bukkit.lib.commands.MissingPermissionCommandException;
import de.jaschastarke.bukkit.lib.configuration.Configuration;
import de.jaschastarke.configuration.IBaseConfigurationNode;
import de.jaschastarke.configuration.ISaveableConfiguration;
import de.jaschastarke.minecraft.lib.permissions.IAbstractPermission;

public class ConfigCommand implements ITabCommand, IHelpDescribed {
    //ConfigHelpCommand help = new ConfigHelpCommand();
    private Configuration conf;
    private IAbstractPermission[] perms;
    private CharSequence packageName = new LocaleString("bukkit.help.configuration.title");
    private static final String SAVE = "save";

    public ConfigCommand(final Configuration config) {
        this(config, null);
    }
    public ConfigCommand(final Configuration config, final IAbstractPermission perm) {
        conf = config;
        if (perm != null)
            perms = new IAbstractPermission[]{perm};
        else
            perms = new IAbstractPermission[]{};
    }

    public String getUsage(final String option) {
        return option == null ? getUsages()[0] : (option + " [newvalue]");
    }
    @Override
    public String[] getUsages() {
        List<String> usages = new ArrayList<String>();
        usages.add("<option> [newvalue]");
        for (IBaseConfigurationNode sc : conf.getConfigNodes()) {
            if (sc instanceof Configuration) {
                usages.add("<subgroup> ...");
                break;
            }
        }
        if (conf instanceof ISaveableConfiguration)
            usages.add(SAVE);
        return usages.toArray(new String[usages.size()]);
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
        for (IAbstractPermission perm : perms) {
            if (!context.checkPermission(perm))
                throw new MissingPermissionCommandException(perm);
        }
        if (args.length == 1 && args[0].equals(SAVE) && conf instanceof ISaveableConfiguration) {
            return processSave(context);
        }
        return new ConfigList(conf, this).process(context, args, new String[0]);
    }
    @Override
    public List<String> tabComplete(final CommandContext context, final String[] args) {
        for (IAbstractPermission perm : perms) {
            if (!context.checkPermission(perm))
                return null;
        }
        List<String> hints = new ConfigList(conf, this).tabComplete(context, args, new String[0]);
        if (hints != null && (args.length == 0 || SAVE.toLowerCase().startsWith(args[0])))
            hints.add(SAVE);
        return hints;
    }

    @Override
    public IAbstractPermission[] getRequiredPermissions() {
        return perms;
    }

    private boolean processSave(final CommandContext context) {
        IFormatter f = context.getFormatter();
        ((ISaveableConfiguration) conf).save();
        context.response(f.formatString(ChatFormattings.SUCCESS, f.getString("bukkit.help.configuration.save")));
        return true;
    }
}
