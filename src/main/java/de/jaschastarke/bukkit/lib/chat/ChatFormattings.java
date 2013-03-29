package de.jaschastarke.bukkit.lib.chat;

import java.text.MessageFormat;

import org.bukkit.ChatColor;

public enum ChatFormattings implements IChatFormatting {
    TEXT_HEADER(ChatColor.AQUA + "=== {0} ===" + ChatColor.RESET),
    SLASH(ChatColor.DARK_GRAY),
    LABEL(ChatColor.BLUE),
    COMMAND(ChatColor.GREEN),
    USED_COMMAND(ChatColor.DARK_AQUA),
    ARGUMENTS(ChatColor.DARK_GREEN),
    REQUIRED_ARGUMENT(ChatColor.DARK_RED),
    OPTIONAL_ARGUMENT(ChatColor.GRAY),
    PARAMETER(ChatColor.DARK_GRAY),
    DESCRIPTION(ChatColor.GOLD),
    PLAYER_NAME(ChatColor.BLUE),
    SUCCESS(ChatColor.GREEN),
    ERROR(ChatColor.RED),
    INFO(ChatColor.YELLOW);
    
    private String display;
    private ChatColor color;
    private ChatFormattings(final String display) {
        this.display = display;
    }
    private ChatFormattings(final ChatColor color) {
        this.color = color;
    }

    @Override
    public String format(final String string) {
        if (display != null)
            return MessageFormat.format(display, string);
        else
            return color + string.replace(ChatColor.RESET.toString(), ChatColor.RESET.toString() + color.toString()) + ChatColor.RESET;
    }
    
}
