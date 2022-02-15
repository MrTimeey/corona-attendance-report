package com.mrtimeey.coronaattendancereportserver.rest.service;

import com.mrtimeey.coronaattendancereportserver.domain.entity.Person;
import com.mrtimeey.coronaattendancereportserver.domain.repository.PersonRepository;
import com.mrtimeey.coronaattendancereportserver.exception.ResourceNotFoundException;
import com.mrtimeey.coronaattendancereportserver.rest.transfer.PersonTO;
import com.mrtimeey.coronaattendancereportserver.utils.AbstractIntegrationTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class PersonServiceTest extends AbstractIntegrationTest {

    @Autowired
    private PersonService serviceUnderTest;

    @Autowired
    private PersonRepository personRepository;

    @BeforeEach
    @AfterEach
    void clearRepository() {
        personRepository.deleteAll();
    }

    @Test
    void createPerson() {
        PersonTO personTO = PersonTO.builder()
                .name("Test Name")
                .build();

        PersonTO serviceResult = serviceUnderTest.createPerson(personTO);

        PersonTO expectedPerson = PersonTO.builder()
                .id(serviceResult.getId())
                .name("Test Name")
                .phoneNumber("")
                .address("")
                .build();

        validatePerson(serviceResult, expectedPerson);
    }

    @Test
    void createPersonShouldFail_ExistingId() {
        PersonTO personTO = PersonTO.builder()
                .id(UUID.randomUUID().toString())
                .name("Test Name")
                .build();

        assertThatExceptionOfType(ConstraintViolationException.class)
                .isThrownBy(() -> serviceUnderTest.createPerson(personTO))
                .withMessage("createPerson.personTO.id: must be null");
    }

    @Test
    void updatePerson() {
        PersonTO personTO = PersonTO.builder()
                .name("Test Name")
                .build();

        PersonTO createdPerson = serviceUnderTest.createPerson(personTO);

        PersonTO changedPerson = createdPerson.toBuilder()
                .name("Changed Test Name")
                .phoneNumber("0123456")
                .address("Street 1, 12345 City")
                .build();

        PersonTO serviceResult = serviceUnderTest.updatePerson(changedPerson);

        PersonTO expectedPerson = PersonTO.builder()
                .id(createdPerson.getId())
                .name("Changed Test Name")
                .phoneNumber("0123456")
                .address("Street 1, 12345 City")
                .build();

        validatePerson(serviceResult, expectedPerson);
    }

    @Test
    void updatePersonShouldFail_MissingId() {
        PersonTO personTO = PersonTO.builder()
                .name("Test Name")
                .build();

        assertThatExceptionOfType(ConstraintViolationException.class)
                .isThrownBy(() -> serviceUnderTest.updatePerson(personTO))
                .withMessage("updatePerson.personTO.id: must not be null");
    }

    @Test
    void updatePersonShouldFail_NonExistingPerson() {
        String randomId = UUID.randomUUID().toString();
        PersonTO personTO = PersonTO.builder()
                .id(randomId)
                .name("Test Name")
                .build();

        assertThatExceptionOfType(ResourceNotFoundException.class)
                .isThrownBy(() -> serviceUnderTest.updatePerson(personTO))
                .withMessage(String.format("Person with id '%s' not found!", randomId));
    }

    @Test
    void getPerson() {
        PersonTO personTO = PersonTO.builder()
                .name("Test Name")
                .build();

        PersonTO createdPerson = serviceUnderTest.createPerson(personTO);
        Optional<PersonTO> serviceResult = serviceUnderTest.getPerson(createdPerson.getId());

        PersonTO expectedPerson = PersonTO.builder()
                .id(createdPerson.getId())
                .name("Test Name")
                .phoneNumber("")
                .address("")
                .build();

        assertThat(serviceResult).isPresent();
        validatePerson(serviceResult.get(), expectedPerson);
    }

    @Test
    void getPersonWithNonExistingId() {
        Optional<PersonTO> serviceResult = serviceUnderTest.getPerson(UUID.randomUUID().toString());

        assertThat(serviceResult).isNotPresent();
    }

    @Test
    void getPersonShouldFail_NullIdParameter() {
        assertThatExceptionOfType(ConstraintViolationException.class)
                .isThrownBy(() -> serviceUnderTest.getPerson(null))
                .withMessage("getPerson.personId: must not be blank");
    }

    @Test
    void getPersonShouldFail_EmptyIdParameter() {
        assertThatExceptionOfType(ConstraintViolationException.class)
                .isThrownBy(() -> serviceUnderTest.getPerson(""))
                .withMessage("getPerson.personId: must not be blank");
    }

    @Test
    void personExists() {
        PersonTO personTO = PersonTO.builder()
                .name("Test Name")
                .build();

        PersonTO createdPerson = serviceUnderTest.createPerson(personTO);

        boolean result = serviceUnderTest.personExists(createdPerson.getId());

        assertThat(result).isTrue();
    }

    @Test
    void personExistsWithNonExistingPerson() {
        boolean result = serviceUnderTest.personExists(UUID.randomUUID().toString());

        assertThat(result).isFalse();
    }

    @Test
    void personExistsWithNonExistingPerson_NullIdParameter() {
        boolean result = serviceUnderTest.personExists(null);

        assertThat(result).isFalse();
    }

    @Test
    void personExistsWithNonExistingPerson_EmptyIdParameter() {
        boolean result = serviceUnderTest.personExists("");

        assertThat(result).isFalse();
    }

    @Test
    void deletePerson() {
        PersonTO personTO = PersonTO.builder()
                .name("Test Name")
                .build();

        PersonTO createdPerson = serviceUnderTest.createPerson(personTO);

        assertThat(personRepository.existsById(createdPerson.getId())).isTrue();

        serviceUnderTest.deletePerson(createdPerson.getId());

        assertThat(personRepository.existsById(createdPerson.getId())).isFalse();
    }

    @Test
    void deletePersonShouldFail_NullIdParameter() {
        assertThatExceptionOfType(ConstraintViolationException.class)
                .isThrownBy(() -> serviceUnderTest.deletePerson(null))
                .withMessage("deletePerson.personId: must not be blank");
    }

    @Test
    void deletePersonShouldFail_EmptyIdParameter() {
        assertThatExceptionOfType(ConstraintViolationException.class)
                .isThrownBy(() -> serviceUnderTest.deletePerson(""))
                .withMessage("deletePerson.personId: must not be blank");
    }

    @Test
    void getPersonList() {
        PersonTO personTO = PersonTO.builder()
                .name("Test Name")
                .build();

        PersonTO firstPerson = serviceUnderTest.createPerson(personTO);
        PersonTO secondPerson = serviceUnderTest.createPerson(personTO);

        List<PersonTO> result = serviceUnderTest.getPersonList();

        assertThat(result)
                .hasSize(2)
                .containsExactlyInAnyOrder(firstPerson, secondPerson);
    }

    private void validatePerson(PersonTO serviceResult, PersonTO expectedPerson) {

        assertThat(serviceResult).isEqualTo(expectedPerson);

        Optional<Person> repositoryResult = personRepository.findById(expectedPerson.getId());

        assertThat(repositoryResult).isPresent();
        assertThat(repositoryResult.get().getId()).isEqualTo(expectedPerson.getId());
        assertThat(repositoryResult.get().getName()).isEqualTo(expectedPerson.getName());
        assertThat(repositoryResult.get().getAddress()).isNotNull().isEqualTo(expectedPerson.getAddress());
        assertThat(repositoryResult.get().getPhoneNumber()).isNotNull().isEqualTo(expectedPerson.getPhoneNumber());
    }

}