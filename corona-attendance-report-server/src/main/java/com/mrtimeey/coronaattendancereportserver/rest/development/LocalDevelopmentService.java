package com.mrtimeey.coronaattendancereportserver.rest.development;

import com.mrtimeey.coronaattendancereportserver.SpringProfiles;
import com.mrtimeey.coronaattendancereportserver.domain.repository.EventRepository;
import com.mrtimeey.coronaattendancereportserver.domain.repository.PersonRepository;
import com.mrtimeey.coronaattendancereportserver.domain.repository.TeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile(SpringProfiles.LOCAL)
@RequiredArgsConstructor
public class LocalDevelopmentService implements DevelopmentService {

    private final TeamRepository teamRepository;
    private final PersonRepository personRepository;
    private final EventRepository eventRepository;

    public void deleteAllEvents() {
        eventRepository.deleteAll();
    }

    public void deleteAllPersons() {
        personRepository.deleteAll();
    }

    public void deleteAllTeams() {
        teamRepository.deleteAll();
    }

    public void deleteAll() {
        deleteAllPersons();
        deleteAllTeams();
        deleteAllEvents();
    }

}
