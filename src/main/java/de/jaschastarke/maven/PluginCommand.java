package de.jaschastarke.maven;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Registers the Command-Objects to be included in the plugin.yml
 */
@Retention(RetentionPolicy.SOURCE) 
@Target(ElementType.TYPE)
public @interface PluginCommand {
}
