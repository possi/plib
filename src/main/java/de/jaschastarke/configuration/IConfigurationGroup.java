package de.jaschastarke.configuration;

import java.util.List;

import org.bukkit.configuration.ConfigurationSection;

public interface IConfigurationGroup {
    public void setValues(ConfigurationSection sect);
    public ConfigurationSection getValues();
    public List<IConfigurationNode> getConfigNodes();
}
