package com.anthonyfdev.graphQLQueryGen.model;

import com.anthonyfdev.graphQLQueryGen.GraphQLField;
import com.anthonyfdev.graphQLQueryGen.GraphQLObject;
import com.anthonyfdev.graphQLQueryGen.model.AllFilms;
import com.anthonyfdev.graphQLQueryGen.model.AllPeople;

/**
 * Created by afermin on 11/7/16.
 */

@GraphQLObject
public class TestModel {

    @GraphQLField
    AllFilms allFilms;

    @GraphQLField
    AllPeople allPeople;

}
