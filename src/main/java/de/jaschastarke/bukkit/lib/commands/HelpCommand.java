package de.jaschastarke.bukkit.lib.commands;

import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.ArrayUtils;

import de.jaschastarke.LocaleString;
import de.jaschastarke.bukkit.lib.chat.ChatFormattings;
import de.jaschastarke.bukkit.lib.chat.IFormatter;
import static de.jaschastarke.bukkit.lib.chat.IFormatter.NEWLINE;
import de.jaschastarke.minecraft.lib.permissions.IAbstractPermission;

public class HelpCommand implements ICommand, ICommandListHelp {
    private static final Pattern USAGE_PARSE = Pattern.compile("\\B(?:(\\-\\w)|\\[([\\w\\.\\-]+)\\]|<([\\w+\\.\\-]+)>)\\B");
    private static final int USAGE_IDX_PARAM = 1;
    private static final int USAGE_IDX_OPTIONAL = 2;
    private static final int USAGE_IDX_REQUIRED = 3;
    private static final String SPACE = " ";
    private static final String LIST_SEP = ", ";
    
    private ICommandListing mainCommand;
    public HelpCommand(final ICommandListing parentCommand) {
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
    public boolean execute(final CommandContext context, final String[] args) throws MissingPermissionCommandException, CommandException {
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

    @Deprecated
    public boolean executeCommandList(final CommandContext context) {
        if (mainCommand instanceof IHelpDescribed)
            return displayDescribedHelpCommand(context, (IHelpDescribed) mainCommand);
        IFormatter formatter = context.getFormatter();
        
        StringBuilder text = new StringBuilder(ICommand.PREFIX);
        for (Entry<ICommand, String> handledcommand : context.getCommandChain().entrySet()) {
            text.append(handledcommand.getValue());
            text.append(SPACE);
        }
        text.append("- ");
        text.append(formatter.getString("bukkit.help.available_commands"));
        text.append(NEWLINE);
        for (ICommand command : mainCommand.getCommandList()) {
            //text.append(" - ");
            text.append(command.getName());
            text.append(LIST_SEP);
        }
        text.replace(text.length() - 2, text.length(), "");
        context.response(text.toString().trim());
        return true;
    }
    
    protected boolean displayDescribedHelpCommand(final CommandContext context, final IHelpDescribed command) {
        return displayDescribedHelpCommand(context, command, null);
    }
    protected boolean displayDescribedHelpCommand(final CommandContext context, final IHelpDescribed command, final String usedAlias) {
        String alias = usedAlias;
        IFormatter formatter = context.getFormatter();
        
        StringBuilder desc = new StringBuilder();
        desc.append(formatter.formatString(ChatFormattings.TEXT_HEADER, command.getPackageName()));
        desc.append(NEWLINE);
        
        boolean something = false;
        
        String usage = command.getUsage().toString();
        if (usage != null && !usage.isEmpty()) {
            something = true;
            desc.append(formatter.formatString(ChatFormattings.LABEL, formatter.getString("bukkit.help.syntax")));
        }
        if (context.isPlayer()) {
            desc.append(formatter.formatString(ChatFormattings.SLASH, ICommand.PREFIX));
        }
        for (Map.Entry<ICommand, String> pcommand : context.getCommandChain().entrySet()) {
            if (pcommand.getKey() == command) {
                alias = pcommand.getValue();
                break;
            } else if (pcommand.getKey() == this) {
                break;
            }
            desc.append(formatter.formatString(ChatFormattings.USED_COMMAND, pcommand.getValue()));
            desc.append(SPACE);
        }
        desc.append(formatter.formatString(ChatFormattings.COMMAND, alias != null ? alias : command.getName()));
        desc.append(SPACE);
        if (usage != null && !usage.isEmpty()) {
            String usagestr = formatUsage(formatter, usage);
            if (context.isPlayer())
                usagestr.replace("$player", context.getPlayer().getName());
            desc.append(formatter.formatString(ChatFormattings.ARGUMENTS, usagestr));
        }
        desc.append(NEWLINE);
        
        String[] aliases = buildAliases(command.getAliases(), alias, command.getName());
        if (aliases != null && aliases.length > 0) {
            something = true;
            desc.append(formatter.formatString(ChatFormattings.LABEL, formatter.getString("bukkit.help.aliases")));
            for (int i = 0; i < aliases.length; i++) {
                if (i > 0)
                    desc.append(LIST_SEP);
                desc.append(formatter.formatString(ChatFormattings.COMMAND, aliases[i]));
            }
            desc.append(NEWLINE);
        }
        
        CharSequence description = command.getDescription();
        if (description != null) {
            String d;
            if (description instanceof LocaleString)
                d = formatter.getString(((LocaleString) description).getRawValue(), ((LocaleString) description).getObjects());
            else
                d = description.toString();
            something = true;
            desc.append(formatter.formatString(ChatFormattings.DESCRIPTION, d.trim()));
            desc.append(NEWLINE);
        }
        
        if (command instanceof ICommandListing && ((ICommandListing) command).getCommandList().size() > 0) {
            something = true;
            desc.append(formatter.formatString(ChatFormattings.LABEL, formatter.getString("bukkit.help.available_sub_commands")));
            for (ICommand scommand : mainCommand.getCommandList()) {
                if (!(scommand instanceof IHelpDescribed) || checkPermisisons(context, ((IHelpDescribed) scommand).getRequiredPermissions())) {
                    desc.append(formatter.formatString(ChatFormattings.REQUIRED_ARGUMENT, scommand.getName()));
                    desc.append(LIST_SEP);
                }
            }
            desc.replace(desc.length() - 2, desc.length(), ""); // remove latest LIST_SEP
            desc.append(NEWLINE);
        }
        
        if (something) {
            context.response(desc.toString().trim());
            return true;
        } else {
            return false;
        }
    }
    
    private String[] buildAliases(final String[] aliases, final String usedAlias, final String originalName) {
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
    public static String formatUsage(final IFormatter format, final String usage) {
        Matcher matcher = USAGE_PARSE.matcher(usage);
        StringBuffer replace = new StringBuffer();
        while (matcher.find()) {
            if (matcher.group(USAGE_IDX_PARAM) != null) {
                matcher.appendReplacement(replace, format.formatString(ChatFormattings.PARAMETER, matcher.group(0)));
            } else if (matcher.group(USAGE_IDX_OPTIONAL) != null) {
                matcher.appendReplacement(replace, format.formatString(ChatFormattings.OPTIONAL_ARGUMENT, matcher.group(0)));
            } else if (matcher.group(USAGE_IDX_REQUIRED) != null) {
                matcher.appendReplacement(replace, format.formatString(ChatFormattings.REQUIRED_ARGUMENT, matcher.group(0)));
            }
        }
        matcher.appendTail(replace);
        return replace.toString();
    }
    
    private boolean checkPermisisons(final CommandContext context, final IAbstractPermission[] perms) {
        if (perms.length == 0)
            return true;
        for (IAbstractPermission perm : perms) {
            if (context.checkPermission(perm))
                return true;
        }
        return false;
    }
}
