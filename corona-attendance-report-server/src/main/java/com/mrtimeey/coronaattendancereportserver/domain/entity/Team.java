package com.mrtimeey.coronaattendancereportserver.domain.entity;

import lombok.Builder;
import lombok.Data;
import lombok.Singular;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
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

}
