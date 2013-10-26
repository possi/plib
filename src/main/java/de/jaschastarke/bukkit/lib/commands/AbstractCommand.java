package de.jaschastarke.bukkit.lib.commands;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.ArrayUtils;

import de.jaschastarke.minecraft.lib.permissions.IAbstractPermission;
import de.jaschastarke.utils.ArrayUtil;

public abstract class AbstractCommand extends AbstractCommandList implements ITabCommand {
    protected ICommand helpcommand = null;
    
    public AbstractCommand() {
        helpcommand = getDefaultHelpCommand();
        if (helpcommand != null)
            registerCommand(helpcommand);
    }
    protected HelpCommand getDefaultHelpCommand() {
        return new HelpCommand(this);
    }
    
    @Override
    public String[] getAliases() {
        return null;
    }

    @Override
    public boolean execute(final CommandContext context, final String[] args) throws MissingPermissionCommandException, CommandException {
        if ((args.length == 0 || args[0].isEmpty()) && helpcommand != null && helpcommand instanceof ICommandListHelp) {
            return ((ICommandListHelp) helpcommand).executeCommandList(context);
        } else if (args.length == 0 || args[0].isEmpty()) {
            return false;
        }
        boolean resp = handler.execute(context, args);
        if (!resp && helpcommand != null) {
            return helpcommand.execute(context, args);
        }
        return resp;
    }
    
    @Override
    public List<String> tabComplete(final CommandContext context, final String[] args) {
        if (args.length > 0) {
            String name = args[0];
            String[] newArgs = ArrayUtil.getRange(args, 1);
            for (ICommand command : handler.getCommands()) {
                if (command.getName().equals(name)) {
                    if (command instanceof ITabComplete) {
                        if (!(command instanceof IHelpDescribed) || checkPermisisons(context, ((IHelpDescribed) command).getRequiredPermissions())) {
                            context.addHandledCommand(command);
                            return ((ITabComplete) command).tabComplete(context, newArgs);
                        }
                    } else {
                        return null;
                    }
                }
            }
            for (ICommand command : handler.getCommands()) { // aliases doesn't overwrite command names
                if (ArrayUtils.contains(command.getAliases(), name)) {
                    if (command instanceof ITabComplete) {
                        if (!(command instanceof IHelpDescribed) || checkPermisisons(context, ((IHelpDescribed) command).getRequiredPermissions())) {
                            context.addHandledCommand(command, name);
                            return ((ITabComplete) command).tabComplete(context, newArgs);
                        }
                    } else {
                        return null;
                    }
                }
            }
            
            if (args.length == 1) {
                List<String> hints = new ArrayList<String>();
                for (ICommand cmd : handler.getCommands()) {
                    if (cmd.getName().toLowerCase().startsWith(name.toLowerCase())) {
                        if (!(cmd instanceof IHelpDescribed) || checkPermisisons(context, ((IHelpDescribed) cmd).getRequiredPermissions())) {
                            hints.add(cmd.getName());
                        }
                    }
                }
                return hints;
            }
        }
        return null;
    }

    private static boolean checkPermisisons(final CommandContext context, final IAbstractPermission[] perms) {
        if (perms.length == 0)
            return true;
        for (IAbstractPermission perm : perms) {
            if (context.checkPermission(perm))
                return true;
        }
        return false;
    }
}
