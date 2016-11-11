package com.anthonyfdev.graphQLQueryGen;

import java.lang.annotation.*;

/**
 * Only to be used within {@link FieldArguments} annotations.
 * <p>
 * An annotation containing a mandatory param-value pair to be used as an argument(s) for the annotated field in the GraphQLQuery.
 * This is an optional annotation for adding arguments to a specific field.
 * <p>
 * NOTE: Depending on your server's GraphQL schema, some fields are required to have arguments
 **/

@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.ANNOTATION_TYPE)
public @interface ArgMapping {

    String param();

    String value();

    /**
     * @return true if value must be wrapped in quotes
     */
    boolean isString() default false;

}
