package de.jaschastarke.bukkit.lib.chat;

import de.jaschastarke.I18n;

public class ConsoleFormatter extends AbstractFormatter {
    public ConsoleFormatter(final I18n lang) {
        super(lang);
    }

    @Override
    public IPagination newPaginiation() {
        return new NoPager();
    }
}
