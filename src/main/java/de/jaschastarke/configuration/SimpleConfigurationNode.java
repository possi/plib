package de.jaschastarke.configuration;

public class SimpleConfigurationNode implements IConfigurationNode {
    private String name;
    private int order = 0;
    
    public SimpleConfigurationNode(final String name) {
        this.name = name;
    }
    public SimpleConfigurationNode(final String name, final int order) {
        this.name = name;
        this.order = order;
    }
    
    @Override
    public int getOrder() {
        return order;
    }
    @Override
    public String getName() {
        return name;
    }
    @Override
    public String getDescription() {
        return null;
    }
    @Override
    public boolean isReadOnly() {
        return false;
    }
    @Override
    public Class<String> getType() {
        return String.class;
    }
    @Override
    public ConfigurationStyle getStyle() {
        return ConfigurationStyle.DEFAULT;
    }
}
