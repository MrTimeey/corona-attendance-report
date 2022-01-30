package com.mrtimeey.coronaattendancereportserver.rest.controller;

import com.mrtimeey.coronaattendancereportserver.rest.development.DevelopmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping(AdminController.API_URL)
public class AdminController {

    public static final String API_URL = "/admin";

    private final DevelopmentService developmentService;

    @DeleteMapping(value = "/deleteAll")
    public ResponseEntity<Void> deleteEverything() {
        developmentService.deleteAll();
        return ResponseEntity.ok().build();
    }

}

