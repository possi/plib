package de.jaschastarke.minecraft.lib.permissions;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface IsChildPermission {
    /**
     * a child node of true inherits the parent permission
     * a child node of false inherits the inverse parent permission
     */
    boolean value() default true;
}
