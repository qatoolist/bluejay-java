package com.qatoolist.bluejay.core.utils.providers;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface DataFile {
    /**
     * Specifies the path to the test data file.
     * The path can be relative to the project or an absolute path.
     *
     * @return The file path.
     */
    String value();
}
