package de.jaschastarke.configuration.annotations;

import java.util.List;

import org.bukkit.configuration.ConfigurationSection;

import de.jaschastarke.configuration.ConfigurationNode;

public interface IConfigurationGroup {
    public void setConfigurationValues(ConfigurationSection sect);
    public ConfigurationSection getConfigurationValues();
    public List<ConfigurationNode> getConfigNodes();
    public List<IConfigurationSubGroup> getSubGroups();
}
