package com.qatoolist.bluejay.listeners.interceptor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The RunIf annotation is used to conditionally execute methods based on a specified
 * environment. Methods annotated with RunIf will only be executed if the current environment
 * matches the value provided in the 'environment' attribute.
 *
 * @Target({ElementType.METHOD}) // Can only be applied to methods
 * @Retention(RetentionPolicy.RUNTIME) // Annotation data available at runtime
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface RunIf {

    /**
     * Specifies the environment in which the annotated method should be executed.
     * Must be a non-empty string.
     *
     * @return The environment name
     */
    String environment();
}
