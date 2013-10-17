package de.jaschastarke.bukkit.lib.commands;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.ArrayUtils;

import de.jaschastarke.LocaleString;
import de.jaschastarke.bukkit.lib.chat.ChatFormattings;
import de.jaschastarke.bukkit.lib.chat.IFormatter;
import de.jaschastarke.bukkit.lib.chat.IPagination;
import de.jaschastarke.bukkit.lib.chat.NoPager;
import de.jaschastarke.minecraft.lib.permissions.IAbstractPermission;
import de.jaschastarke.utils.ArrayUtil;

public class HelpCommand implements ICommand, ICommandListHelp, IHelpDescribed {
    private static final Pattern USAGE_PARSE = Pattern.compile("(\\B\\-\\w(?:\\s\\w+)?\\b|\\B\\-?\\-\\w+\\b)|\\B\\[(.+?)\\]\\B|\\B<(.+?)>\\B|\\B\\|(.+?)\\|\\B");
    private static final int USAGE_IDX_PARAM = 1;
    private static final int USAGE_IDX_OPTIONAL = 2;
    private static final int USAGE_IDX_REQUIRED = 3;
    private static final int USAGE_IDX_STATIC = 4;
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
    public boolean execute(final CommandContext context, final String[] cArgs) throws MissingPermissionCommandException, CommandException {
        String[] args = cArgs;
        IPagination page = context.getFormatter().newPaginiation();
        if (cArgs.length > 0 && cArgs[cArgs.length - 1].equals("-n")) {
            page = new NoPager();
            args = ArrayUtil.getRange(cArgs, 0, -1);
        } else if (cArgs.length > 0) {
            try {
                int p = Integer.parseInt(cArgs[cArgs.length - 1]);
                page.selectPage(p);
                args = ArrayUtil.getRange(cArgs, 0, -1);
            } catch (NumberFormatException e) {
                page.selectPage(0);
            }
        }
        if (args.length > 0) {
            for (ICommand command : mainCommand.getCommandList()) {
                if (command.getName().equals(args[0])) {
                    if (command instanceof IHelpDescribed) {
                        return displayDescribedHelpCommand(page, context, (IHelpDescribed) command);
                    }
                }
            }
            for (ICommand command : mainCommand.getCommandList()) {
                if (ArrayUtils.contains(command.getAliases(), args[0])) {
                    if (command instanceof IHelpDescribed) {
                        return displayDescribedHelpCommand(page, context, (IHelpDescribed) command, args[0]);
                    }
                }
            }
        }
        if (mainCommand instanceof IHelpDescribed)
            return displayDescribedHelpCommand(page, context, (IHelpDescribed) mainCommand);
        
        return executeCommandList(context);
    }
    public boolean executeCommandList(final CommandContext context) {
        return executeCommandList(context.getFormatter().newPaginiation(), context);
    }
    public boolean executeCommandList(final IPagination page, final CommandContext context) {
        if (mainCommand instanceof IHelpDescribed)
            return displayDescribedHelpCommand(page, context, (IHelpDescribed) mainCommand);
        
        if (mainCommand instanceof ICommandListing && ((ICommandListing) mainCommand).getCommandList().size() > 0) {
            IPagination desc = context.getFormatter().newPaginiation();
            for (ICommand scommand : mainCommand.getCommandList()) {
                if (!(scommand instanceof IHelpDescribed) || checkPermisisons(context, ((IHelpDescribed) scommand).getRequiredPermissions())) {
                    if (scommand instanceof IHelpDescribed) {
                        for (CharSequence cusage : ((IHelpDescribed) scommand).getUsages()) {
                            buildUsage(desc, context, scommand, cusage.toString());
                            desc.appendln();
                        }
                    } else {
                        buildUsage(desc, context, scommand, null);
                        desc.appendln();
                    }
                }
            }
            context.response(desc.toString().trim());
            return true;
        }
        return false;
    }
    
