package de.jaschastarke.bukkit.lib.chat;

import de.jaschastarke.i18n;

public class InGameFormatter extends AbstractFormatter {
    public InGameFormatter(i18n lang) {
        super(lang);
    }

    @Override
    public Integer getLineLimit() {
        return 10;
    }

    @Override
    public Integer getLineLengthLimit() {
        return 55;
    }

}
