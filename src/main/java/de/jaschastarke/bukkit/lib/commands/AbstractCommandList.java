package de.jaschastarke.bukkit.lib.commands;

import java.util.Arrays;
import java.util.List;

public abstract class AbstractCommandList implements ICommandListing {
    protected CommandHandler handler = new CommandHandler();
    
    public AbstractCommandList() {
        handler.registerCommands(Arrays.asList((ICommand[]) MethodCommand.getMethodCommandsFor(this)));
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
