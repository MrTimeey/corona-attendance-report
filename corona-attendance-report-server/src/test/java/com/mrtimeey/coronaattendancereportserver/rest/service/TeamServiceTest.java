package com.mrtimeey.coronaattendancereportserver.rest.service;

import com.mrtimeey.coronaattendancereportserver.domain.entity.Team;
import com.mrtimeey.coronaattendancereportserver.domain.repository.TeamRepository;
import com.mrtimeey.coronaattendancereportserver.exception.ResourceNotFoundException;
import com.mrtimeey.coronaattendancereportserver.rest.transfer.TeamTO;
import com.mrtimeey.coronaattendancereportserver.utils.AbstractIntegrationTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class TeamServiceTest extends AbstractIntegrationTest {

    @Autowired
    private TeamService serviceUnderTest;

    @Autowired
    private TeamRepository teamRepository;

    @MockBean
    private PersonService personService;

    @BeforeEach
    @AfterEach
    void clearRepository() {
        teamRepository.deleteAll();
    }

    @Test
    void createTeam() {
        TeamTO team = TeamTO.builder()
                .name("FC Test Team")
                .build();

        TeamTO serviceResult = serviceUnderTest.createTeam(team);

        TeamTO expectedTeam = TeamTO.builder()
                .id(serviceResult.getId())
                .name("FC Test Team")
                .defaultStartTime("")
                .defaultEndTime("")
                .mailTargets(List.of())
                .build();

        validateTeam(serviceResult, expectedTeam);
    }

    @Test
    void createTeamShouldFail_AlreadyPresentId() {
        TeamTO team = TeamTO.builder()
                .id(UUID.randomUUID().toString())
                .name("FC Fehlschlag")
                .build();

        assertThatExceptionOfType(ConstraintViolationException.class)
                .isThrownBy(() -> serviceUnderTest.createTeam(team))
                .withMessage("createTeam.teamTO.id: must be null");
    }

    @Test
    void updateTeam() {
        TeamTO team = TeamTO.builder()
                .name("FC Test Team")
                .build();

        TeamTO createdTeam = serviceUnderTest.createTeam(team);

        TeamTO changedTeam = createdTeam.toBuilder()
                .name("FC Changed Test Team")
                .defaultStartTime("startTime")
                .defaultEndTime("endTime")
                .build();

        TeamTO serviceResult = serviceUnderTest.updateTeam(changedTeam);

        TeamTO expectedTeam = TeamTO.builder()
                .id(createdTeam.getId())
                .name("FC Changed Test Team")
                .defaultStartTime("startTime")
                .defaultEndTime("endTime")
                .mailTargets(List.of())
                .build();

        validateTeam(serviceResult, expectedTeam);
    }

    @Test
    void updateTeamShouldFail_MissingId() {
        TeamTO team = TeamTO.builder()
                .name("FC Test Team")
                .build();

        assertThatExceptionOfType(ConstraintViolationException.class)
                .isThrownBy(() -> serviceUnderTest.updateTeam(team))
                .withMessage("updateTeam.teamTO.id: must not be null");
    }

    @Test
    void updateTeamShouldFail_UnknownId() {
        String randomId = UUID.randomUUID().toString();
        TeamTO team = TeamTO.builder()
                .id(randomId)
                .name("FC Test Team")
                .build();

        assertThatExceptionOfType(ResourceNotFoundException.class)
                .isThrownBy(() -> serviceUnderTest.updateTeam(team))
                .withMessage(String.format("Team with id '%s' not found!", randomId));
    }

    @Test
    void getTeamById() {
        TeamTO team = TeamTO.builder()
                .name("FC Test Team")
                .build();

        TeamTO createdTeam = serviceUnderTest.createTeam(team);

        Optional<TeamTO> serviceResult = serviceUnderTest.getTeam(createdTeam.getId());

        TeamTO expectedTeam = TeamTO.builder()
                .id(createdTeam.getId())
                .name("FC Test Team")
                .defaultStartTime("")
                .defaultEndTime("")
                .mailTargets(List.of())
                .build();

        assertThat(serviceResult).isPresent();

        validateTeam(serviceResult.get(), expectedTeam);
    }

    @Test
    void getTeamByIdShouldFindNothing() {
        Optional<TeamTO> serviceResult = serviceUnderTest.getTeam(UUID.randomUUID().toString());

        assertThat(serviceResult).isEmpty();
    }

    @Test
    void getTeamShouldFail_NullId() {
        assertThatExceptionOfType(ConstraintViolationException.class)
                .isThrownBy(() -> serviceUnderTest.getTeam(null))
                .withMessage("getTeam.teamId: must not be blank");
    }

    @Test
    void getTeamShouldFail_EmptyId() {
        assertThatExceptionOfType(ConstraintViolationException.class)
                .isThrownBy(() -> serviceUnderTest.getTeam(""))
                .withMessage("getTeam.teamId: must not be blank");
    }

    @Test
    void teamExisting() {
        TeamTO team = TeamTO.builder()
                .name("FC Test Team")
                .build();

        TeamTO createdTeam = serviceUnderTest.createTeam(team);

        boolean result = serviceUnderTest.teamExisting(createdTeam.getId());

        assertThat(result).isTrue();
    }

    @Test
    void teamExistingShouldReturnFalse() {
        boolean result = serviceUnderTest.teamExisting(UUID.randomUUID().toString());

        assertThat(result).isFalse();
    }

    @Test
    void teamExistingShouldReturnFalse_NullId() {
        boolean result = serviceUnderTest.teamExisting(null);

        assertThat(result).isFalse();
    }

    @Test
    void teamExistingShouldReturnFalse_EmptyId() {
        boolean result = serviceUnderTest.teamExisting("");

        assertThat(result).isFalse();
    }

    @Test
    void deleteTeam() {
        TeamTO team = TeamTO.builder()
                .name("FC Test Team")
                .build();

        TeamTO createdTeam = serviceUnderTest.createTeam(team);
        assertThat(teamRepository.existsById(createdTeam.getId())).isTrue();

        serviceUnderTest.deleteTeam(createdTeam.getId());

        assertThat(teamRepository.existsById(createdTeam.getId())).isFalse();
    }

    @Test
    void deleteTeamShouldFail_NullId() {
        assertThatExceptionOfType(ConstraintViolationException.class)
                .isThrownBy(() -> serviceUnderTest.deleteTeam(null))
                .withMessage("deleteTeam.teamId: must not be blank");
    }

    @Test
    void deleteTeamShouldFail_EmptyId() {
        assertThatExceptionOfType(ConstraintViolationException.class)
                .isThrownBy(() -> serviceUnderTest.deleteTeam(""))
                .withMessage("deleteTeam.teamId: must not be blank");
    }

    @Test
    void getTeamList() {
        TeamTO team = TeamTO.builder()
                .name("FC Test Team")
                .build();

        TeamTO firstCreatedTeam = serviceUnderTest.createTeam(team);
        TeamTO secondCreatedTeam = serviceUnderTest.createTeam(team);

        List<TeamTO> teamList = serviceUnderTest.getTeamList();

        assertThat(teamList)
                .hasSize(2)
                .containsExactlyInAnyOrder(firstCreatedTeam, secondCreatedTeam);
    }

    @Test
    void addMemberListShouldFail_TeamNotExisting() {
        String randomId = UUID.randomUUID().toString();

        assertThatExceptionOfType(ResourceNotFoundException.class)
                .isThrownBy(() -> serviceUnderTest.addMemberList(randomId, List.of()))
                .withMessage(String.format("Team with id '%s' not found!", randomId));
    }

    @Test
    void addMemberListShouldFail_NullId() {
        assertThatExceptionOfType(ConstraintViolationException.class)
                .isThrownBy(() -> serviceUnderTest.addMemberList(null, List.of()))
                .withMessage("addMemberList.teamId: must not be blank");
    }

    @Test
    void addMemberListShouldFail_EmptyId() {
        assertThatExceptionOfType(ConstraintViolationException.class)
                .isThrownBy(() -> serviceUnderTest.addMemberList("", List.of()))
                .withMessage("addMemberList.teamId: must not be blank");
    }

    @Test
    void addMemberListShouldFail_NullList() {
        assertThatExceptionOfType(ConstraintViolationException.class)
                .isThrownBy(() -> serviceUnderTest.addMemberList(UUID.randomUUID().toString(), null))
                .withMessage("addMemberList.membersToAdd: must not be null");
    }

    @Test
    void addMemberListWithNonExistingPersonIds() {
        TeamTO team = TeamTO.builder()
                .name("FC Test Team")
                .build();

        TeamTO createdTeam = serviceUnderTest.createTeam(team);

        List<String> membersToAdd = List.of(UUID.randomUUID().toString(), UUID.randomUUID().toString());
        TeamTO serviceResult = serviceUnderTest.addMemberList(createdTeam.getId(), membersToAdd);

        TeamTO expectedTeam = TeamTO.builder()
                .id(createdTeam.getId())
                .name("FC Test Team")
                .defaultStartTime("")
                .defaultEndTime("")
                .mailTargets(List.of())
                .members(Set.of())
                .build();

        validateTeam(serviceResult, expectedTeam);
    }

    @Test
    void addMemberList() {
        TeamTO team = TeamTO.builder()
                .name("FC Test Team")
                .build();

        TeamTO createdTeam = serviceUnderTest.createTeam(team);

        String firstPerson = UUID.randomUUID().toString();
        String secondPerson = UUID.randomUUID().toString();
        List<String> membersToAdd = List.of(firstPerson, secondPerson);
        when(personService.personExists(any(String.class))).thenReturn(true);
        TeamTO serviceResult = serviceUnderTest.addMemberList(createdTeam.getId(), membersToAdd);

        TeamTO expectedTeam = TeamTO.builder()
                .id(createdTeam.getId())
                .name("FC Test Team")
                .defaultStartTime("")
                .defaultEndTime("")
                .mailTargets(List.of())
                .members(Set.of(firstPerson, secondPerson))
                .build();

        validateTeam(serviceResult, expectedTeam);
    }

    private void validateTeam(TeamTO serviceResult, TeamTO expectedTeam) {

        assertThat(serviceResult).isEqualTo(expectedTeam);

        Optional<Team> repositoryResult = teamRepository.findById(expectedTeam.getId());

        assertThat(repositoryResult).isPresent();
        assertThat(repositoryResult.get().getId()).isEqualTo(expectedTeam.getId());
        assertThat(repositoryResult.get().getName()).isEqualTo(expectedTeam.getName());
        assertThat(repositoryResult.get().getDefaultStartTime()).isNotNull().isEqualTo(expectedTeam.getDefaultStartTime());
        assertThat(repositoryResult.get().getDefaultEndTime()).isNotNull().isEqualTo(expectedTeam.getDefaultEndTime());
        assertThat(repositoryResult.get().getMailTargets()).isNotNull().containsExactlyInAnyOrderElementsOf(expectedTeam.getMailTargets());
        assertThat(repositoryResult.get().getMembers()).isNotNull().containsExactlyInAnyOrderElementsOf(expectedTeam.getMembers());
    }

}
