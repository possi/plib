package de.jaschastarke.bukkit.lib.commands;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.Validate;

import de.jaschastarke.bukkit.lib.Core;
import de.jaschastarke.utils.ArrayUtil;

public class CommandHandler {
    protected Core plugin;
    protected List<ICommand> commands = new ArrayList<ICommand>();

    public List<ICommand> getCommands() {
        return commands;
    }

    public ICommand getCommand(final String name) {
        for (ICommand command : commands) {
            if (command.getName().equals(name))
                return command;
        }
        return null;
    }
    
    /*public void registerCommands(ICommandListing commands) {
        registerCommands(commands.getCommands());
    }*/
    public void registerCommands(final List<ICommand> cmds) {
        commands.addAll(cmds);
    }

    /*public void removeCommands(ICommandListing commands) {
        removeCommands(commands);
    }*/
    public void removeCommands(final List<ICommand> cmds) {
        commands.removeAll(cmds);
    }

    public void registerCommand(final ICommand command) {
        commands.add(command);
    }
    public void removeCommand(final ICommand command) {
        commands.remove(command);
        for (Iterator<ICommand> iterator = commands.iterator(); iterator.hasNext();) {
            ICommand c = iterator.next();
            if (c instanceof AliasCommand<?>) {
                if (((AliasCommand<?>) c).getOriginal().equals(command))
                    iterator.remove();
            }
        }
    }

    public boolean execute(final CommandContext context, final String[] args) throws MissingPermissionCommandException, CommandException {
        Validate.notEmpty(args, "Can not execute no command");
        String name = args[0];
        Validate.notEmpty(name, "Can not execute empty command");
        String[] newArgs = ArrayUtil.getRange(args, 1);
        
        for (ICommand command : commands) {
            if (command.getName().equals(name)) {
                context.addHandledCommand(command);
                return command.execute(context, newArgs);
            }
        }
        for (ICommand command : commands) { // aliases doesn't overwrite command names
            if (ArrayUtils.contains(command.getAliases(), name)) {
                context.addHandledCommand(command, name);
                return command.execute(context, newArgs);
            }
        }
        return false;
    }
}
