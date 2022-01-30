package com.mrtimeey.coronaattendancereportserver.rest.service;

import com.mrtimeey.coronaattendancereportserver.domain.entity.Person;
import com.mrtimeey.coronaattendancereportserver.domain.repository.PersonRepository;
import com.mrtimeey.coronaattendancereportserver.exception.ResourceNotFoundException;
import com.mrtimeey.coronaattendancereportserver.rest.transfer.PersonTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class PersonService {

    private final PersonRepository personRepository;

    public PersonTO createPerson(PersonTO personTO) {
        Person person = Person.fromTransferObject(personTO);
        return PersonTO.fromBusinessModel(personRepository.save(person));
    }

    public PersonTO updatePerson(PersonTO personTO) {
        if (!personRepository.existsById(personTO.getId())) {
            throw new ResourceNotFoundException(String.format("Person with id '%s' not found!", personTO.getId()));
        }
        Person person = Person.fromTransferObject(personTO);
        return PersonTO.fromBusinessModel(personRepository.save(person));
    }

    public Optional<PersonTO> getPerson(String personId) {
        return personRepository.findById(personId)
                .map(PersonTO::fromBusinessModel);
    }

    public boolean personExists(String personId) {
        return personRepository.existsById(personId);
    }

    public void deletePerson(String personId) {
        personRepository.findById(personId)
                .ifPresent(personRepository::delete);
    }

    public List<PersonTO> getPersonList() {
        return personRepository.findAll().stream()
                .map(PersonTO::fromBusinessModel)
                .collect(Collectors.toList());
    }

}
