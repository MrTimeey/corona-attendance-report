package com.mrtimeey.coronaattendancereportserver.rest.service;

import com.mrtimeey.coronaattendancereportserver.domain.entity.Team;
import com.mrtimeey.coronaattendancereportserver.domain.repository.TeamRepository;
import com.mrtimeey.coronaattendancereportserver.rest.transfer.TeamTO;
import com.mrtimeey.coronaattendancereportserver.utils.AbstractIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class TeamServiceTest extends AbstractIntegrationTest {

    @Autowired
    private TeamService serviceUnderTest;

    @Autowired
    private TeamRepository teamRepository;

    @Test
    void testCreateTeam() {
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

        assertThat(serviceResult).isEqualTo(expectedTeam);

        Optional<Team> repositoryResult = teamRepository.findById(serviceResult.getId());

        assertThat(repositoryResult).isPresent();
        assertThat(repositoryResult.get().getId()).isEqualTo(expectedTeam.getId());
        assertThat(repositoryResult.get().getName()).isEqualTo(expectedTeam.getName());
        assertThat(repositoryResult.get().getDefaultStartTime()).isNotNull().isEqualTo(expectedTeam.getDefaultStartTime());
        assertThat(repositoryResult.get().getDefaultEndTime()).isNotNull().isEqualTo(expectedTeam.getDefaultEndTime());
        assertThat(repositoryResult.get().getMailTargets()).isNotNull().isEqualTo(expectedTeam.getMailTargets());
    }

}
