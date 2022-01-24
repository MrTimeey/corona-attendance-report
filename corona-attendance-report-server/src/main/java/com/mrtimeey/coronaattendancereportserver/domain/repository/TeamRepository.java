package com.mrtimeey.coronaattendancereportserver.domain.repository;

import com.mrtimeey.coronaattendancereportserver.domain.entity.Team;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeamRepository extends MongoRepository<Team, String> {

}
