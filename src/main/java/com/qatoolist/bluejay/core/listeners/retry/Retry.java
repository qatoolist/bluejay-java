package com.qatoolist.bluejay.core.listeners.retry;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to define retry logic for TestNG tests.
 * Allows specifying the maximum number of retries and the backoff time between retries.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD) // This annotation is applicable to test methods.
public @interface Retry {

    /**
     * Specifies the maximum number of retry attempts.
     * Default: 2
     */
    int maxRetries() default 2;

    /**
     * Specifies the backoff time (in milliseconds) between retry attempts.
     * Default: 1000 (1 second)
     */
    long backoffTimeMs() default 1000;
}
