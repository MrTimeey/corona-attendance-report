package com.mrtimeey.coronaattendancereportserver.rest.controller;

import com.mrtimeey.coronaattendancereportserver.rest.development.DevelopmentService;
import com.mrtimeey.coronaattendancereportserver.rest.request.OnCreate;
import com.mrtimeey.coronaattendancereportserver.rest.request.OnUpdate;
import com.mrtimeey.coronaattendancereportserver.rest.service.EventService;
import com.mrtimeey.coronaattendancereportserver.rest.transfer.EventTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
    private final DevelopmentService developmentService;

    @Validated(OnCreate.class)
    @PostMapping
    public ResponseEntity<EventTO> createEvent(@Valid @RequestBody EventTO eventTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(eventService.createEvent(eventTO));
    }

    @Validated(OnUpdate.class)
    @PatchMapping
    public ResponseEntity<EventTO> updateEvent(@Valid @RequestBody EventTO eventTO) {
        return ResponseEntity.ok(eventService.updateEvent(eventTO));
    }

    @GetMapping(value = "/{eventId}")
    public ResponseEntity<EventTO> getEvent(@PathVariable String eventId) {
        return eventService.getEvent(eventId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PostMapping(value = "/{eventId}/print")
    public ResponseEntity<EventTO> printEvent(@PathVariable String eventId) {
        eventService.printEvent(eventId);
        return ResponseEntity.ok(null);
    }

    @DeleteMapping(value = "/{eventId}")
    public ResponseEntity<Void> deleteEvent(@PathVariable String eventId) {
        eventService.deleteEvent(eventId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteAllEvents() {
        developmentService.deleteAllEvents();
        return ResponseEntity.ok().build();
    }

}

