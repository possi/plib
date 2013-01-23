package de.jaschastarke.bukkit.lib.configuration;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.bukkit.configuration.ConfigurationSection;

import de.jaschastarke.configuration.ElementConfigurationNode;
import de.jaschastarke.configuration.IConfigurationGroup;
import de.jaschastarke.configuration.IConfigurationNode;
import de.jaschastarke.configuration.IConfigurationSubGroup;
import de.jaschastarke.configuration.annotations.IsConfigurationNode;

public abstract class Configuration implements IConfigurationGroup {
    /*@Override
    public String getNodeName() {
        if (this.getClass().getAnnotation(ConfigurationGroup.class) != null) {
            return this.getClass().getAnnotation(ConfigurationGroup.class).value();
        }
        return null;
    }*/
    private List<IConfigurationNode> nodes = new ArrayList<IConfigurationNode>();
    protected ConfigurationSection config;
    
    private static final Comparator<IConfigurationNode> SORTER = new Comparator<IConfigurationNode>() {
        @Override
        public int compare(IConfigurationNode arg0, IConfigurationNode arg1) {
            return new Integer(arg0.getOrder()).compareTo(new Integer(arg1.getOrder()));
        }
    };
    
    public Configuration() {
        initializeConfigNodes();
    }
    public Configuration(ConfigurationSection sect) {
        initializeConfigNodes();
        setValues(sect);
    }
    private void initializeConfigNodes() {
        for (Method method : this.getClass().getMethods()) {
            IsConfigurationNode annot = method.getAnnotation(IsConfigurationNode.class);
            if (annot != null) {
                ElementConfigurationNode node = new ElementConfigurationNode(method, annot);
                nodes.add(node);
            }
        }
        this.sort();
    }
    public void sort() {
        if (nodes.size() > 1) {
            Collections.sort(nodes, SORTER);
        }
    }
    
    
    public void setValues(ConfigurationSection sect) {
        config = sect;
    }
    public ConfigurationSection getValues() {
        return config;
    }
    
    public List<IConfigurationNode> getConfigNodes() {
        return nodes;
    }
    
    public <T extends IConfigurationSubGroup> T registerSection(T section) {
        for (IConfigurationNode node : nodes) {
            if (node.getName() == section.getName()) {
                throw new IllegalAccessError("A configuration node with this name is alread registered: " + section.getName());
            }
        }
        nodes.add(section);
        if (section.getValues() == null) {
            if (config.isConfigurationSection(section.getName()))
                section.setValues(config.getConfigurationSection(section.getName()));
            else
                section.setValues(config.createSection(section.getName())); // TODO: overthink, maybe no setvalues then?
        }
        this.sort();
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
