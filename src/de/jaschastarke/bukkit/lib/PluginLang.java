package de.jaschastarke.bukkit.lib;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Locale;

import de.jaschastarke.MultipleResourceBundle;
import de.jaschastarke.i18n;

public class PluginLang extends i18n {
    private Core plugin = null;
    public PluginLang(String bundle, Core plugin) {
        super(bundle);
        this.plugin = plugin;
        useBundle(bundle, null);
    }
    
    protected void useBundle(String bundleName, Locale locale) {
        if (plugin == null)
            return;
        URLClassLoader loader = null;
        try {
            URL datadir = plugin.getDataFolder().toURI().toURL();
            loader = new URLClassLoader(new URL[]{datadir}, plugin.getClass().getClassLoader());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        bundle = new MultipleResourceBundle(locale, new String[]{"de.jaschastarke.bukkit.messages", bundleName}, loader);
    }
}
