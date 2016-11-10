package com.anthonyfdev.graphQLQueryGen;

/**
 * Only to be used within {@link FieldArguments} annotations.
 *
 * An annotation containing a mandatory param-value pair to be used as an argument(s) for the annotated field in the GraphQLQuery.
 * This is an optional annotation for adding arguments to a specific field.
 *
 * NOTE: Depending on your server's GraphQL schema, some fields are required to have arguments
 */
public @interface ArgMapping {

    String param();

    String value();

}
