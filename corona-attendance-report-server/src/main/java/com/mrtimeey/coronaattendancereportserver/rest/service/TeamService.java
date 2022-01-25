package com.mrtimeey.coronaattendancereportserver.rest.service;

import com.mrtimeey.coronaattendancereportserver.domain.entity.Team;
import com.mrtimeey.coronaattendancereportserver.domain.repository.TeamRepository;
import com.mrtimeey.coronaattendancereportserver.rest.transfer.TeamTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TeamService {

    private final TeamRepository teamRepository;

    public TeamTO createTeam(TeamTO teamTO) {
        Team team = Team.builder()
                .name(teamTO.getName())
                .defaultStartTime(teamTO.getDefaultStartTime())
                .defaultEndTime(teamTO.getDefaultEndTime())
                .mailTargets(teamTO.getMailTargets())
                .members(teamTO.getMembers())
                .build();
        return TeamTO.fromBusinessModel(teamRepository.save(team));
    }

}
