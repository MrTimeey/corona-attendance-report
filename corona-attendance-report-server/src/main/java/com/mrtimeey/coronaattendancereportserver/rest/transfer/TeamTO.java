package com.mrtimeey.coronaattendancereportserver.rest.transfer;

import com.mrtimeey.coronaattendancereportserver.domain.entity.Team;
import com.mrtimeey.coronaattendancereportserver.rest.request.OnCreate;
import com.mrtimeey.coronaattendancereportserver.rest.request.OnUpdate;
import lombok.Builder;
import lombok.Data;
import lombok.Singular;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.util.List;

@Data
@Builder
public class TeamTO {

    @NotNull(groups = OnUpdate.class)
    @Null(groups = OnCreate.class)
    private String id;

    @NotBlank
    private String name;

    private String defaultStartTime = "";

    private String defaultEndTime = "";

    @Singular
    private List<String> mailTargets;

    @Singular
    private List<String> members;

    public static TeamTO fromBusinessModel(Team team) {
        return TeamTO.builder()
                .id(team.getId())
                .name(team.getName())
                .defaultStartTime(team.getDefaultStartTime())
                .defaultEndTime(team.getDefaultEndTime())
                .mailTargets(team.getMailTargets())
                .members(team.getMembers())
                .build();
    }

}
