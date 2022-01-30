package com.mrtimeey.coronaattendancereportserver.domain.repository;

import com.mrtimeey.coronaattendancereportserver.domain.entity.Event;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventRepository extends MongoRepository<Event, String> {

    boolean existsById(String id);

}
