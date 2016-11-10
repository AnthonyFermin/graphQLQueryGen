package com.anthonyfdev.graphQLQueryGen;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * A separate annotation to take in field arguments
 */

@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.FIELD)
public @interface FieldArguments {

    ArgMapping[] mappings();

}
