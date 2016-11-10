package com.anthonyfdev.graphQLQueryGen;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Use this to mark Object fields that can be used in a GraphQL query.
 * <p>
 * The name of the marked field will be used directly in the query:
 * for example, the definition
 * <pre>
 *{@literal @}GraphQLObject
 * public class Product {
 *    {@literal @}GraphQLField
 *     public String name;
 *
 *    {@literal @}GraphQLField
 *     public float height;
 *
 *    {@literal @}GraphQLField(type = "isKnown")
 *     public boolean available;
 * }
 * </pre>
 * will be treated as the following piece of GraphQL:
 * <pre>
 * {
 *     name
 *     height
 *     available : isKnown
 * }
 * </pre>
 * </p>
 */
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.FIELD)
public @interface GraphQLField {

    /**
     * NOTE: Will use field name as alias
     * @return Type that is part of GraphQLSchema
     */
    String type() default Processor.UNASSIGNED_VALUE;
}
