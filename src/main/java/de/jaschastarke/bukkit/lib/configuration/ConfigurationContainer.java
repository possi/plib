package de.jaschastarke.bukkit.lib.configuration;

import org.bukkit.configuration.ConfigurationSection;

import de.jaschastarke.utils.ClassDescriptorStorage;

public interface ConfigurationContainer {
    public ConfigurationSection getConfig();
    public ClassDescriptorStorage getDocCommentStorage();
}
