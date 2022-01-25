package com.mrtimeey.coronaattendancereportserver.rest.controller;

import com.mrtimeey.coronaattendancereportserver.rest.request.OnCreate;
import com.mrtimeey.coronaattendancereportserver.rest.service.TeamService;
import com.mrtimeey.coronaattendancereportserver.rest.transfer.TeamTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(TeamController.API_URL)
public class TeamController {

    public static final String API_URL = "/teams";
    private final TeamService teamService;

    @Validated(OnCreate.class)
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<TeamTO> createTeam(@RequestBody @Valid TeamTO teamTO) {
        return ResponseEntity.ok(teamService.createTeam(teamTO));
    }

}
