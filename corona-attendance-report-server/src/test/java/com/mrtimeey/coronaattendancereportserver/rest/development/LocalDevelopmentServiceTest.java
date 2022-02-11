package com.mrtimeey.coronaattendancereportserver.rest.development;

import com.mrtimeey.coronaattendancereportserver.domain.repository.EventRepository;
import com.mrtimeey.coronaattendancereportserver.domain.repository.PersonRepository;
import com.mrtimeey.coronaattendancereportserver.domain.repository.TeamRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.annotation.Profile;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class LocalDevelopmentServiceTest {

    @InjectMocks
    private LocalDevelopmentService serviceUnderTest;
    @Mock
    private TeamRepository teamRepository;
    @Mock
    private EventRepository eventRepository;
    @Mock
    private PersonRepository personRepository;

    @Test
    void testDeleteAll() {
        serviceUnderTest.deleteAll();

        verify(teamRepository, times(1)).deleteAll();
        verify(eventRepository, times(1)).deleteAll();
        verify(personRepository, times(1)).deleteAll();
    }

    @Test
    void testDeleteAllTeams() {
        serviceUnderTest.deleteAllTeams();

        verify(teamRepository, times(1)).deleteAll();
        verify(eventRepository, never()).deleteAll();
        verify(personRepository, never()).deleteAll();
    }

    @Test
    void testDeleteAllEvents() {
        serviceUnderTest.deleteAllEvents();

        verify(teamRepository, never()).deleteAll();
        verify(eventRepository, times(1)).deleteAll();
        verify(personRepository, never()).deleteAll();
    }

    @Test
    void testDeleteAllPersons() {
        serviceUnderTest.deleteAllPersons();

        verify(teamRepository, never()).deleteAll();
        verify(eventRepository, never()).deleteAll();
        verify(personRepository, times(1)).deleteAll();
    }

    @Test
    void testProfile() {
        Profile annotation = serviceUnderTest.getClass().getAnnotation(Profile.class);
        String profileValue = String.join("", annotation.value());

        assertThat(profileValue).isEqualTo("local");
    }

}
