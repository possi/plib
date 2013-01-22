package de.jaschastarke.bukkit.lib.commands;

import java.util.List;

public abstract class AbstractCommandList implements ICommandListing {
    protected CommandHandler handler = new CommandHandler();
    
    public AbstractCommandList() {
        List<ICommand> commands = MethodCommand.getMethodCommandsFor(this);
        handler.registerCommands(commands);
    }
    
    public void registerCommand(ICommand command) {
        handler.registerCommand(command);
    }
    public void removeCommand(ICommand command) {
        handler.removeCommand(command);
    }
    public List<ICommand> getCommandList() {
        return handler.getCommands();
    }
    public CommandHandler getHandler() {
        return handler;
    }
}
