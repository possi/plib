package de.jaschastarke.bukkit.lib.commands;

public abstract class AbstractCommand extends AbstractCommandList implements ICommand {
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
}
