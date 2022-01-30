package com.mrtimeey.coronaattendancereportserver.rest.controller;

import com.mrtimeey.coronaattendancereportserver.rest.request.OnCreate;
import com.mrtimeey.coronaattendancereportserver.rest.request.OnUpdate;
import com.mrtimeey.coronaattendancereportserver.rest.service.PersonService;
import com.mrtimeey.coronaattendancereportserver.rest.transfer.PersonTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<PersonTO> createPerson(@RequestBody @Valid PersonTO personTO) {
        return ResponseEntity.ok(personService.createPerson(personTO));
    }

    @Validated(OnUpdate.class)
    @RequestMapping(method = RequestMethod.PATCH)
    public ResponseEntity<PersonTO> updatePerson(@RequestBody @Valid PersonTO personTO) {
        return ResponseEntity.ok(personService.updatePerson(personTO));
    }

    @RequestMapping(value = "/{personId}", method = RequestMethod.GET)
    public ResponseEntity<PersonTO> getPerson(@PathVariable String personId) {
        return personService.getPerson(personId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<PersonTO>> getPersons() {
        return ResponseEntity.ok(personService.getPersonList());
    }

    @RequestMapping(value = "/{personId}", method = RequestMethod.DELETE)
    public ResponseEntity<Void> deletePerson(@PathVariable String personId) {
        personService.deletePerson(personId);
        return ResponseEntity.ok().build();
    }

    @RequestMapping(method = RequestMethod.DELETE)
    public ResponseEntity<Void> deleteAllPersons() {
        personService.deleteAll();
        return ResponseEntity.ok().build();
    }


}
