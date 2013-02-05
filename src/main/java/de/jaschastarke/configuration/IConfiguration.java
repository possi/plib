package de.jaschastarke.configuration;

import java.util.List;

public interface IConfiguration {
    /*public void setValues(ConfigurationSection sect);
    public ConfigurationSection getValues();*/
    public List<IBaseConfigurationNode> getConfigNodes();
    public Object getValue(IConfigurationNode node);
}
