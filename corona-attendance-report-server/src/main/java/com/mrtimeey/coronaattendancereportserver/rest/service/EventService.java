package com.mrtimeey.coronaattendancereportserver.rest.service;

import com.mrtimeey.coronaattendancereportserver.domain.entity.Event;
import com.mrtimeey.coronaattendancereportserver.domain.repository.EventRepository;
import com.mrtimeey.coronaattendancereportserver.exception.ResourceNotFoundException;
import com.mrtimeey.coronaattendancereportserver.rest.request.OnCreate;
import com.mrtimeey.coronaattendancereportserver.rest.request.OnUpdate;
import com.mrtimeey.coronaattendancereportserver.rest.transfer.EventTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Validated
public class EventService {

    private final TeamService teamService;
    private final EventRepository eventRepository;


    @Validated(OnCreate.class)
    public EventTO createEvent(@Valid EventTO eventTO) {
        if (!teamService.teamExisting(eventTO.getTeamId())) {
            throw new ResourceNotFoundException(String.format("Event not created! Team with id '%s' not found!", eventTO.getTeamId()));
        }
        Event event = Event.fromTransferObject(eventTO);
        return EventTO.fromBusinessModel(eventRepository.save(event));
    }

    @Validated(OnUpdate.class)
    public EventTO updateEvent(@Valid EventTO eventTO) {
        if (!eventRepository.existsById(eventTO.getId())) {
            throw new ResourceNotFoundException(String.format("Event with id '%s' not found!", eventTO.getId()));
        }
        Event event = Event.fromTransferObject(eventTO);
        return EventTO.fromBusinessModel(eventRepository.save(event));
    }

    public Optional<EventTO> getEvent(@NotBlank String eventId) {
        return eventRepository.findById(eventId)
                .map(EventTO::fromBusinessModel);
    }

    public void deleteEvent(@NotBlank String eventId) {
        eventRepository.findById(eventId)
                .ifPresent(eventRepository::delete);
    }
}
