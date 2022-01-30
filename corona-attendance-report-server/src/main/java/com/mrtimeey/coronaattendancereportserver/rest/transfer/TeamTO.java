package com.mrtimeey.coronaattendancereportserver.rest.transfer;

import com.mrtimeey.coronaattendancereportserver.domain.entity.Team;
import com.mrtimeey.coronaattendancereportserver.rest.request.OnCreate;
import com.mrtimeey.coronaattendancereportserver.rest.request.OnUpdate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TeamTO {

    @NotNull(groups = OnUpdate.class)
    @Null(groups = OnCreate.class)
    private String id;

    @NotBlank
    private String name;

    private String defaultStartTime = "";

    private String defaultEndTime = "";

    @Builder.Default
    private List<String> mailTargets = new ArrayList<>();

    @Builder.Default
    private List<String> members = new ArrayList<>();

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
