package com.mrtimeey.coronaattendancereportserver.rest.service;

import com.mrtimeey.coronaattendancereportserver.domain.entity.Event;
import com.mrtimeey.coronaattendancereportserver.domain.entity.EventParticipant;
import com.mrtimeey.coronaattendancereportserver.domain.entity.EventStatus;
import com.mrtimeey.coronaattendancereportserver.domain.repository.EventRepository;
import com.mrtimeey.coronaattendancereportserver.exception.ResourceNotFoundException;
import com.mrtimeey.coronaattendancereportserver.rest.transfer.EventTO;
import com.mrtimeey.coronaattendancereportserver.rest.transfer.PersonTO;
import com.mrtimeey.coronaattendancereportserver.rest.transfer.TeamTO;
import com.mrtimeey.coronaattendancereportserver.utils.AbstractIntegrationTest;
import com.mrtimeey.coronaattendancereportserver.utils.DateUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import javax.validation.ConstraintViolationException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.when;

class EventServiceTest extends AbstractIntegrationTest {

    @Autowired
    private EventService serviceUnderTest;

    @Autowired
    private EventRepository eventRepository;

    @MockBean
    private TeamService teamService;

    private final String existingTeamId = UUID.randomUUID().toString();

    @BeforeEach
    @AfterEach
    void clearRepository() {
        eventRepository.deleteAll();
        TeamTO team = TeamTO.builder().id(existingTeamId).name("Test Team").build();
        when(teamService.getTeam(existingTeamId)).thenReturn(Optional.of(team));
    }

    @Test
    void createEvent() {
        PersonTO personToCreate = PersonTO.builder().name("Hans").id(UUID.randomUUID().toString()).build();

        TeamTO team = TeamTO.builder()
                .id(existingTeamId)
                .name("Test Team")
                .members(List.of(personToCreate))
                .build();

        when(teamService.getTeam(existingTeamId)).thenReturn(Optional.of(team));

        EventTO eventTO = EventTO.builder()
                .name("Test Event Name")
                .teamId(existingTeamId)
                .build();

        EventTO serviceResult = serviceUnderTest.createEvent(eventTO);

        EventParticipant eventParticipant = EventParticipant.builder().name("Hans").build();
        EventTO expectedEvent = EventTO.builder()
                .id(serviceResult.getId())
                .name("Test Event Name")
                .teamId(existingTeamId)
                .date(LocalDate.now())
                .startTime("")
                .endTime("")
                .created(LocalDateTime.now())
                .released(null)
                .status(EventStatus.CREATED)
                .participants(List.of(eventParticipant))
                .sent(null)
                .build();

        validateEvent(serviceResult, expectedEvent);
    }

    @Test
    void createEventShouldFail_NonExistingTeam() {
        String randomId = UUID.randomUUID().toString();
        EventTO eventTO = EventTO.builder()
                .name("Test Event Name")
                .teamId(randomId)
                .build();

        assertThatExceptionOfType(ResourceNotFoundException.class)
                .isThrownBy(() -> serviceUnderTest.createEvent(eventTO))
                .withMessage(String.format("Event not created! Team with id '%s' not found!", randomId));
    }

    @Test
    void createEventShouldFail_ExistingId() {
        EventTO eventTO = EventTO.builder()
                .id(UUID.randomUUID().toString())
                .teamId(existingTeamId)
                .name("Test Event")
                .build();

        assertThatExceptionOfType(ConstraintViolationException.class)
                .isThrownBy(() -> serviceUnderTest.createEvent(eventTO))
                .withMessage("createEvent.eventTO.id: must be null");
    }

    @Test
    void createEventShouldFail_NullTeamId() {
        EventTO eventTO = EventTO.builder()
                .teamId(null)
                .name("Test Event")
                .build();

        assertThatExceptionOfType(ConstraintViolationException.class)
                .isThrownBy(() -> serviceUnderTest.createEvent(eventTO))
                .withMessage("createEvent.eventTO.teamId: must not be blank");
    }

    @Test
    void createEventShouldFail_EmptyTeamId() {
        EventTO eventTO = EventTO.builder()
                .teamId("")
                .name("Test Event")
                .build();

        assertThatExceptionOfType(ConstraintViolationException.class)
                .isThrownBy(() -> serviceUnderTest.createEvent(eventTO))
                .withMessage("createEvent.eventTO.teamId: must not be blank");
    }

    @Test
    void updateEvent() {
        EventTO eventTO = EventTO.builder()
                .name("Test Event")
                .teamId(existingTeamId)
                .build();

        EventTO createdEvent = serviceUnderTest.createEvent(eventTO);

        EventTO changedEvent = createdEvent.toBuilder()
                .name("Changed Test Event")
                .startTime("10:00 Uhr")
                .endTime("12:00 Uhr")
                .build();

        EventTO serviceResult = serviceUnderTest.updateEvent(changedEvent);

        EventTO expectedEvent = EventTO.builder()
                .id(serviceResult.getId())
                .name("Changed Test Event")
                .teamId(existingTeamId)
                .date(LocalDate.now())
                .startTime("10:00 Uhr")
                .endTime("12:00 Uhr")
                .created(LocalDateTime.now())
                .released(null)
                .participants(List.of())
                .status(EventStatus.CREATED)
                .sent(null)
                .build();

        validateEvent(serviceResult, expectedEvent);
    }

    @Test
    void updateEventShouldFail_MissingId() {
        EventTO eventTO = EventTO.builder()
                .teamId(existingTeamId)
                .name("Test Event Name")
                .build();

        assertThatExceptionOfType(ConstraintViolationException.class)
                .isThrownBy(() -> serviceUnderTest.updateEvent(eventTO))
                .withMessage("updateEvent.eventTO.id: must not be null");
    }

