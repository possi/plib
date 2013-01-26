package de.jaschastarke.bukkit.lib.configuration;

import de.jaschastarke.configuration.IConfigurationSubGroup;

public abstract class SubConfiguration extends Configuration implements IConfigurationSubGroup {
    @Override
    public int getOrder() {
        return 0;
    }
}
