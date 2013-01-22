package de.jaschastarke.bukkit.lib.commands;

import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.ArrayUtils;
import org.bukkit.ChatColor;

import de.jaschastarke.LocaleString;
import de.jaschastarke.bukkit.lib.chat.IFormatter;
import de.jaschastarke.minecraft.lib.permissions.IAbstractPermission;

public class HelpCommand implements ICommand, ICommandListHelp {
    private static Pattern CHANGE_DEFAULT = Pattern.compile(ChatColor.RESET.toString());
    private static Pattern USAGE_PARSE = Pattern.compile("\\B(?:(\\-\\w)|\\[(\\w+)\\]|<(\\w+)>)\\B");
    
    private ICommandListing mainCommand;
    public HelpCommand(ICommandListing parentCommand) {
        mainCommand = parentCommand;
    }
    @Override
    public String getName() {
        return "help";
    }
    @Override
    public String[] getAliases() {
        return new String[]{"?"};
    }
    public ICommandListing getParentCommand() {
        return mainCommand;
    }
    @Override
    public boolean execute(CommandContext context, String[] args) throws MissingPermissionCommandException, CommandException {
        if (args.length > 0) {
            for (ICommand command : mainCommand.getCommandList()) {
                if (command.getName().equals(args[0])) {
                    if (command instanceof IHelpDescribed) {
                        return displayDescribedHelpCommand(context, (IHelpDescribed) command);
                    }
                }
            }
            for (ICommand command : mainCommand.getCommandList()) {
                if (ArrayUtils.contains(command.getAliases(), args[0])) {
                    if (command instanceof IHelpDescribed) {
                        return displayDescribedHelpCommand(context, (IHelpDescribed) command, args[0]);
                    }
                }
            }
        }
        if (mainCommand instanceof IHelpDescribed)
            return displayDescribedHelpCommand(context, (IHelpDescribed) mainCommand);
        executeCommandList(context);
        return true;
    }

    public boolean executeCommandList(CommandContext context) {
        if (mainCommand instanceof IHelpDescribed)
            return displayDescribedHelpCommand(context, (IHelpDescribed) mainCommand);
        IFormatter format = context.getFormatter();
        
        StringBuilder text = new StringBuilder("/");
        for (Entry<ICommand, String> handledcommand : context.getCommandChain().entrySet()) {
            text.append(handledcommand.getValue());
            text.append(" ");
        }
        text.append("- ");
        text.append(format.getString("bukkit.help.available_commands"));
        text.append("\n");
        for (ICommand command : mainCommand.getCommandList()) {
            //text.append(" - ");
            text.append(command.getName());
            text.append(", ");
        }
        text.replace(text.length() - 2, text.length(), "");
        context.response(text.toString().trim());
        return true;
    }
    
