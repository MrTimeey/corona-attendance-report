package com.mrtimeey.coronaattendancereportserver.domain.entity;

import com.mrtimeey.coronaattendancereportserver.rest.transfer.PersonTO;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;

import javax.validation.constraints.NotBlank;

@Data
@Builder
public class EventParticipant {

    @Id
    private String id;

    @NotBlank
    private String name;

    @Builder.Default
    private String address = "";

    @Builder.Default
    private String phoneNumber = "";

    public static EventParticipant fromTransferObject(PersonTO personTO) {
        return EventParticipant.builder()
                .name(personTO.getName())
                .address(personTO.getAddress())
                .phoneNumber(personTO.getPhoneNumber())
                .build();
    }
}
