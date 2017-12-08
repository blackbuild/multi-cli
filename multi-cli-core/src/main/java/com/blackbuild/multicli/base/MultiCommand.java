package com.blackbuild.multicli.base;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Defines an hierarchical command. Must be placed on an Instance of @{@link MultiCommandLevel}.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface MultiCommand {
    Class<? extends MultiCommandLevel> value() default MultiCommandLevel.class;
}
