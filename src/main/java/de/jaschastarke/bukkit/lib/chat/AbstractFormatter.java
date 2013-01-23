package de.jaschastarke.bukkit.lib.chat;

import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;

import de.jaschastarke.i18n;
import de.jaschastarke.bukkit.lib.commands.CommandContext;

public abstract class AbstractFormatter implements IFormatter {
    //private static Pattern CODES = Pattern.compile("§[0-9a-fklmnor]", Pattern.CASE_INSENSITIVE);
    protected i18n lang;
    public AbstractFormatter(i18n lang) {
        this.lang = lang;
    }

    @Override
    public Integer getLineLimit() {
        return null;
    }

    @Override
    public String getString(String msg, Object... params) {
        return lang == null ? msg : lang.trans(msg, params);
    }

    @Override
    public String formatPlayerName(OfflinePlayer player) {
        return formatPlayerName(player.getName());
    }
    @Override
    public String formatPlayerExample(CommandContext context, String defaultName) {
        return formatPlayerName(context.isPlayer() ? context.getPlayer().getName() : defaultName);
    }
    public String formatPlayerName(String player) {
        return ChatColor.BLUE + player + ChatColor.RESET;
    }

    @Override
    public String formatRequiredArgument(String name) {
        return new StringBuilder().append(ChatColor.DARK_RED).append("<").append(name).append(">").append(ChatColor.RESET).toString();
    }

    @Override
    public String formatOptionalArgument(String name) {
        return new StringBuilder().append(ChatColor.GRAY).append("[").append(name).append("]").append(ChatColor.RESET).toString(); 
    }

    @Override
    public String formatParameter(String param) {
        return new StringBuilder().append(ChatColor.DARK_GRAY).append(param).append(ChatColor.RESET).toString();
    }

    @Override
    public Integer getLineLengthLimit() {
        return null;
    }
    
    public int countChars(String str) {
        return removeFormatting(str).length();
    }
    public String removeFormatting(String str) {
        return ChatColor.stripColor(str);
    }
}
