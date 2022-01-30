package com.mrtimeey.coronaattendancereportserver.rest.controller;

import com.mrtimeey.coronaattendancereportserver.rest.request.OnCreate;
import com.mrtimeey.coronaattendancereportserver.rest.service.EventService;
import com.mrtimeey.coronaattendancereportserver.rest.transfer.EventTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping(EventController.API_URL)
public class EventController {

    public static final String API_URL = "/events";

    private final EventService eventService;

    @Validated(OnCreate.class)
    @PostMapping
    public ResponseEntity<EventTO> createEvent(@Valid @RequestBody EventTO eventTO) {
        return ResponseEntity.ok(eventService.createEvent(eventTO));
    }
}

