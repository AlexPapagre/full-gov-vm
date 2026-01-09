package gr.hua.dit.nocgov.web.rest;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import gr.hua.dit.nocgov.core.dto.PersonPhoneResponse;
import gr.hua.dit.nocgov.core.dto.PersonResponse;
import gr.hua.dit.nocgov.core.service.PersonService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("api/v1/person")
@RequiredArgsConstructor
public class PersonController {
    private final PersonService personService;

    @GetMapping
    public List<PersonResponse> getAllPeople() {
        return personService.getAllPeople();
    }

    @GetMapping("/{afm}")
    public PersonResponse getPersonByAfm(@PathVariable String afm) {
        return personService.getPersonByAfm(afm);
    }

    @GetMapping("/{afm}/phone")
    public PersonPhoneResponse getPersonPhone(@PathVariable String afm) {
        return personService.getPhoneByAfm(afm);
    }
}
