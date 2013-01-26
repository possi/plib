package de.jaschastarke.bukkit.lib.chat;

import de.jaschastarke.I18n;

public class InGameFormatter extends AbstractFormatter {
    public static final int CHAT_HEIGHT = 10;
    public static final int CHAT_WIDTH = 55;
    
    public InGameFormatter(final I18n lang) {
        super(lang);
    }

    @Override
    public Integer getLineLimit() {
        return CHAT_HEIGHT;
    }

    @Override
    public Integer getLineLengthLimit() {
        return CHAT_WIDTH;
    }

}
