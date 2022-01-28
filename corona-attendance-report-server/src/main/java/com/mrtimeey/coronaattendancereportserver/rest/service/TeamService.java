package com.mrtimeey.coronaattendancereportserver.rest.service;

import com.mrtimeey.coronaattendancereportserver.domain.entity.Team;
import com.mrtimeey.coronaattendancereportserver.domain.repository.TeamRepository;
import com.mrtimeey.coronaattendancereportserver.rest.transfer.TeamTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class TeamService {

    private final TeamRepository teamRepository;

    public TeamTO createTeam(TeamTO teamTO) {
        Team team = Team.builder()
                .name(teamTO.getName())
                .defaultStartTime(teamTO.getDefaultStartTime() == null ? "" : teamTO.getDefaultStartTime())
                .defaultEndTime(teamTO.getDefaultEndTime() == null ? "" : teamTO.getDefaultEndTime())
                .mailTargets(teamTO.getMailTargets() == null ? new ArrayList<>() : teamTO.getMailTargets())
                .members(teamTO.getMembers() == null ? new ArrayList<>() : teamTO.getMembers())
                .build();
        return TeamTO.fromBusinessModel(teamRepository.save(team));
    }

    public Optional<TeamTO> getTeam(String teamId) {
        return teamRepository.findById(teamId)
                .map(TeamTO::fromBusinessModel);
    }

    public List<TeamTO> getTeamList() {
        return teamRepository.findAll().stream()
                .map(TeamTO::fromBusinessModel)
                .collect(Collectors.toList());
    }
}
