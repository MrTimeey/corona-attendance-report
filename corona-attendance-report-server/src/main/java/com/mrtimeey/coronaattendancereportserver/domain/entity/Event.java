package com.mrtimeey.coronaattendancereportserver.domain.entity;

import lombok.Builder;
import lombok.Data;
import lombok.Singular;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.lang.NonNull;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;
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

    private String name;

    private String startTime;

    private String endTime;

    @Builder.Default
    private LocalDate date = LocalDate.now();

    @Builder.Default
    private EventStatus status = EventStatus.CREATED;

    @NotNull
    @Singular
    private List<EventParticipant> participants;

    @Builder.Default
    private LocalDateTime created = LocalDateTime.now();

    private LocalDateTime released;

    private LocalDateTime sent;

}
