package de.jaschastarke.bukkit.lib.chat;

import de.jaschastarke.I18n;

public class InGameFormatter extends AbstractFormatter {
    public static final int CHAT_HEIGHT = 10;
    
    public InGameFormatter(final I18n lang) {
        super(lang);
    }

    @Override
    public Integer getLineLimit() {
        return CHAT_HEIGHT;
    }

    @Override
    public IPagination newPaginiation() {
        return new InGamePagination(this);
    }
}
