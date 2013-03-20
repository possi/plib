package de.jaschastarke.bukkit.lib.commands.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Requires the class using this annotation to implement {@see IMethodCommandContainer}
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface NeedsPermission {
    String[] value();
    boolean optional() default false;
}
