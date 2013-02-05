package de.jaschastarke.bukkit.lib.chat;

import de.jaschastarke.LocaleString;

public interface IFormatter {
    public Integer getLineLimit();
    
    public String getString(LocaleString msg);
    public String getString(String msg, Object... params);
    public String getNewLine();
    
    public IPagination newPaginiation();
    
    /**
     * Event if this method doesn't do anything than calling the .format method on the IChatFormatting by default, it
     * can be overridden to change the behavior defined in the Enum (or other enums that may be also passed through).
     */
    public String formatString(IChatFormatting formating, CharSequence msg);
}
