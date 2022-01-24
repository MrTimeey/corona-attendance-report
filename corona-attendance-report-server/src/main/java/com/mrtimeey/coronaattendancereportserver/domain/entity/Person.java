package com.mrtimeey.coronaattendancereportserver.domain.entity;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@Document
public class Person {

    @Id
    private String id;

    
}
