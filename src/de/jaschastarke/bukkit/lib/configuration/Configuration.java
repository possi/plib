package de.jaschastarke.bukkit.lib.configuration;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.configuration.ConfigurationSection;

import de.jaschastarke.configuration.ConfigurationNode;
import de.jaschastarke.configuration.annotations.IConfigurationGroup;
import de.jaschastarke.configuration.annotations.IConfigurationSubGroup;
import de.jaschastarke.configuration.annotations.IsConfigurationNode;

public abstract class Configuration implements IConfigurationGroup {
    /*@Override
    public String getNodeName() {
        if (this.getClass().getAnnotation(ConfigurationGroup.class) != null) {
            return this.getClass().getAnnotation(ConfigurationGroup.class).value();
        }
        return null;
    }*/
    private List<IConfigurationSubGroup> sections = new ArrayList<IConfigurationSubGroup>();
    private List<ConfigurationNode> nodes = new ArrayList<ConfigurationNode>();
    protected ConfigurationSection config; 
    
    public Configuration() {
        initializeConfigNodes();
    }
    public Configuration(ConfigurationSection sect) {
        initializeConfigNodes();
        setConfigurationValues(sect);
    }
    private void initializeConfigNodes() {
        for (Method method : this.getClass().getMethods()) {
            IsConfigurationNode annot = method.getAnnotation(IsConfigurationNode.class);
            if (annot != null) {
                ConfigurationNode node = new ConfigurationNode(method, annot);
                nodes.add(node);
            }
        }
    }
    
    
    public void setConfigurationValues(ConfigurationSection sect) {
        config = sect;
    }
    public ConfigurationSection getConfigurationValues() {
        return config;
    }
    
    public List<ConfigurationNode> getConfigNodes() {
        return nodes;
    }
    
    public List<IConfigurationSubGroup> getSubGroups() {
        return sections;
    }
    
    public <T extends IConfigurationSubGroup> T registerSection(T section) {
        for (IConfigurationSubGroup cSection : sections) {
            if (cSection.getNodeName() == section.getNodeName()) {
                throw new IllegalAccessError("A configuration group with this NodeName is alread registered: " + section.getNodeName());
            }
        }
        sections.add(section);
        if (section.getConfigurationValues() == null) {
            section.setConfigurationValues(config.getConfigurationSection(section.getNodeName()));
        }
        return section;
    }
    
    /*
    public Configuration(ConfigurationSection sect) {
        config = sect;
    }
    public <T extends IConfigurationGroup> T registerSection(Class<T> sectionType) {
        ConfigurationGroup annot = sectionType.getAnnotation(ConfigurationGroup.class);
        if (annot == null || annot.value() == "")
            throw new RuntimeException("Can not add section to configuration without a NodeName");
        T section;
        try {
            if (config.contains(annot.value())) {
                section = sectionType.getConstructor(ConfigurationSection.class).newInstance(config.getConfigurationSection(annot.value()));
            } else {
                section = sectionType.getConstructor(ConfigurationSection.class).newInstance(config.createSection(annot.value()));
            }
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Failed to initialize ConfigurationGroup", e);
        } catch (SecurityException e) {
            throw new RuntimeException("Failed to initialize ConfigurationGroup", e);
        } catch (InstantiationException e) {
            throw new RuntimeException("Failed to initialize ConfigurationGroup", e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Failed to initialize ConfigurationGroup", e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException("Failed to initialize ConfigurationGroup", e);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("Failed to initialize ConfigurationGroup", e);
        }
        registerSection(section);
        return section;
    }
    
    public IConfigurationGroup registerSection(IConfigurationGroup section) {
        if (section.getNodeName() == null)
            throw new RuntimeException("Can not add section to configuration without a NodeName");
        for (IConfigurationGroup cSection : sections) {
            if (cSection.getNodeName() == section.getNodeName()) {
                throw new RuntimeException("A configuration group with this NodeName is alread registered: " + section.getNodeName());
            }
        }
        sections.add(section);
        return section;
    }
    
    @SuppressWarnings("unchecked")
    public <T extends IConfigurationGroup> T getSection(Class<T> sectionType) {
        for (IConfigurationGroup cSection : sections) {
            if (sectionType.isInstance(cSection)) {
                return (T) cSection;
            }
        }
        return null;
    }*/
}
