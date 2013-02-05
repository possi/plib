package de.jaschastarke.bukkit.lib.configuration;

import java.io.File;
import java.io.IOException;

import de.jaschastarke.bukkit.lib.Core;
import de.jaschastarke.configuration.ISaveableConfiguration;

public abstract class PluginConfiguration extends Configuration implements ISaveableConfiguration {
    protected Core plugin;

    protected File file;
    public PluginConfiguration(final Core plugin) {
        super();
        this.plugin = plugin;
        setValues(plugin.getConfig());
        file = new File(plugin.getDataFolder(), "config.yml");
    }
    
    @Override
    public void save() {
        YamlConfigurationDumper dumper = new YamlConfigurationDumper(this);
        try {
            dumper.store(file);
        } catch (IOException e) {
            plugin.getLog().severe("Failed to write Configuration to " + file.getAbsolutePath());
        }
    }
    
    public void saveDefault() {
        if (!file.exists())
            save();
    }
}
