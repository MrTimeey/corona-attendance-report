package com.mrtimeey.coronaattendancereportserver.rest.development;

import com.mrtimeey.coronaattendancereportserver.domain.repository.EventRepository;
import com.mrtimeey.coronaattendancereportserver.domain.repository.PersonRepository;
import com.mrtimeey.coronaattendancereportserver.domain.repository.TeamRepository;
import com.mrtimeey.coronaattendancereportserver.exception.NotImplementedException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.annotation.Profile;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ProdDevelopmentServiceTest {

    @InjectMocks
    private ProdDevelopmentService serviceUnderTest;
    @Mock
    private TeamRepository teamRepository;
    @Mock
    private EventRepository eventRepository;
    @Mock
    private PersonRepository personRepository;

    @Test
    void testDeleteAll() {
        assertThatExceptionOfType(NotImplementedException.class)
                .isThrownBy(() -> serviceUnderTest.deleteAll())
                .withMessage("Delete all is currently not implemented!");

        verify(teamRepository, never()).deleteAll();
        verify(eventRepository, never()).deleteAll();
        verify(personRepository, never()).deleteAll();
    }

    @Test
    void testDeleteAllTeams() {
        assertThatExceptionOfType(NotImplementedException.class)
                .isThrownBy(() -> serviceUnderTest.deleteAllTeams())
                .withMessage("Delete all teams is currently not implemented!");

        verify(teamRepository, never()).deleteAll();
        verify(eventRepository, never()).deleteAll();
        verify(personRepository, never()).deleteAll();
    }

    @Test
    void testDeleteAllEvents() {
        assertThatExceptionOfType(NotImplementedException.class)
                .isThrownBy(() -> serviceUnderTest.deleteAllEvents())
                .withMessage("Delete all events is currently not implemented!");

        verify(teamRepository, never()).deleteAll();
        verify(eventRepository, never()).deleteAll();
        verify(personRepository, never()).deleteAll();
    }

    @Test
    void testDeleteAllPersons() {
        assertThatExceptionOfType(NotImplementedException.class)
                .isThrownBy(() -> serviceUnderTest.deleteAllPersons())
                .withMessage("Delete all persons is currently not implemented!");

        verify(teamRepository, never()).deleteAll();
        verify(eventRepository, never()).deleteAll();
        verify(personRepository, never()).deleteAll();
    }

    @Test
    void testProfile() {
        Profile annotation = serviceUnderTest.getClass().getAnnotation(Profile.class);
        String profileValue = String.join("", annotation.value());

        assertThat(profileValue).isEqualTo("!local");
    }

}
