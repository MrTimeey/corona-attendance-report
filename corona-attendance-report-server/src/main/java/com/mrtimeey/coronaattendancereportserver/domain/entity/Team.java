package com.mrtimeey.coronaattendancereportserver.domain.entity;

import lombok.Builder;
import lombok.Data;
import lombok.Singular;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@Builder
@Document
public class Team {

    @Id
    private String id;

    @NotNull
    @Singular
    private List<String> members;


}
