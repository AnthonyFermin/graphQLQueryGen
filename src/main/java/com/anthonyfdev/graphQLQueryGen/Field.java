package com.anthonyfdev.graphQLQueryGen;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Denotes a field to be used to generate the desired GraphQL Query
 */

@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.FIELD)
public @interface Field {

    /**
     * Assign this to the type you want returned by your server
     * NOTE: Will use field name as alias
     */
    String aliasType() default Processor.UNASSIGNED_VALUE;

}
