package de.jaschastarke.bukkit.lib.configuration;

import org.bukkit.configuration.ConfigurationSection;

import de.jaschastarke.configuration.IChangeableConfiguration;
import de.jaschastarke.configuration.IConfigurationNode;
import de.jaschastarke.configuration.IConfigurationSubGroup;
import de.jaschastarke.configuration.InvalidValueException;
import de.jaschastarke.configuration.MethodConfiguration;
import de.jaschastarke.configuration.MethodConfigurationNode;
import de.jaschastarke.utils.ClassDescriptorStorage;
import de.jaschastarke.utils.DocComment;

/**
 * An abstract Configuration that combines a {@see MethodConfiguration} with Bukkits ConfigurationSection
 */
public abstract class Configuration extends MethodConfiguration implements IChangeableConfiguration {
    protected ConfigurationSection config;
    protected DocComment comment;
    
    public Configuration() {
        super();
        comment = ClassDescriptorStorage.getInstance().getClassFor(this).getDocComment();
    }

    public void setValues(final ConfigurationSection sect) {
        config = sect;
    }
    public ConfigurationSection getValues() {
        return config;
    }
    
    @Override
    public void setValue(final IConfigurationNode node, final Object pValue) throws InvalidValueException {
        Object value = pValue;
        if (node.isReadOnly())
            throw new ReadOnlyException();
        if (node instanceof MethodConfigurationNode && value instanceof String) {
            String val = (String) value;
            MethodConfigurationNode mnode = (MethodConfigurationNode) node;
            Class<?> type = mnode.getMethod().getReturnType();
            if (Boolean.TYPE.isAssignableFrom(type)) {
                if (val.equals("true") || val.equals("on") || val.equals("1")) {
                    value = true;
                } else if (val.equals("false") || val.equals("off") || val.equals("0")) {
                    value = false;
                } else {
                    throw new InvalidValueException("Not a boolean: " + val);
                }
            } else {
                try {
                    if (Integer.TYPE.isAssignableFrom(type)) {
                        value = Integer.parseInt(val);
                    } else if (Float.TYPE.isAssignableFrom(type)) {
                        value = Float.parseFloat(val);
                    } else if (Double.TYPE.isAssignableFrom(type)) {
                        value = Double.parseDouble(val);
                    } else if (Long.TYPE.isAssignableFrom(type)) {
                        value = Long.parseLong(val);
                    } else if (Short.TYPE.isAssignableFrom(type)) {
                        value = Short.parseShort(val);
                    } else if (Byte.TYPE.isAssignableFrom(type)) {
                        value = Byte.parseByte(val);
                    }
                } catch (NumberFormatException e) {
                    throw new InvalidValueException(e);
                }
            }
        }
        config.set(node.getName(), value);
    }

    public String getDescription() {
        return comment != null ? comment.getDescription() : null;
    }
    
    public <T extends IConfigurationSubGroup> T registerSection(final T section) {
        for (IConfigurationNode node : nodes) {
            if (node.getName() == section.getName()) {
                throw new IllegalAccessError("A configuration node with this name is alread registered: " + section.getName());
            }
        }
        nodes.add(section);
        if (section instanceof Configuration && config != null) {
            if (((Configuration) section).getValues() == null) {
                if (config.isConfigurationSection(section.getName()))
                    ((Configuration) section).setValues(config.getConfigurationSection(section.getName()));
                else
                    ((Configuration) section).setValues(config.createSection(section.getName()));
            }
        }
        this.sort();
        return section;
    }
}
