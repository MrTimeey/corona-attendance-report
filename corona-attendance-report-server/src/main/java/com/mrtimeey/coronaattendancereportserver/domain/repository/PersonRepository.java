package com.mrtimeey.coronaattendancereportserver.domain.repository;

import com.mrtimeey.coronaattendancereportserver.domain.entity.Person;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonRepository extends MongoRepository<Person, String> {

    boolean existsById(String id);

}
