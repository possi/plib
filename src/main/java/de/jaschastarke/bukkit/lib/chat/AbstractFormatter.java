package de.jaschastarke.bukkit.lib.chat;

import org.bukkit.ChatColor;

import de.jaschastarke.LocaleString;
import de.jaschastarke.I18n;

public abstract class AbstractFormatter implements IFormatter {

    protected I18n lang;
    public static final String NEWLINE = "\n";
    public AbstractFormatter(final I18n lang) {
        this.lang = lang;
    }

    @Override
    public Integer getLineLimit() {
        return null;
    }
    @Override
    public String getNewLine() {
        return NEWLINE;
    }

    @Override
    public String getString(final LocaleString msg) {
        if (!msg.isTranslated())
            msg.translate(lang);
        return msg.toString();
    }
    @Override
    public String getString(final String msg, final Object... params) {
        return lang == null ? msg.toString() : lang.trans(msg, params);
    }
    @Override
    public String formatString(final IChatFormatting formating, final CharSequence msg) {
        if (msg instanceof LocaleString && lang != null)
            ((LocaleString) msg).translate(lang);
        return formating.format(msg.toString());
    }
    
    public int countChars(final String str) {
        return removeFormatting(str).length();
    }
    public String removeFormatting(final String str) {
        return ChatColor.stripColor(str);
    }
}
