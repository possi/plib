package de.jaschastarke.bukkit.lib.commands;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabExecutor;

import de.jaschastarke.bukkit.lib.Core;
import de.jaschastarke.bukkit.lib.chat.ConsoleFormatter;
import de.jaschastarke.bukkit.lib.chat.IFormatter;
import de.jaschastarke.bukkit.lib.chat.InGameFormatter;

public class BukkitCommandHandler implements TabExecutor, ICommandListing {
    protected Core plugin;
    protected Map<Command, ICommand> commands = new HashMap<Command, ICommand>();
    public BukkitCommandHandler(final Core plugin) {
        this.plugin = plugin;
    }
    
    public void registerCommand(final ICommand cmd) {
        PluginCommand bcmd = plugin.getCommand(cmd.getName());
        if (bcmd == null)
            throw new IllegalArgumentException("Command " + cmd.getName() + " isn't registered for this plugin. Check plugin.yml");
        commands.put(bcmd, cmd);
        if (cmd instanceof BukkitCommand)
            ((BukkitCommand) cmd).setBukkitCommand(bcmd);
        bcmd.setExecutor(this);
    }
    public void removeCommand(final ICommand cmd) {
        PluginCommand bcmd = plugin.getCommand(cmd.getName());
        if (bcmd != null) {
            commands.remove(bcmd);
            bcmd.setExecutor(null);
        }
    }
    public void removeCommand(final String cmd) {
        PluginCommand bcmd = plugin.getCommand(cmd);
        if (bcmd != null) {
            commands.remove(bcmd);
            bcmd.setExecutor(null);
        }
    }
    @Override
    public Collection<ICommand> getCommandList() {
        return commands.values();
    }
    
    @Override
    public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
        ICommand cmd = commands.get(command);
        if (cmd == null)
            throw new IllegalArgumentException("Not to handler registered command fired: " + command.getName());
        CommandContext context = createContext(sender);
        try {
            context.addHandledCommand(cmd, label);
            return cmd.execute(context, args);
        } catch (MissingPermissionCommandException e) {
            sender.sendMessage(ChatColor.RED + e.getMessage());
            return true;
        } catch (CommandException e) {
            sender.sendMessage(ChatColor.RED + e.getMessage());
            return true;
        }
    }

    @Override
    public List<String> onTabComplete(final CommandSender sender, final Command command, final String alias, final String[] args) {
        ICommand cmd = commands.get(command);
        if (cmd == null)
            throw new IllegalArgumentException("Not to handler registered command fired: " + command.getName());
        
        if (cmd instanceof ITabCommand) {
            CommandContext context = new CommandContext(sender);
            context.setPlugin(plugin);
            context.setPermissinManager(plugin.getPermManager());
            
            context.addHandledCommand(cmd);
            return ((ITabCommand) cmd).tabComplete(context, args);
        }
        return null;
    }
    
    public CommandContext createContext(final CommandSender sender) {
        CommandContext context = new CommandContext(sender);
        context.setPlugin(plugin);
        context.setPermissinManager(plugin.getPermManager());
        context.setFormatter(getFormatter(sender));
        
        return context;
    }
    
    private Map<Object, IFormatter> formatter = new HashMap<Object, IFormatter>();
    protected IFormatter getFormatter(final CommandSender sender) {
        if (sender instanceof ConsoleCommandSender) {
            if (!formatter.containsKey(ConsoleCommandSender.class))
                formatter.put(ConsoleCommandSender.class, new ConsoleFormatter(plugin.getLang()));
            return formatter.get(ConsoleCommandSender.class);
        } else {
            if (!formatter.containsKey(null))
                formatter.put(null, new InGameFormatter(plugin.getLang()));
            return formatter.get(null);
        }
    }
}
