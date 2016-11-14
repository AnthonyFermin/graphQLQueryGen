package com.anthonyfdev.graphQLQueryGen;

import java.lang.annotation.*;

/**
 * Only to be used within {@link GraphQLField} annotations.
 * <p>
 * An annotation containing a mandatory param-value pair to be used as an argument(s) for the annotated field in the GraphQLQuery.
 * This is an optional annotation for adding arguments to a specific field. The value can be of String (see {@link #isString()}),
 * enum, int, or float types.
 * <p>
 * NOTE: Depending on your server's GraphQL schema, some fields are required to have arguments.
 * Also, enums defined on your server do not need to be wrapped in quotes so {@link #isString()} should remain false
 **/

@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.ANNOTATION_TYPE)
public @interface ArgMapping {

    /**
     * Key or Parameter value of a field argument
     */
    String param();

    /**
     * Value of a field argument
     */
    String value();

    /**
     * Set to true if value must be wrapped in quotes. Default is false.
     */
    boolean isString() default false;

}
