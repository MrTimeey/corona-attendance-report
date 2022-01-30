package com.mrtimeey.coronaattendancereportserver.rest.controller;

import com.mrtimeey.coronaattendancereportserver.rest.request.OnCreate;
import com.mrtimeey.coronaattendancereportserver.rest.request.OnUpdate;
import com.mrtimeey.coronaattendancereportserver.rest.service.PersonService;
import com.mrtimeey.coronaattendancereportserver.rest.transfer.PersonTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(PersonController.API_URL)
public class PersonController {

    public static final String API_URL = "/persons";

    private final PersonService personService;

    @Validated(OnCreate.class)
    @PostMapping
    public ResponseEntity<PersonTO> createPerson(@RequestBody @Valid PersonTO personTO) {
        return ResponseEntity.ok(personService.createPerson(personTO));
    }

    @Validated(OnUpdate.class)
    @PatchMapping
    public ResponseEntity<PersonTO> updatePerson(@RequestBody @Valid PersonTO personTO) {
        return ResponseEntity.ok(personService.updatePerson(personTO));
    }

    @GetMapping(value = "/{personId}")
    public ResponseEntity<PersonTO> getPerson(@PathVariable String personId) {
        return personService.getPerson(personId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @GetMapping
    public ResponseEntity<List<PersonTO>> getPersons() {
        return ResponseEntity.ok(personService.getPersonList());
    }

    @DeleteMapping(value = "/{personId}")
    public ResponseEntity<Void> deletePerson(@PathVariable String personId) {
        personService.deletePerson(personId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteAllPersons() {
        personService.deleteAll();
        return ResponseEntity.ok().build();
    }


}
