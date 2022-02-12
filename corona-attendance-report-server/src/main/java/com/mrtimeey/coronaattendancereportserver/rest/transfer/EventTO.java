package com.mrtimeey.coronaattendancereportserver.rest.transfer;

import com.mrtimeey.coronaattendancereportserver.domain.entity.Event;
import com.mrtimeey.coronaattendancereportserver.domain.entity.EventParticipant;
import com.mrtimeey.coronaattendancereportserver.domain.entity.EventStatus;
import com.mrtimeey.coronaattendancereportserver.rest.request.OnCreate;
import com.mrtimeey.coronaattendancereportserver.rest.request.OnUpdate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Singular;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EventTO {

    @NotNull(groups = OnUpdate.class)
    @Null(groups = OnCreate.class)
    private String id;

    @NotNull
    private String teamId;

    @Builder.Default
    private String name = "";

    @Builder.Default
    private String startTime = "";

    @Builder.Default
    private String endTime = "";

    @Builder.Default
    private LocalDate date = LocalDate.now();

    @Builder.Default
    private EventStatus status = EventStatus.CREATED;

    @Singular
    private List<EventParticipant> participants = new ArrayList<>();

    @Builder.Default
    private LocalDateTime created = LocalDateTime.now();

    private LocalDateTime released;

    private LocalDateTime sent;

    public static EventTO fromBusinessModel(Event event) {
        return EventTO.builder()
                .id(event.getId())
                .teamId(event.getTeamId())
                .name(event.getName())
                .startTime(event.getStartTime())
                .endTime(event.getEndTime())
                .date(event.getDate())
                .status(event.getStatus())
                .participants(event.getParticipants())
                .created(event.getCreated())
                .released(event.getReleased())
                .sent(event.getSent())
                .build();
    }

}