    protected boolean displayDescribedHelpCommand(final IPagination desc, final CommandContext context, final IHelpDescribed command) {
        for (Map.Entry<ICommand, String> entry : context.getCommandChain().entrySet()) {
            if (entry.getKey() == command) {
                return displayDescribedHelpCommand(desc, context, command, entry.getValue());
            }
        }
        return displayDescribedHelpCommand(desc, context, command, null);
    }
    protected boolean displayDescribedHelpCommand(final IPagination desc, final CommandContext context, final IHelpDescribed command, final String usedAlias) {
        String alias = usedAlias;
        IFormatter formatter = context.getFormatter();
        
        if (command.getPackageName() != null) {
            String title = (command.getPackageName() + SPACE + desc.getPageDisplay()).trim();
            desc.appendln(formatter.formatString(ChatFormattings.TEXT_HEADER, title));
            desc.setFixedLines(1, 0);
        }
        
        boolean something = false;
        
        if (command.getUsages() != null) {
            for (CharSequence cusage : command.getUsages()) {
                String usage = cusage.toString();
                if (usage != null && !usage.isEmpty()) {
                    something = true;
                    desc.append(formatter.formatString(ChatFormattings.LABEL, formatter.getString("bukkit.help.usage")));
                }
                buildUsage(desc, context, command, usage, usedAlias);
                desc.appendln();
            }
        }
        
        String[] aliases = buildAliases(command.getAliases(), alias, command.getName());
        if (aliases != null && aliases.length > 0) {
            something = true;
            desc.append(formatter.formatString(ChatFormattings.LABEL, formatter.getString("bukkit.help.aliases")));
            for (int i = 0; i < aliases.length; i++) {
                if (i > 0)
                    desc.append(LIST_SEP);
                desc.append(formatter.formatString(ChatFormattings.COMMAND, aliases[i]));
            }
            desc.appendln();
        }
        
        CharSequence description = command.getDescription();
        if (description != null) {
            String d;
            if (description instanceof LocaleString)
                d = formatter.getString(((LocaleString) description).getRawValue(), ((LocaleString) description).getObjects());
            else
                d = description.toString();
            something = true;
            desc.appendln(formatter.formatString(ChatFormattings.DESCRIPTION, d.trim()));
        }
        
        if (command instanceof ICommandListing && ((ICommandListing) command).getCommandList().size() > 0) {
            something = true;
            /*desc.append(formatter.formatString(ChatFormattings.LABEL, formatter.getString("bukkit.help.available_sub_commands")));
            for (ICommand scommand : mainCommand.getCommandList()) {
                if (!(scommand instanceof IHelpDescribed) || checkPermisisons(context, ((IHelpDescribed) scommand).getRequiredPermissions())) {
                    desc.append(formatter.formatString(ChatFormattings.REQUIRED_ARGUMENT, scommand.getName()));
                    desc.append(LIST_SEP);
                }
            }
            desc.replace(desc.length() - 2, desc.length(), ""); // remove latest LIST_SEP
            desc.append(NEWLINE);*/
            
            for (ICommand scommand : mainCommand.getCommandList()) {
                if (!(scommand instanceof IHelpDescribed) || checkPermisisons(context, ((IHelpDescribed) scommand).getRequiredPermissions())) {
                    if (scommand instanceof IHelpDescribed) {
                        if (((IHelpDescribed) scommand).getUsages() != null) {
                            for (CharSequence cusage : ((IHelpDescribed) scommand).getUsages()) {
                                buildUsage(desc, context, scommand, cusage.toString());
                                desc.appendln();
                            }
                        }
                    } else {
                        buildUsage(desc, context, scommand, null);
                        desc.appendln();
                    }
                }
            }
        }
        
        if (command.getPackageName() == null && desc.getPageCount() > 1) {
            desc.appendln(desc.getPageDisplay());
            desc.setFixedLines(0, 1);
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
            } else if (matcher.group(USAGE_IDX_STATIC) != null) {
                matcher.appendReplacement(replace, format.formatString(ChatFormattings.COMMAND, matcher.group(USAGE_IDX_STATIC)));
            }
        }
        matcher.appendTail(replace);
        return replace.toString();
    }
    
    protected void buildUsage(final IPagination desc, final CommandContext context, final ICommand command, final String usage) {
        buildUsage(desc, context, command, usage, null);
    }
    protected void buildUsage(final IPagination desc, final CommandContext context, final ICommand command, final String usage, final String usedAlias) {
        String alias = usedAlias;
        IFormatter formatter = context.getFormatter();
        if (context.isPlayer()) {
            desc.append(formatter.formatString(ChatFormattings.SLASH, ICommand.PREFIX));
        }
        for (Map.Entry<ICommand, String> pcommand : context.getCommandChain().entrySet()) {
            ICommand tcommand = pcommand.getKey();
            if (tcommand instanceof AliasCommand) {
                tcommand = ((AliasCommand<?>) tcommand).getOriginal();
            }
            if (tcommand == command) {
                alias = pcommand.getValue();
                break;
            } else if (tcommand == this) {
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
    
    // IHelpDescribed
    @Override
    public IAbstractPermission[] getRequiredPermissions() {
        return new IAbstractPermission[0];
    }
    @Override
    public CharSequence[] getUsages() {
        return new String[]{"[command] [-n|page]"};
    }
    @Override
    public CharSequence getDescription() {
        return null;
    }
    @Override
    public CharSequence getPackageName() {
        return mainCommand instanceof IHelpDescribed
                ? ((IHelpDescribed) mainCommand).getPackageName()
                : new LocaleString("bukkit.help.title");
    }
}
