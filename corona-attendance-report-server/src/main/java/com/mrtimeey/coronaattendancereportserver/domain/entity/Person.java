package com.mrtimeey.coronaattendancereportserver.domain.entity;

import com.mrtimeey.coronaattendancereportserver.rest.transfer.PersonTO;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotBlank;

@Data
@Builder
@Document
public class Person {

    @Id
    private String id;

    @NotBlank
    private String name;

    @Builder.Default
    private String address = "";

    @Builder.Default
    private String phoneNumber = "";

    public static Person fromTransferObject(PersonTO personTO) {
        return Person.builder()
                .id(personTO.getId())
                .name(personTO.getName())
                .address(personTO.getAddress())
                .phoneNumber(personTO.getPhoneNumber())
                .build();
    }

}