    protected boolean displayDescribedHelpCommand(CommandContext context, IHelpDescribed command) {
        return displayDescribedHelpCommand(context, command, null);
    }
    protected boolean displayDescribedHelpCommand(CommandContext context, IHelpDescribed command, String usedAlias) {
        IFormatter format = context.getFormatter();
        
        StringBuilder desc = new StringBuilder();
        desc.append(ChatColor.AQUA);
        desc.append("=== ");
        desc.append(command.getPackageName());
        desc.append(" ===");
        desc.append("\n");
        
        boolean something = false;
        
        String usage = command.getUsage();
        if (usage != null && !usage.isEmpty()) {
            something = true;
            desc.append(ChatColor.BLUE);
            desc.append(format.getString("bukkit.help.syntax"));
        }
        if (context.isPlayer()) {
            desc.append(ChatColor.DARK_GRAY);
            desc.append("/");
        }
        desc.append(ChatColor.DARK_AQUA);
        for (Map.Entry<ICommand, String> pcommand : context.getCommandChain().entrySet()) {
            if (pcommand.getKey() == command) {
                usedAlias = pcommand.getValue();
                break;
            } else if (pcommand.getKey() == this) {
                break;
            }
            desc.append(pcommand.getValue());
            desc.append(" ");
        }
        desc.append(ChatColor.GREEN);
        desc.append(usedAlias != null ? usedAlias : command.getName());
        desc.append(" ");
        if (usage != null && !usage.isEmpty()) {
            desc.append(ChatColor.DARK_GREEN);
            desc.append(CHANGE_DEFAULT.matcher(formatUsage(format, usage)).replaceAll(ChatColor.DARK_GREEN.toString()));
        }
        desc.append("\n");
        
        String[] aliases = buildAliases(command.getAliases(), usedAlias, command.getName());
        if (aliases != null && aliases.length > 0) {
            something = true;
            desc.append(ChatColor.BLUE);
            desc.append(format.getString("bukkit.help.aliases"));
            desc.append(ChatColor.RESET);
            for (int i = 0; i < aliases.length; i++) {
                if (i > 0)
                    desc.append(", ");
                desc.append(ChatColor.GREEN);
                desc.append(aliases[i]);
                desc.append(ChatColor.RESET);
            }
            desc.append("\n");
        }
        
        CharSequence description = command.getDescription();
        if (description != null) {
            String d;
            if (description instanceof LocaleString)
                d = format.getString(((LocaleString) description).getRawValue(), ((LocaleString) description).getObjects());
            else
                d = description.toString();
            something = true;
            desc.append(ChatColor.GOLD);
            desc.append(CHANGE_DEFAULT.matcher(d.trim()).replaceAll(ChatColor.GOLD.toString()));
            desc.append("\n");
        }
        
        if (command instanceof ICommandListing && ((ICommandListing) command).getCommandList().size() > 0) {
            something = true;
            desc.append(ChatColor.DARK_AQUA);
            desc.append(format.getString("bukkit.help.available_sub_commands"));
            desc.append(ChatColor.RESET);
            for (ICommand scommand : mainCommand.getCommandList()) {
                if (!(scommand instanceof IHelpDescribed) || checkPermisisons(context, ((IHelpDescribed) scommand).getRequiredPermissions())) {
                    desc.append(scommand.getName());
                    desc.append(", ");
                }
            }
            desc.replace(desc.length() - 2, desc.length(), ""); // remove latest ", "
            desc.append("\n");
        }
        
        if (something) {
            context.response(desc.toString().trim());
            return true;
        } else {
            return false;
        }
    }
    
    private String[] buildAliases(String[] aliases, String usedAlias, String originalName) {
        if (aliases != null && aliases.length > 0 && usedAlias != null) {
            String[] newAliases = new String[aliases.length];
            for (int i = 0; i < aliases.length; i++) {
                if (aliases[i].equals(usedAlias)) {
                    newAliases[i] = originalName;
                } else {
                    newAliases[i] = aliases[i];
                }
            }
            return newAliases;
        }
        return aliases;
    }
    protected String formatUsage(IFormatter format, String usage) {
        Matcher matcher = USAGE_PARSE.matcher(usage);
        StringBuffer replace = new StringBuffer();
        while (matcher.find()) {
            if (matcher.group(1) != null) {
                matcher.appendReplacement(replace, format.formatParameter(matcher.group(1)));
            } else if (matcher.group(2) != null) {
                matcher.appendReplacement(replace, format.formatOptionalArgument(matcher.group(2)));
            } else if (matcher.group(3) != null) {
                matcher.appendReplacement(replace, format.formatRequiredArgument(matcher.group(3)));
            }
        }
        matcher.appendTail(replace);
        return replace.toString();
    }
    
    private boolean checkPermisisons(CommandContext context, IAbstractPermission[] perms) {
        if (perms.length == 0)
            return true;
        for (IAbstractPermission perm : perms) {
            if (context.checkPermission(perm))
                return true;
        }
        return false;
    }
}
