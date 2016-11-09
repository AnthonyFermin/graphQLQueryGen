package com.anthonyfdev.graphQLQueryGen;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.FIELD)
public @interface GraphQLField {

    /**
     * NOTE: Will use field name as alias
     * @return Type that is part of GraphQLSchema
     */
    String type() default Processor.UNASSIGNED_VALUE;
}
