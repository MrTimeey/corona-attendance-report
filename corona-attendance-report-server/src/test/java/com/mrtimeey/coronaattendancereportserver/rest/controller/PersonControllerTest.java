package com.mrtimeey.coronaattendancereportserver.rest.controller;

import com.mrtimeey.coronaattendancereportserver.SpringProfiles;
import com.mrtimeey.coronaattendancereportserver.rest.development.DevelopmentService;
import com.mrtimeey.coronaattendancereportserver.rest.service.PersonService;
import com.mrtimeey.coronaattendancereportserver.rest.transfer.PersonTO;
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

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
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

@WebMvcTest(PersonController.class)
@ActiveProfiles(SpringProfiles.NOSECURITY)
class PersonControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PersonService personService;

    @MockBean
    private DevelopmentService developmentService;

    private final JsonTestHelper jsonTestHelper = new JsonTestHelper();

    private String personId;

    @BeforeEach
    public void setup() {
        personId = UUID.randomUUID().toString();
    }

    @Test
    void shouldCreatePerson() throws Exception {
        PersonTO person = PersonTO.builder()
                .name("Test-Person")
                .build();
        when(personService.createPerson(person)).thenReturn(person);

        MockHttpServletRequestBuilder request = post("/persons")
                .content(jsonTestHelper.toJson(person))
                .contentType(MediaType.APPLICATION_JSON_VALUE);

        mockMvc
                .perform(request)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Test-Person"));
    }

    @Test
    void shouldFailCreatePersonBecauseOfExistingId() throws Exception {
        PersonTO person = PersonTO.builder()
                .id(personId)
                .name("Test-Person")
                .build();

        MockHttpServletRequestBuilder request = post("/persons")
                .content(jsonTestHelper.toJson(person))
                .contentType(MediaType.APPLICATION_JSON_VALUE);


        assertThatExceptionOfType(NestedServletException.class)
                .isThrownBy(() -> mockMvc.perform(request))
                .withMessage("Request processing failed; nested exception is javax.validation.ConstraintViolationException: createPerson.personTO.id: must be null");
    }

    @Test
    void shouldUpdatePerson() throws Exception {
        PersonTO person = PersonTO.builder()
                .id(personId)
                .name("Test-Person")
                .phoneNumber("0123456789")
                .build();
        when(personService.updatePerson(person)).thenReturn(person);

        MockHttpServletRequestBuilder request = patch("/persons")
                .content(jsonTestHelper.toJson(person))
                .contentType(MediaType.APPLICATION_JSON_VALUE);

        mockMvc
                .perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test-Person"))
                .andExpect(jsonPath("$.phoneNumber").value("0123456789"));
    }

    @Test
    void shouldFailUpdatePersonBecauseOfMissingId() throws Exception {
        PersonTO person = PersonTO.builder()
                .name("Test-Person")
                .build();

        MockHttpServletRequestBuilder request = patch("/persons")
                .content(jsonTestHelper.toJson(person))
                .contentType(MediaType.APPLICATION_JSON_VALUE);

        assertThatExceptionOfType(NestedServletException.class)
                .isThrownBy(() -> mockMvc.perform(request))
                .withMessage("Request processing failed; nested exception is javax.validation.ConstraintViolationException: updatePerson.personTO.id: must not be null");
    }

    @Test
    void shouldGetAllPersons() throws Exception {
        PersonTO firstPerson = PersonTO.builder()
                .id(UUID.randomUUID().toString())
                .name("Test-Person-1")
                .build();
        PersonTO secondPerson = PersonTO.builder()
                .id(UUID.randomUUID().toString())
                .name("Test-Person-2")
                .build();

        when(personService.getPersonList()).thenReturn(List.of(firstPerson, secondPerson));

        mockMvc
                .perform(get("/persons"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void shouldGetSinglePerson() throws Exception {
        PersonTO person = PersonTO.builder()
                .name("Test-Person")
                .build();
        when(personService.getPerson(personId)).thenReturn(Optional.of(person));

        mockMvc
                .perform(get("/persons/{personId}", personId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test-Person"));
    }

    @Test
    void shouldNotFindAnyPerson() throws Exception {
        when(personService.getPerson(any())).thenReturn(Optional.empty());

        mockMvc
                .perform(get("/persons/{personId}", personId))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldDeleteSinglePerson() throws Exception {
        mockMvc
                .perform(delete("/persons/{personId}", personId))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("")));

        verify(personService, times(1)).deletePerson(personId);
    }

    @Test
    void shouldDeleteAllPersons() throws Exception {
        mockMvc
                .perform(delete("/persons"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("")));

        verify(developmentService, times(1)).deleteAllPersons();
    }

}
