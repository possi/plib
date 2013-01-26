package de.jaschastarke.bukkit.lib.chat;

import de.jaschastarke.LocaleString;

public interface IFormatter {
    public static final String NEWLINE = "\n";
    
    public Integer getLineLimit();
    public Integer getLineLengthLimit();
    
    public String getString(LocaleString msg);
    public String getString(String msg, Object... params);
    /**
     * Event if this method doesn't do anything than calling the .format method on the IChatFormatting by default, it
     * can be overridden to change the behavior defined in the Enum (or other enums that may be also passed through).
     */
    public String formatString(IChatFormatting formating, CharSequence msg);
}
