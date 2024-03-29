package com.mrtimeey.coronaattendancereportserver.rest.controller;

import com.mrtimeey.coronaattendancereportserver.rest.development.DevelopmentService;
import com.mrtimeey.coronaattendancereportserver.rest.request.OnCreate;
import com.mrtimeey.coronaattendancereportserver.rest.request.OnUpdate;
import com.mrtimeey.coronaattendancereportserver.rest.service.TeamService;
import com.mrtimeey.coronaattendancereportserver.rest.transfer.TeamTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(TeamController.API_URL)
public class TeamController {

    public static final String API_URL = "/teams";
    private final TeamService teamService;
    private final DevelopmentService developmentService;

    @Validated(OnCreate.class)
    @PostMapping
    public ResponseEntity<TeamTO> createTeam(@RequestBody @Valid TeamTO teamTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(teamService.createTeam(teamTO));
    }

    @Validated(OnUpdate.class)
    @PatchMapping
    public ResponseEntity<TeamTO> updateTeam(@RequestBody @Valid TeamTO teamTO) {
        return ResponseEntity.ok(teamService.updateTeam(teamTO));
    }

    @GetMapping(value = "/{teamId}")
    public ResponseEntity<TeamTO> getTeam(@PathVariable String teamId) {
        return teamService.getTeam(teamId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @Validated
    @PostMapping(value = "/{teamId}/members")
    public ResponseEntity<TeamTO> addMember(@PathVariable String teamId, @RequestBody @NotEmpty List<String> membersToAdd) {
        return ResponseEntity.ok(teamService.addMemberList(teamId, membersToAdd));
    }

    @GetMapping
    public ResponseEntity<List<TeamTO>> getTeams() {
        return ResponseEntity.ok(teamService.getTeamList());
    }

    @DeleteMapping(value = "/{teamId}")
    public ResponseEntity<TeamTO> deleteTeam(@PathVariable String teamId) {
        teamService.deleteTeam(teamId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteAllTeams() {
        developmentService.deleteAllTeams();
        return ResponseEntity.ok().build();
    }
}