    @Test
    void updateEventShouldFail_EmptyTeamId() {
        EventTO eventTO = EventTO.builder()
                .id(UUID.randomUUID().toString())
                .teamId("")
                .name("Test Event Name")
                .build();

        assertThatExceptionOfType(ConstraintViolationException.class)
                .isThrownBy(() -> serviceUnderTest.updateEvent(eventTO))
                .withMessage("updateEvent.eventTO.teamId: must not be blank");
    }

    @Test
    void updateEventShouldFail_NullTeamId() {
        EventTO eventTO = EventTO.builder()
                .id(UUID.randomUUID().toString())
                .teamId(null)
                .name("Test Event Name")
                .build();

        assertThatExceptionOfType(ConstraintViolationException.class)
                .isThrownBy(() -> serviceUnderTest.updateEvent(eventTO))
                .withMessage("updateEvent.eventTO.teamId: must not be blank");
    }

    @Test
    void updateEventShouldFail_NonExistingEvent() {
        String randomId = UUID.randomUUID().toString();
        EventTO eventTO = EventTO.builder()
                .id(randomId)
                .teamId(existingTeamId)
                .name("Test Event Name")
                .build();

        assertThatExceptionOfType(ResourceNotFoundException.class)
                .isThrownBy(() -> serviceUnderTest.updateEvent(eventTO))
                .withMessage(String.format("Event with id '%s' not found!", randomId));
    }

    @Test
    void getEvent() {
        EventTO eventTO = EventTO.builder()
                .name("Test Event")
                .teamId(existingTeamId)
                .build();

        EventTO createdEvent = serviceUnderTest.createEvent(eventTO);
        Optional<EventTO> serviceResult = serviceUnderTest.getEvent(createdEvent.getId());

        EventTO expectedEvent = EventTO.builder()
                .id(createdEvent.getId())
                .name("Test Event")
                .teamId(existingTeamId)
                .date(LocalDate.now())
                .startTime("")
                .endTime("")
                .created(LocalDateTime.now())
                .released(null)
                .status(EventStatus.CREATED)
                .sent(null)
                .participants(List.of())
                .build();

        assertThat(serviceResult).isPresent();
        validateEvent(serviceResult.get(), expectedEvent);
    }

    @Test
    void getEventWithNonExistingId() {
        Optional<EventTO> serviceResult = serviceUnderTest.getEvent(UUID.randomUUID().toString());

        assertThat(serviceResult).isNotPresent();
    }

    @Test
    void getEventShouldFail_NullIdParameter() {
        assertThatExceptionOfType(ConstraintViolationException.class)
                .isThrownBy(() -> serviceUnderTest.getEvent(null))
                .withMessage("getEvent.eventId: must not be blank");
    }

    @Test
    void getEventShouldFail_EmptyIdParameter() {
        assertThatExceptionOfType(ConstraintViolationException.class)
                .isThrownBy(() -> serviceUnderTest.getEvent(""))
                .withMessage("getEvent.eventId: must not be blank");
    }

    @Test
    void deleteEvent() {
        EventTO eventTO = EventTO.builder()
                .name("Test Event")
                .teamId(existingTeamId)
                .build();

        EventTO createdEvent = serviceUnderTest.createEvent(eventTO);

        assertThat(eventRepository.existsById(createdEvent.getId())).isTrue();

        serviceUnderTest.deleteEvent(createdEvent.getId());

        assertThat(eventRepository.existsById(createdEvent.getId())).isFalse();
    }

    @Test
    void deleteEventShouldFail_NullIdParameter() {
        assertThatExceptionOfType(ConstraintViolationException.class)
                .isThrownBy(() -> serviceUnderTest.deleteEvent(null))
                .withMessage("deleteEvent.eventId: must not be blank");
    }

    @Test
    void deleteEventShouldFail_EmptyIdParameter() {
        assertThatExceptionOfType(ConstraintViolationException.class)
                .isThrownBy(() -> serviceUnderTest.deleteEvent(""))
                .withMessage("deleteEvent.eventId: must not be blank");
    }

    private void validateEvent(EventTO serviceResult, EventTO expectedEvent) {

        assertThat(serviceResult).usingRecursiveComparison()
                .ignoringFields("created")
                .isEqualTo(expectedEvent);

        Optional<Event> repositoryResult = eventRepository.findById(expectedEvent.getId());

        assertThat(repositoryResult).isPresent();
        assertThat(repositoryResult.get().getId()).isEqualTo(expectedEvent.getId());
        assertThat(repositoryResult.get().getName()).isEqualTo(expectedEvent.getName());
        assertThat(repositoryResult.get().getTeamId()).isEqualTo(expectedEvent.getTeamId());
        assertThat(repositoryResult.get().getDate()).isNotNull().isEqualTo(expectedEvent.getDate());
        assertThat(repositoryResult.get().getStartTime()).isNotNull().isEqualTo(expectedEvent.getStartTime());
        assertThat(repositoryResult.get().getEndTime()).isNotNull().isEqualTo(expectedEvent.getEndTime());
        assertThat(repositoryResult.get().getReleased()).isEqualTo(expectedEvent.getReleased());
        assertThat(repositoryResult.get().getStatus()).isNotNull().isEqualTo(expectedEvent.getStatus());
        assertThat(repositoryResult.get().getSent()).isEqualTo(expectedEvent.getSent());
        assertThat(repositoryResult.get().getParticipants()).isEqualTo(expectedEvent.getParticipants());

        DateUtils.isBetweenNowAndOneMinuteAgo(repositoryResult.get().getCreated());
    }

}
