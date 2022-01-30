package com.mrtimeey.coronaattendancereportserver.rest.service;

import com.mrtimeey.coronaattendancereportserver.domain.entity.Event;
import com.mrtimeey.coronaattendancereportserver.domain.repository.EventRepository;
import com.mrtimeey.coronaattendancereportserver.exception.ResourceNotFoundException;
import com.mrtimeey.coronaattendancereportserver.rest.transfer.EventTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EventService {

    private final TeamService teamService;
    private final EventRepository eventRepository;


    public EventTO createEvent(EventTO eventTO) {
        if (!teamService.teamExisting(eventTO.getTeamId())) {
            throw new ResourceNotFoundException(String.format("Event not created! Team with id '%s' not found!", eventTO.getId()));
        }
        Event event = Event.fromTransferObject(eventTO);
        return EventTO.fromBusinessModel(eventRepository.save(event));
    }
}
