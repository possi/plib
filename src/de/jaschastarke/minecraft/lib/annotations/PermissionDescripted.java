package de.jaschastarke.minecraft.lib.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.SOURCE) 
@Target(ElementType.TYPE)
@Deprecated
public @interface PermissionDescripted {
    public enum Type {
        SELF,
        STATIC_ATTRIBUTES;
    };
    Type value() default Type.SELF;
}
