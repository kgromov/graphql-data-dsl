package com.graphql.data;

import com.graphql.data.model.Person;
import com.graphql.data.model.PersonFullNameProjection;
import com.graphql.data.model.PersonProjection;
import com.graphql.data.model.QPerson;
import com.graphql.data.repositories.PersonRepository;
import graphql.schema.DataFetcher;
import graphql.schema.idl.RuntimeWiring;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;
import org.springframework.graphql.data.query.QuerydslDataFetcher;
import org.springframework.graphql.execution.RuntimeWiringConfigurer;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@EnableReactiveMongoRepositories
@SpringBootApplication
public class GraphqlDataDslApplication {

    public static void main(String[] args) {
        SpringApplication.run(GraphqlDataDslApplication.class, args);
    }

    // equivalent to put @GraphQlRepository over QuerydslPredicateExecutor or ReactiveQuerydslPredicateExecutor repository
    @Bean
    public RuntimeWiringConfigurer runtimeWiringConfigurer(PersonRepository personRepository) {
        return builder -> {
            val singlePersonFetcher = QuerydslDataFetcher.builder(personRepository).single();
            DataFetcher<Flux<PersonFullNameProjection>> manyPersonsFetcher = QuerydslDataFetcher.builder(personRepository)
                    .projectAs(PersonFullNameProjection.class)
                    .many();
            builder.type("Query", wiring -> wiring
                    .dataFetcher("person", singlePersonFetcher)
                    // traditional approach can be used as well
                   /* .dataFetcher("personByFirstName", env -> {
                        String firstName = env.getArgument("firstName");
                        return personRepository.findByFirstName(firstName);
                    })*/
                    .dataFetcher("persons", manyPersonsFetcher)
            );
        };
    }

    @Bean
    ApplicationRunner applicationRunner(PersonRepository personRepository) {
        return args -> {
            Flux<Person> findAll = personRepository.findAll();
            Flux<Person> findByPredicate = personRepository.findAll(QPerson.person.firstName.startsWithIgnoreCase("Ko"));
            Flux<Person> findByLastNameContainsPredicate = personRepository.findAll(QPerson.person.lastName.contains("ro"));

            personRepository.deleteAll()
                    .thenMany(
                            Flux.just("Konstantin Gromov", "Ihor Bystrov", "Serhii Muslanov", "Viacheslav Cheban", "Eugene Turovskii")
                                    .map(name -> new Person(null, name.split("\\s")[0], name.split("\\s")[1]))
                                    .flatMap(personRepository::save)
                    )
                    .thenMany(findByLastNameContainsPredicate)
                    .subscribe(person -> log.info("saved {}", person));
        };
    }

}
