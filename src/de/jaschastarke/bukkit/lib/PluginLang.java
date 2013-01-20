package de.jaschastarke.bukkit.lib;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Locale;
import java.util.ResourceBundle;

import de.jaschastarke.i18n;

public class PluginLang extends i18n {
    private Core plugin;
    public PluginLang(String bundle, Core plugin) {
        super(false);
        this.plugin = plugin;
        useBundle(bundle, null); // replace bundle set by parent
    }
    
    private void useBundle(String bundleName, Locale locale) {
        URLClassLoader loader = null;
        try {
            URL datadir = plugin.getDataFolder().toURI().toURL();
            loader = new URLClassLoader(new URL[]{datadir}, plugin.getClass().getClassLoader());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        plugin.getLog().debug("Using own classloader for resourcebundle!");
        
        if (locale == null)
            bundle = ResourceBundle.getBundle(bundleName, Locale.getDefault(), loader);
        else
            bundle = ResourceBundle.getBundle(bundleName, locale, loader);
    }
}
