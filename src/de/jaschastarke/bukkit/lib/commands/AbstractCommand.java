package de.jaschastarke.bukkit.lib.commands;

abstract public class AbstractCommand extends AbstractCommandList implements ICommand {
    protected ICommand helpcommand = null;
    
    public AbstractCommand() {
        helpcommand = getDefaultHelpCommand();
        if (helpcommand != null)
            registerCommand(helpcommand);
    }
    protected ICommand getDefaultHelpCommand() {
        return new HelpCommand(this);
    }
    
    @Override
    public String[] getAliases() {
        return null;
    }

    @Override
    public boolean execute(CommandContext context, String[] args) throws MissingPermissionCommandException, CommandException {
        if ((args.length == 0 || args[0].isEmpty()) && helpcommand != null && helpcommand instanceof ICommandListHelp) {
            return ((ICommandListHelp) helpcommand).executeCommandList(context);
        } else if (args.length == 0 || args[0].isEmpty()) {
            return false;
        }
        return handler.execute(context, args);
    }
}
