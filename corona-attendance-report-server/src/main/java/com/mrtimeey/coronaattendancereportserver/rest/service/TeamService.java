package com.mrtimeey.coronaattendancereportserver.rest.service;

import com.mrtimeey.coronaattendancereportserver.domain.entity.Team;
import com.mrtimeey.coronaattendancereportserver.domain.repository.TeamRepository;
import com.mrtimeey.coronaattendancereportserver.exception.ResourceNotFoundException;
import com.mrtimeey.coronaattendancereportserver.rest.transfer.TeamTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class TeamService {

    private final TeamRepository teamRepository;
    private final PersonService personService;

    public TeamTO createTeam(TeamTO teamTO) {
        Team team = Team.fromTransferObject(teamTO);
        return TeamTO.fromBusinessModel(teamRepository.save(team));
    }

    public TeamTO updateTeam(TeamTO teamTO) {
        if (!teamExisting(teamTO.getId())) {
            throw new ResourceNotFoundException(String.format("Team with id '%s' not found!", teamTO.getId()));
        }
        Team team = Team.fromTransferObject(teamTO);
        return TeamTO.fromBusinessModel(teamRepository.save(team));
    }

    public Optional<TeamTO> getTeam(String teamId) {
        return teamRepository.findById(teamId)
                .map(TeamTO::fromBusinessModel);
    }

    public TeamTO addMemberList(String teamId, List<String> membersToAdd) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Team with id '%s' not found!", teamId)));
        List<String> existingNewMember = membersToAdd.stream()
                .filter(personService::personExists)
                .collect(Collectors.toList());
        team.getMembers().addAll(existingNewMember);
        return TeamTO.fromBusinessModel(teamRepository.save(team));
    }

    public List<TeamTO> getTeamList() {
        return teamRepository.findAll().stream()
                .map(TeamTO::fromBusinessModel)
                .collect(Collectors.toList());
    }

    public void deleteTeam(String teamId) {
        teamRepository.findById(teamId)
                .ifPresent(teamRepository::delete);
    }

    public boolean teamExisting(String teamId) {
        return teamRepository.existsById(teamId);
    }
}
