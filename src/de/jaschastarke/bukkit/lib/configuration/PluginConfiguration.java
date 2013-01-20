package de.jaschastarke.bukkit.lib.configuration;

import java.io.File;
import java.io.IOException;

import de.jaschastarke.bukkit.lib.Core;

public abstract class PluginConfiguration extends Configuration {
    protected Core plugin;
    protected File file;
    public PluginConfiguration(Core plugin) {
        super(plugin.getConfig());
        this.plugin = plugin;
        file = new File(plugin.getDataFolder(), "config.yml");
    }
    
    public void save() {
        ConfigurationDumper dumper = new ConfigurationDumper(this);
        try {
            dumper.store(file);
        } catch (IOException e) {
            plugin.getLog().severe("Failed to write Configuration to "+file.getAbsolutePath());
        }
    }
    
    public void saveDefault() {
        if (!file.exists())
            save();
    }
}
