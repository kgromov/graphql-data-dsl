package com.graphql.data.repositories;

import com.graphql.data.model.Person;
import org.springframework.data.querydsl.ReactiveQuerydslPredicateExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.graphql.data.GraphQlRepository;

@GraphQlRepository
public interface PersonRepository extends ReactiveCrudRepository<Person, String>,
        ReactiveQuerydslPredicateExecutor<Person> {
}
