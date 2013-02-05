package de.jaschastarke.bukkit.lib.chat;

import de.jaschastarke.LocaleString;

public class NullFormatter implements IFormatter {
    @Override
    public Integer getLineLimit() {
        return null;
    }
    @Override
    public String getNewLine() {
        return AbstractFormatter.NEWLINE;
    }
    @Override
    public IPagination newPaginiation() {
        return new NoPager();
    }


    @Override
    public String getString(final LocaleString msg) {
        return msg.toString();
    }
    @Override
    public String getString(final String msg, final Object... params) {
        return msg;
    }

    @Override
    public String formatString(final IChatFormatting formating, final CharSequence msg) {
        return msg.toString();
    }
}
