package de.jaschastarke;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.IncompleteArgumentException;

public class I18n {
    protected boolean ignorant = false;
    
    protected ResourceBundle bundle;
    public I18n(final String bundleName, final Locale locale) {
        useBundle(bundleName, locale);
    }
    public I18n(final String bundleName) {
        useBundle(bundleName, null);
    }
    public I18n(final Locale locale) {
        useBundle(null, locale);
    }
    public I18n() {
        useBundle(null, null);
    }
    
    protected void useBundle(final String bundleName, final Locale locale) {
        bundle = new MultipleResourceBundle(locale, new String[]{I18n.class.getPackage().getName() + ".bukkit.messages", bundleName});
    }
    
    public ResourceBundle getResourceBundle() {
        return bundle;
    }
    public boolean isIgnorant() {
        return ignorant;
    }
    public void setIgnorant(final boolean value) {
        ignorant = value;
    }
    
    private String translate(final String msg, final Object... objects) {
        try {
            String str = bundle.getString(msg);
            if (objects.length > 0)
                str = MessageFormat.format(str, objects);
            return str;
        } catch (MissingResourceException ex) {
            if (ignorant)
                return msg;
            throw ex;
        }
    }

    public String trans(final CharSequence msg) {
        return msg instanceof LocaleString ? ((LocaleString) msg).translate(this) : translate(msg.toString(), new Object[0]);
    }
    public String trans(final String msg, final Object... objects) {
        return translate(msg, objects);
    }
    
    private static final int RIDX_MOD = 3;
    public static Locale getLocaleFromString(final String locale) {
        Matcher match = Pattern.compile("^([a-z]+)(?:[-_]([A-Za-z]+)(?:[-_](.*))?)?$").matcher(locale);
        if (!match.matches())
            throw new IncompleteArgumentException("Locale-String could not be interpreted: " + locale);
        if (match.group(RIDX_MOD) != null)
            return new Locale(match.group(1), match.group(2), match.group(RIDX_MOD));
        else if (match.group(2) != null)
            return new Locale(match.group(1), match.group(2));
        else
            return new Locale(match.group(1));
    }
}
