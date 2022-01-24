package com.mrtimeey.coronaattendancereportserver.domain.entity;

import lombok.Builder;
import lombok.Data;
import lombok.Singular;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.lang.NonNull;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@Document
public class Event {

    @Id
    private String id;

    @NotNull
    private String teamId;

    @NotNull
    @Singular
    private List<EventParticipant> participants;
    
}
