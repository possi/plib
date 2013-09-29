package de.jaschastarke.configuration;

public interface IConfigurationNode extends IBaseConfigurationNode {
    public boolean isReadOnly();
    public Class<?> getType();
    public ConfigurationStyle getStyle();
}
