package de.jaschastarke.configuration;

import java.lang.reflect.Method;

import org.apache.commons.lang.WordUtils;

import de.jaschastarke.configuration.annotations.IsConfigurationNode;
import de.jaschastarke.utils.DocComment;

public class MethodConfigurationNode implements IConfigurationNode {
    private String name;
    private Method method;
    private IsConfigurationNode annot;
    private DocComment description;
    
    public MethodConfigurationNode(final Method method) {
        this.method = method;
        setNameFromAnnotation(method.getAnnotation(IsConfigurationNode.class));
    }
    public MethodConfigurationNode(final Method method, final IsConfigurationNode annot) {
        this.method = method;
        setNameFromAnnotation(annot);
    }
    public void setDescription(final DocComment desc) {
        this.description = desc;
    }
    
    private static final String METHOD_PREFIX = "get";
    private void setNameFromAnnotation(final IsConfigurationNode pAnnot) {
        if (pAnnot != null)
            this.annot = pAnnot;
        if (pAnnot == null || pAnnot.name().isEmpty()) {
            String mname = this.method.getName();
            if (mname.startsWith(METHOD_PREFIX)) {
                this.name = WordUtils.uncapitalize(mname.substring(METHOD_PREFIX.length()));
            } else {
                throw new IllegalArgumentException("Can't extract name non-getter-method");
            }
        } else {
            this.name = pAnnot.name();
        }
    }
    public Method getMethod() {
        return this.method;
    }
    public IsConfigurationNode getAnnotation() {
        return annot;
    }
    
    @Override
    public int getOrder() {
        return annot != null ? annot.order() : 0;
    }
    @Override
    public String getName() {
        return name;
    }
    @Override
    public String getDescription() {
        return description != null ? description.getDescription() : null;
    }
}
