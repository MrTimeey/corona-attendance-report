package com.mrtimeey.coronaattendancereportserver.domain.entity;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder
@Document
public class Person {

    @Id
    private String id;

    @NotBlank
    private String name;

    private String address = "";

    private String phoneNumber;

}
