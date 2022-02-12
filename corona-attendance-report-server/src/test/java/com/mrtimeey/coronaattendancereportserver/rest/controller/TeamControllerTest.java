package com.mrtimeey.coronaattendancereportserver.rest.controller;

import com.mrtimeey.coronaattendancereportserver.SpringProfiles;
import com.mrtimeey.coronaattendancereportserver.rest.development.DevelopmentService;
import com.mrtimeey.coronaattendancereportserver.rest.service.TeamService;
import com.mrtimeey.coronaattendancereportserver.rest.transfer.TeamTO;
import com.mrtimeey.coronaattendancereportserver.utils.JsonTestHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.web.util.NestedServletException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TeamController.class)
@ActiveProfiles(SpringProfiles.NOSECURITY)
class TeamControllerTest {

    private final JsonTestHelper jsonTestHelper = new JsonTestHelper();
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private TeamService teamService;
    @MockBean
    private DevelopmentService developmentService;
    private String teamId;

    @BeforeEach
    public void setup() {
        teamId = UUID.randomUUID().toString();
    }

    @Test
    void shouldCreateTeam() throws Exception {
        String teamId = UUID.randomUUID().toString();
        TeamTO team = TeamTO.builder()
                .name("Test-Team")
                .build();
        when(teamService.createTeam(team)).thenReturn(team);

        MockHttpServletRequestBuilder request = post("/teams")
                .content(jsonTestHelper.toJson(team))
                .contentType(MediaType.APPLICATION_JSON_VALUE);

        mockMvc
                .perform(request)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Test-Team"));
    }

    @Test
    void shouldFailCreateTeamBecauseOfExistingTeamId() throws Exception {
        TeamTO team = TeamTO.builder()
                .id(teamId)
                .name("Test-Team")
                .build();

        MockHttpServletRequestBuilder request = post("/teams")
                .content(jsonTestHelper.toJson(team))
                .contentType(MediaType.APPLICATION_JSON_VALUE);


        assertThatExceptionOfType(NestedServletException.class)
                .isThrownBy(() -> mockMvc.perform(request))
                .withMessage("Request processing failed; nested exception is javax.validation.ConstraintViolationException: createTeam.teamTO.id: must be null");
    }

    @Test
    void shouldUpdateTeam() throws Exception {
        TeamTO team = TeamTO.builder()
                .id(teamId)
                .name("Test-Team")
                .defaultStartTime("startTime")
                .build();
        when(teamService.updateTeam(team)).thenReturn(team);

        MockHttpServletRequestBuilder request = patch("/teams")
                .content(jsonTestHelper.toJson(team))
                .contentType(MediaType.APPLICATION_JSON_VALUE);

        mockMvc
                .perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test-Team"))
                .andExpect(jsonPath("$.defaultStartTime").value("startTime"));
    }

    @Test
    void shouldFailUpdateTeamBecauseOfMissingId() throws Exception {
        TeamTO team = TeamTO.builder()
                .name("Test-Team")
                .build();

        MockHttpServletRequestBuilder request = patch("/teams")
                .content(jsonTestHelper.toJson(team))
                .contentType(MediaType.APPLICATION_JSON_VALUE);

        assertThatExceptionOfType(NestedServletException.class)
                .isThrownBy(() -> mockMvc.perform(request))
                .withMessage("Request processing failed; nested exception is javax.validation.ConstraintViolationException: updateTeam.teamTO.id: must not be null");
    }

    @Test
    void shouldGetAllTeams() throws Exception {
        TeamTO firstTeam = TeamTO.builder()
                .id(UUID.randomUUID().toString())
                .name("Test-Team-1")
                .build();
        TeamTO secondTeam = TeamTO.builder()
                .id(UUID.randomUUID().toString())
                .name("Test-Team-2")
                .build();

        when(teamService.getTeamList()).thenReturn(List.of(firstTeam, secondTeam));

        mockMvc
                .perform(get("/teams"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void shouldGetSingleTeam() throws Exception {
        TeamTO team = TeamTO.builder()
                .name("Test-Team")
                .build();
        when(teamService.getTeam(teamId)).thenReturn(Optional.of(team));

        mockMvc
                .perform(get("/teams/{teamId}", teamId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test-Team"));
    }

    @Test
    void shouldAddMemberToTeam() throws Exception {
        Set<String> membersToAdd = Set.of(UUID.randomUUID().toString(), UUID.randomUUID().toString());
        TeamTO team = TeamTO.builder()
                .id(teamId)
                .members(membersToAdd)
                .build();

        when(teamService.addMemberList(teamId, new ArrayList<>(membersToAdd))).thenReturn(team);

        MockHttpServletRequestBuilder request = post("/teams/{teamId}/members", teamId)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(jsonTestHelper.toJson(membersToAdd));

        mockMvc
                .perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.members.length()").value(2));
    }

    @Test
    void shouldNotFindAnyTeam() throws Exception {
        when(teamService.getTeam(any())).thenReturn(Optional.empty());

        mockMvc
                .perform(get("/teamss/{teamId}", teamId))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldDeleteSingleTeam() throws Exception {
        mockMvc
                .perform(delete("/teams/{teamId}", teamId))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("")));

        verify(teamService, times(1)).deleteTeam(teamId);
    }

    @Test
    void shouldDeleteAllTeams() throws Exception {
        mockMvc
                .perform(delete("/teams"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("")));

        verify(developmentService, times(1)).deleteAllTeams();
    }

}
