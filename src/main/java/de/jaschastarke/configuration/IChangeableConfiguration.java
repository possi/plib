package de.jaschastarke.configuration;

public interface IChangeableConfiguration extends IConfiguration {
    public void setValue(IConfigurationNode node, Object value) throws InvalidValueException;
}
