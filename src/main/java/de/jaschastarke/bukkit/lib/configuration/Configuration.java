package de.jaschastarke.bukkit.lib.configuration;

import java.util.List;

import org.bukkit.configuration.ConfigurationSection;

import de.jaschastarke.configuration.ConfigurationStyle;
import de.jaschastarke.configuration.IBaseConfigurationNode;
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
    protected DocComment comment = null;
    
    public Configuration(final ConfigurationContainer container) {
        super(container.getDocCommentStorage());
        comment = container.getDocCommentStorage().getClassFor(this).getDocComment();
        config = container.getConfig();
    }
    public Configuration(final ClassDescriptorStorage docCommentStorage) {
        super(docCommentStorage);
        comment = docCommentStorage.getClassFor(this).getDocComment();
    }

    public void setValues(final ConfigurationSection sect) {
        config = sect;
        if (sect != null) {
            for (IBaseConfigurationNode node : nodes) {
                if (node instanceof Configuration) {
                    if (config.isConfigurationSection(node.getName())) {
                        ((Configuration) node).setValues(config.getConfigurationSection(node.getName()));
                    } else {
                        ((Configuration) node).setValues(config.createSection(node.getName()));
                    }
                }
            }
        }
    }
    public ConfigurationSection getValues() {
        return config;
    }
    
    public <T extends IConfigurationSubGroup> T registerSection(final T section) {
        for (IBaseConfigurationNode node : nodes) {
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
    
    protected <T extends Enum<T>> T getEnum(final Class<T> type, final String option) {
        return getEnum(type, option, null);
    }
    protected <T extends Enum<T>> T getEnum(final Class<T> type, final String option, final T defaultValue) {
        if (config.isBoolean(option) && !config.getBoolean(option)) {
            return null;
        } else {
            if (!config.contains(option) || config.get(option) == null)
                return defaultValue;
            try {
                T val = Enum.valueOf(type, config.getString(option).toUpperCase());
                return (val == null) ? defaultValue : val;
            } catch (IllegalArgumentException e) {
                return defaultValue;
            }
        }
    }
    
    @Override
    public List<IBaseConfigurationNode> getConfigNodes() {
        List<IBaseConfigurationNode> rnodes = super.getConfigNodes();
        for (IBaseConfigurationNode node : rnodes) {
            if (node instanceof MethodConfigurationNode) {
                if (((MethodConfigurationNode) node).getStyle() == ConfigurationStyle.HIDDEN) {
                    if (config.contains(node.getName()))
                        ((MethodConfigurationNode) node).setStyle(ConfigurationStyle.DEFAULT);
                }
            }
        }
        return rnodes;
    }
}
