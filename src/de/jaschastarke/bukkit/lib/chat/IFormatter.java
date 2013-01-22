package de.jaschastarke.bukkit.lib.chat;

import org.bukkit.OfflinePlayer;

import de.jaschastarke.bukkit.lib.commands.CommandContext;

public interface IFormatter {
    public Integer getLineLimit();
    public Integer getLineLengthLimit();
    
    public String getString(String msg, Object... params);
    public String formatPlayerName(OfflinePlayer player);
    public String formatPlayerExample(CommandContext context, String defaultName);
    public String formatRequiredArgument(String name);
    public String formatOptionalArgument(String name);
    public String formatParameter(String param);
}
