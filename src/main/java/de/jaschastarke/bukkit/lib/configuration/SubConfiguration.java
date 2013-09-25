package de.jaschastarke.bukkit.lib.configuration;

import de.jaschastarke.configuration.IConfigurationSubGroup;
import de.jaschastarke.utils.ClassDescriptorStorage;

public abstract class SubConfiguration extends Configuration implements IConfigurationSubGroup {
    public SubConfiguration(final ClassDescriptorStorage docCommentStorage) {
        super(docCommentStorage);
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
