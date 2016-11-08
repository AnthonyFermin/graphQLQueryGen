package com.anthonyfdev.graphQLQueryGen.model;

import com.anthonyfdev.graphQLQueryGen.GraphQLField;
import com.anthonyfdev.graphQLQueryGen.GraphQLObject;

/**
 * Created by afermin on 11/7/16.
 */

@GraphQLObject
public class AllFilms {

    @GraphQLField
    int totalCount;

}
