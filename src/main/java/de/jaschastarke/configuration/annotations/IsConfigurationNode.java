package de.jaschastarke.configuration.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import de.jaschastarke.configuration.ConfigurationStyle;

@Retention(RetentionPolicy.RUNTIME) 
@Target(ElementType.METHOD)
/**
 * This Annotation describes that the getter-method it is added tot, is treated as a value-node in the configuration
 * file.
 * 
 * If no nodename is given, the name is derived from the getter-method-name: getEnabled() -> enabled
 */
public @interface IsConfigurationNode {
    /**
     * If no nodename is given, the name is derived from the getter-method-name: getEnabled() -> enabled
     */
    String name() default "";
    int order() default 0;
    boolean readonly() default false;
    ConfigurationStyle style() default ConfigurationStyle.DEFAULT;
}
