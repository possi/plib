package de.jaschastarke.maven;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Registers the Permission-Objects to be included in the plugin.yml
 */
@Retention(RetentionPolicy.SOURCE)
public @interface PluginPermissions {
}
