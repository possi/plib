package de.jaschastarke.bukkit.lib.commands;

import java.util.Collection;

public interface ICommandListing {
    public void registerCommand(ICommand command);
    public void removeCommand(ICommand command);
    public Collection<ICommand> getCommandList();
}
