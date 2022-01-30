package com.mrtimeey.coronaattendancereportserver.domain.entity;

import com.mrtimeey.coronaattendancereportserver.rest.transfer.TeamTO;
import lombok.Builder;
import lombok.Data;
import lombok.Singular;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@Document
public class Team {

    @Id
    private String id;

    @NotBlank
    private String name;

    @NotNull
    private String defaultStartTime = "";

    @NotNull
    private String defaultEndTime = "";

    @NotNull
    @Singular
    private List<String> mailTargets;

    @NotNull
    @Singular
    private List<String> members;

    public static Team fromTransferObject(TeamTO teamTO) {
        return Team.builder()
                .id(teamTO.getId())
                .name(teamTO.getName())
                .defaultStartTime(teamTO.getDefaultStartTime() == null ? "" : teamTO.getDefaultStartTime())
                .defaultEndTime(teamTO.getDefaultEndTime() == null ? "" : teamTO.getDefaultEndTime())
                .mailTargets(teamTO.getMailTargets() == null ? new ArrayList<>() : teamTO.getMailTargets())
                .members(teamTO.getMembers() == null ? new ArrayList<>() : teamTO.getMembers())
                .build();
    }

}
