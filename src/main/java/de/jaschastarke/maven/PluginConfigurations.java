package de.jaschastarke.maven;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import de.jaschastarke.bukkit.lib.configuration.Configuration;

/**
 * Registers the Configuration-Objects to be used for example config.yml generation
 */
@Retention(RetentionPolicy.SOURCE) 
@Target(ElementType.TYPE)
public @interface PluginConfigurations {
    Class<? extends Configuration> parent() default Configuration.class;
}
