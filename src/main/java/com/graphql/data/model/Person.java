package com.graphql.data.model;

import com.querydsl.core.annotations.QueryEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@QueryEntity
@Document
@Data
@AllArgsConstructor
public class Person {
    @Id private String id;
    private String firstName;
    private String lastName;
}
