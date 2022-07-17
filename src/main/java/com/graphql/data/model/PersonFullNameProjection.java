package com.graphql.data.model;

public interface PersonFullNameProjection {
    String getFirstName();

    String getLastName();

    default String getFullName() {
        return getFirstName() + ' ' + getLastName();
    }
}
