package de.jaschastarke.bukkit.lib.configuration;

import java.io.File;
import java.io.IOException;

import de.jaschastarke.bukkit.lib.Core;
import de.jaschastarke.configuration.ISaveableConfiguration;

public abstract class PluginConfiguration extends Configuration implements ISaveableConfiguration {
    protected Core plugin;

    protected File file;
    
    public PluginConfiguration(final ConfigurationContainer container) {
        super(container);
    }
    public PluginConfiguration(final Core plugin) {
        super(plugin);
        file = new File(plugin.getDataFolder(), "config.yml");
        this.plugin = plugin;
        setValues(plugin.getConfig());
    }
    
    public void reload() {
        this.plugin.reloadConfig();
        setValues(plugin.getConfig());
    }
    
    @Override
    public void save() {
        YamlConfigurationDumper dumper = new YamlConfigurationDumper(this);
        try {
            file.getParentFile().mkdirs();
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
