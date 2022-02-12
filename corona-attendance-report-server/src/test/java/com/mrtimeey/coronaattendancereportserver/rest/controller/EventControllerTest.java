package com.mrtimeey.coronaattendancereportserver.rest.controller;

import com.mrtimeey.coronaattendancereportserver.SpringProfiles;
import com.mrtimeey.coronaattendancereportserver.rest.development.DevelopmentService;
import com.mrtimeey.coronaattendancereportserver.rest.service.EventService;
import com.mrtimeey.coronaattendancereportserver.rest.transfer.EventTO;
import com.mrtimeey.coronaattendancereportserver.utils.JsonTestHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.web.util.NestedServletException;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(EventController.class)
@ActiveProfiles(SpringProfiles.NOSECURITY)
class EventControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EventService eventService;

    @MockBean
    private DevelopmentService developmentService;

    private JsonTestHelper jsonTestHelper = new JsonTestHelper();

    private String eventId;

    @BeforeEach
    public void setup() {
        eventId = UUID.randomUUID().toString();
    }

    @Test
    void shouldCreateEvent() throws Exception {
        String teamId = UUID.randomUUID().toString();
        EventTO event = EventTO.builder()
                .name("Testevent")
                .teamId(teamId)
                .date(null)
                .build();
        when(eventService.createEvent(event)).thenReturn(event);

        MockHttpServletRequestBuilder request = post("/events")
                .content(jsonTestHelper.toJson(event))
                .contentType(MediaType.APPLICATION_JSON_VALUE);

        mockMvc
                .perform(request)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is("Testevent")))
                .andExpect(jsonPath("$.teamId", is(teamId)));
    }

    @Test
    void shouldFailCreateEventBecauseOfMissingTeamId() throws Exception {
        EventTO event = EventTO.builder()
                .name("Testevent")
                .build();

        MockHttpServletRequestBuilder request = post("/events")
                .content(jsonTestHelper.toJson(event))
                .contentType(MediaType.APPLICATION_JSON_VALUE);

        mockMvc
                .perform(request)
                .andExpect(status().isBadRequest())
                .andExpect(content().string(""));
    }

    @Test
    void shouldUpdateEvent() throws Exception {
        String teamId = UUID.randomUUID().toString();
        EventTO event = EventTO.builder()
                .id(eventId)
                .name("Testevent")
                .teamId(teamId)
                .endTime("endTime")
                .build();
        when(eventService.updateEvent(event)).thenReturn(event);

        MockHttpServletRequestBuilder request = patch("/events")
                .content(jsonTestHelper.toJson(event))
                .contentType(MediaType.APPLICATION_JSON_VALUE);

        mockMvc
                .perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Testevent")))
                .andExpect(jsonPath("$.endTime", is("endTime")))
                .andExpect(jsonPath("$.teamId", is(teamId)));
    }

    @Test
    void shouldFailUpdateEventBecauseOfMissingId() throws Exception {
        String teamId = UUID.randomUUID().toString();
        EventTO event = EventTO.builder()
                .name("Testevent")
                .teamId(teamId)
                .endTime("endTime")
                .build();

        MockHttpServletRequestBuilder request = patch("/events")
                .content(jsonTestHelper.toJson(event))
                .contentType(MediaType.APPLICATION_JSON_VALUE);

        assertThatExceptionOfType(NestedServletException.class)
                .isThrownBy(() -> mockMvc.perform(request))
                .withMessage("Request processing failed; nested exception is javax.validation.ConstraintViolationException: updateEvent.eventTO.id: must not be null");
    }

    @Test
    void shouldGetSingleEvent() throws Exception {
        EventTO event = EventTO.builder()
                .name("Testevent")
                .build();
        when(eventService.getEvent(eventId)).thenReturn(Optional.of(event));

        mockMvc
                .perform(get("/events/{eventId}", eventId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Testevent")));
    }

    @Test
    void shouldDeleteSingleEvents() throws Exception {
        mockMvc
                .perform(delete("/events/{eventId}", eventId))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("")));

        verify(eventService, times(1)).deleteEvent(eventId);
    }

    @Test
    void shouldDeleteAllEvents() throws Exception {
        mockMvc
                .perform(delete("/events"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("")));

        verify(developmentService, times(1)).deleteAllEvents();
    }

}
