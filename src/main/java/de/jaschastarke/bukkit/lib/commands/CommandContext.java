package de.jaschastarke.bukkit.lib.commands;

import java.util.LinkedHashMap;
import java.util.Map;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.jaschastarke.bukkit.lib.Core;
import de.jaschastarke.bukkit.lib.chat.IFormatter;
import de.jaschastarke.bukkit.lib.permissions.PermissionManager;
import de.jaschastarke.minecraft.lib.permissions.IAbstractPermission;

public class CommandContext {
    private CommandSender sender;
    private PermissionManager permhandler;
    private Core plugin;
    private IFormatter formatter;
    private Map<ICommand, String> commandchain = new LinkedHashMap<ICommand, String>();
    
    public CommandContext(CommandSender sender) {
        this.sender = sender;
    }
    public void response(String msg) {
        // TODO: IMPORTANT: MAKE BETTER HANDLED
        sender.sendMessage(msg);
    }
    public CommandSender getSender() {
        return sender;
    }
    public boolean checkPermission(IAbstractPermission perm) {
        return getPermissionManager().hasPermission(sender, perm);
    }
    public boolean isPlayer() {
        return sender instanceof Player;
    }
    public Player getPlayer() {
        return isPlayer() ? (Player) sender : null;
    }
    public PermissionManager getPermissionManager() {
        return permhandler;
    }
    public void setPermissinManager(PermissionManager permhandler) {
        this.permhandler = permhandler;
    }
    public Core getPlugin() {
        return plugin;
    }
    public void setPlugin(Core plugin) {
        this.plugin = plugin;
    }
    public IFormatter getFormatter() {
        return formatter;
    }
    public void setFormatter(IFormatter formatter) {
        this.formatter = formatter;
    }
    public void addHandledCommand(ICommand command, String usedAlias) {
        commandchain.put(command, usedAlias);
    }
    public void addHandledCommand(ICommand command) {
        addHandledCommand(command, command.getName());
    }
    public Map<ICommand, String> getCommandChain() {
        return commandchain;
    }
}
