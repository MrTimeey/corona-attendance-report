package com.mrtimeey.coronaattendancereportserver.rest.development;

import com.mrtimeey.coronaattendancereportserver.SpringProfiles;
import com.mrtimeey.coronaattendancereportserver.exception.NotImplementedException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile(SpringProfiles.NOT_LOCAL)
@RequiredArgsConstructor
public class ProdDevelopmentService implements DevelopmentService {

    public void deleteAllEvents() {
        throw new NotImplementedException("Delete all events is currently not implemented!");
    }

    public void deleteAllPersons() {
        throw new NotImplementedException("Delete all persons is currently not implemented!");
    }

    public void deleteAllTeams() {
        throw new NotImplementedException("Delete all teams is currently not implemented!");
    }

    public void deleteAll() {
        throw new NotImplementedException("Delete all is currently not implemented!");
    }

}
