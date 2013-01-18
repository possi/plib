package de.jaschastarke.configuration;

import java.lang.reflect.Method;

import org.apache.commons.lang.WordUtils;

import de.jaschastarke.configuration.annotations.IsConfigurationNode;

public class ConfigurationNode {
    private String name;
    private Method method;
    
    public ConfigurationNode(String name) {
        this.name = name;
    }
    public ConfigurationNode(Method method) {
        this.method = method;
        setNameFromAnnotation(method.getAnnotation(IsConfigurationNode.class));
    }
    public ConfigurationNode(Method method, IsConfigurationNode annot) {
        this.method = method;
        setNameFromAnnotation(annot);
    }
    private void setNameFromAnnotation(IsConfigurationNode annot) {
        if (annot == null || annot.value().isEmpty()) {
            String name = this.method.getName();
            if (name.startsWith("get")) {
                this.name = WordUtils.uncapitalize(name.substring(3));
            } else {
                throw new IllegalArgumentException("Can't extract name non-getter-method");
            }
        } else {
            this.name = annot.value();
        }
    }
    public Method getMethod() {
        return this.method;
    }
    
    public String getName() {
        return name;
    }
}
