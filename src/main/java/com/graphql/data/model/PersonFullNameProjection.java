package com.graphql.data.model;

import org.springframework.beans.factory.annotation.Value;

public interface PersonFullNameProjection {
    String getFirstName();

    String getLastName();

    // Either SpEL or default interface (more preferable way imho)
    @Value("#{target.firstName + ' ' + target.lastName}")
    String getFullName();

   /* default String getFullName() {
        return getFirstName() + ' ' + getLastName();
    }*/


}
