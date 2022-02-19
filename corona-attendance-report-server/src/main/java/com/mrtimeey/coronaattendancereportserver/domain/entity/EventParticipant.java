package com.mrtimeey.coronaattendancereportserver.domain.entity;

import com.mrtimeey.coronaattendancereportserver.rest.transfer.PersonTO;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.UUID;

@Data
@Builder
public class EventParticipant {

    @Builder.Default
    private String id = UUID.randomUUID().toString();

    @NotBlank
    private String name;

    @Builder.Default
    private String address = "";

    @Builder.Default
    private String phoneNumber = "";

    @Builder.Default
    private boolean wasPresent = false;

    public static EventParticipant fromTransferObject(PersonTO personTO) {
        return EventParticipant.builder()
                .name(personTO.getName())
                .address(personTO.getAddress())
                .phoneNumber(personTO.getPhoneNumber())
                .build();
    }
}
