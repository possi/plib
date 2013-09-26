package de.jaschastarke.bukkit.lib.commands;

import java.util.LinkedHashMap;
import java.util.Map;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.jaschastarke.bukkit.lib.Core;
import de.jaschastarke.bukkit.lib.chat.IChatFormatting;
import de.jaschastarke.bukkit.lib.chat.IFormatter;
import de.jaschastarke.bukkit.lib.permissions.PermissionManager;
import de.jaschastarke.minecraft.lib.permissions.IAbstractPermission;

public class CommandContext {
    private CommandSender sender;
    private PermissionManager permhandler;
    private Core plugin;
    private IFormatter formatter;
    private Map<ICommand, String> commandchain = new LinkedHashMap<ICommand, String>();
    
    public CommandContext(final CommandSender sender) {
        this.sender = sender;
    }
    public void response(final String msg) {
        sender.sendMessage(msg);
    }
    public void responseFormatted(final IChatFormatting format, final String msg) {
        response(getFormatter().formatString(format, msg));
    }
    public CommandSender getSender() {
        return sender;
    }
    public boolean checkPermission(final IAbstractPermission perm) {
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
    public void setPermissinManager(final PermissionManager pPermhandler) {
        this.permhandler = pPermhandler;
    }
    public Core getPlugin() {
        return plugin;
    }
    public void setPlugin(final Core pPlugin) {
        this.plugin = pPlugin;
    }
    public IFormatter getFormatter() {
        return formatter;
    }
    public void setFormatter(final IFormatter pFormatter) {
        this.formatter = pFormatter;
    }
    public void addHandledCommand(final ICommand command, final String usedAlias) {
        commandchain.put(command, usedAlias);
    }
    public void addHandledCommand(final ICommand command) {
        addHandledCommand(command, command.getName());
    }
    public Map<ICommand, String> getCommandChain() {
        return commandchain;
    }
}
