package com.graphql.data.repositories;

import com.graphql.data.model.Person;
import com.graphql.data.model.PersonFullNameProjection;
import org.springframework.data.querydsl.ReactiveQuerydslPredicateExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.graphql.data.GraphQlRepository;
import reactor.core.publisher.Mono;

@GraphQlRepository
public interface PersonRepository extends ReactiveCrudRepository<Person, String>,
        ReactiveQuerydslPredicateExecutor<Person> {

    // can be defined to explicitly map in fetcher but with @GraphQlRepository and projection it's redundant
    Mono<PersonFullNameProjection> findByFirstName(String firstName);
}
