package de.jaschastarke.configuration;

import java.lang.reflect.Method;

import org.apache.commons.lang.WordUtils;

import de.jaschastarke.configuration.annotations.IsConfigurationNode;

public class ElementConfigurationNode implements IConfigurationNode {
    private String name;
    private Method method;
    private IsConfigurationNode annot;
    
    public ElementConfigurationNode(String name) {
        this.name = name;
    }
    public ElementConfigurationNode(Method method) {
        this.method = method;
        setNameFromAnnotation(method.getAnnotation(IsConfigurationNode.class));
    }
    public ElementConfigurationNode(Method method, IsConfigurationNode annot) {
        this.method = method;
        setNameFromAnnotation(annot);
    }
    private void setNameFromAnnotation(IsConfigurationNode annot) {
        if (annot != null)
            this.annot = annot;
        if (annot == null || annot.name().isEmpty()) {
            String name = this.method.getName();
            if (name.startsWith("get")) {
                this.name = WordUtils.uncapitalize(name.substring(3));
            } else {
                throw new IllegalArgumentException("Can't extract name non-getter-method");
            }
        } else {
            this.name = annot.name();
        }
    }
    public Method getMethod() {
        return this.method;
    }
    public IsConfigurationNode getAnnotation() {
        return annot;
    }
    public int getOrder() {
        return annot != null ? annot.order() : 0;
    }
    
    public String getName() {
        return name;
    }
}
