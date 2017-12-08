package com.blackbuild.multicli.base;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Defines an hierarchical command. Points to the given parent command.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface SubCommandOf {
    /**
     * The class of the parent command.
     */
    Class<?> value();
}
