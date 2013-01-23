package de.jaschastarke.bukkit.lib.chat;

import org.bukkit.OfflinePlayer;

import de.jaschastarke.bukkit.lib.commands.CommandContext;

public class NullFormatter implements IFormatter {
    @Override
    public Integer getLineLimit() {
        return null;
    }

    @Override
    public Integer getLineLengthLimit() {
        return null;
    }

    @Override
    public String getString(String msg, Object... params) {
        return msg;
    }

    @Override
    public String formatPlayerName(OfflinePlayer player) {
        return player.getName();
    }

    @Override
    public String formatPlayerExample(CommandContext context, String defaultName) {
        return defaultName;
    }

    @Override
    public String formatRequiredArgument(String name) {
        return name;
    }

    @Override
    public String formatOptionalArgument(String name) {
        return name;
    }

    @Override
    public String formatParameter(String param) {
        return param;
    }

}
