package com.mrtimeey.coronaattendancereportserver.rest.transfer;

import com.mrtimeey.coronaattendancereportserver.domain.entity.Person;
import com.mrtimeey.coronaattendancereportserver.rest.request.OnCreate;
import com.mrtimeey.coronaattendancereportserver.rest.request.OnUpdate;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

@Data
@Builder
public class PersonTO {

    @NotNull(groups = OnUpdate.class)
    @Null(groups = OnCreate.class)
    private String id;

    @NotBlank
    private String name;

    @Builder.Default
    private String address = "";

    @Builder.Default
    private String phoneNumber = "";

    public static PersonTO fromBusinessModel(Person person) {
        return PersonTO.builder()
                .id(person.getId())
                .name(person.getName())
                .address(person.getAddress())
                .phoneNumber(person.getPhoneNumber())
                .build();
    }

}
