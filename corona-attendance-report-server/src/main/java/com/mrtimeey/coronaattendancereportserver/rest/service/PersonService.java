package com.mrtimeey.coronaattendancereportserver.rest.service;

import com.mrtimeey.coronaattendancereportserver.domain.entity.Person;
import com.mrtimeey.coronaattendancereportserver.domain.repository.PersonRepository;
import com.mrtimeey.coronaattendancereportserver.exception.ResourceNotFoundException;
import com.mrtimeey.coronaattendancereportserver.rest.request.OnCreate;
import com.mrtimeey.coronaattendancereportserver.rest.request.OnUpdate;
import com.mrtimeey.coronaattendancereportserver.rest.transfer.PersonTO;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Validated
public class PersonService {

    private final PersonRepository personRepository;

    @Validated(OnCreate.class)
    public PersonTO createPerson(@Valid PersonTO personTO) {
        Person person = Person.fromTransferObject(personTO);
        return PersonTO.fromBusinessModel(personRepository.save(person));
    }

    @Validated(OnUpdate.class)
    public PersonTO updatePerson(@Valid PersonTO personTO) {
        if (!personRepository.existsById(personTO.getId())) {
            throw new ResourceNotFoundException(String.format("Person with id '%s' not found!", personTO.getId()));
        }
        Person person = Person.fromTransferObject(personTO);
        return PersonTO.fromBusinessModel(personRepository.save(person));
    }

    public Optional<PersonTO> getPerson(@NotBlank String personId) {
        return personRepository.findById(personId)
                .map(PersonTO::fromBusinessModel);
    }

    public boolean personExists(String personId) {
        if (Strings.isBlank(personId)) {
            return false;
        }
        return personRepository.existsById(personId);
    }

    public void deletePerson(@NotBlank String personId) {
        personRepository.findById(personId)
                .ifPresent(personRepository::delete);
    }

    public List<PersonTO> getPersonList() {
        return personRepository.findAll().stream()
                .map(PersonTO::fromBusinessModel)
                .collect(Collectors.toList());
    }

}
