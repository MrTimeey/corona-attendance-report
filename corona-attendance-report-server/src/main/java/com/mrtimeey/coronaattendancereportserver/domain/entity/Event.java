package com.mrtimeey.coronaattendancereportserver.domain.entity;

import com.mrtimeey.coronaattendancereportserver.rest.transfer.EventTO;
import lombok.Builder;
import lombok.Data;
import lombok.Singular;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;
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

    @NotNull
    private LocalDate date;

    @Builder.Default
    private EventStatus status = EventStatus.CREATED;

    @NotNull
    @Singular
    private List<EventParticipant> participants;

    @Builder.Default
    private LocalDateTime created = LocalDateTime.now();

    private LocalDateTime released;

    private LocalDateTime sent;

    public static Event fromTransferObject(EventTO eventTO) {
        return Event.builder()
                .id(eventTO.getId())
                .teamId(eventTO.getTeamId())
                .name(eventTO.getName())
                .startTime(eventTO.getStartTime())
                .endTime(eventTO.getEndTime())
                .date(eventTO.getDate())
                .status(eventTO.getStatus())
                .participants(eventTO.getParticipants())
                .created(eventTO.getCreated())
                .released(eventTO.getReleased())
                .sent(eventTO.getSent())
                .build();
    }
}
