package gr.hua.dit.nocgov.core.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import gr.hua.dit.nocgov.core.dto.PersonPhoneResponse;
import gr.hua.dit.nocgov.core.dto.PersonResponse;
import gr.hua.dit.nocgov.core.model.Person;
import gr.hua.dit.nocgov.core.repository.PersonRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PersonService {
    private static final Logger LOGGER = LoggerFactory.getLogger(PersonService.class);

    private final PersonRepository personRepository;

    public List<PersonResponse> getAllPeople() {
        return personRepository.findAll()
                .stream()
                .map(this::personToPersonResponse)
                .toList();
    }

    public PersonResponse getPersonByAfm(String afm) {
        Person person = personRepository.findByAfm(afm)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        LOGGER.info("Returning person with AFM {}.", afm);

        return personToPersonResponse(person);
    }

    public PersonPhoneResponse getPhoneByAfm(String afm) {
        Person person = personRepository.findByAfm(afm)
                .orElseThrow(() -> {
                    LOGGER.warn("Person with AFM {} not found.", afm);
                    return new ResponseStatusException(HttpStatus.NOT_FOUND);
                });

        String phone = person.getPhone();
        LOGGER.info("Person found with AFM {} and PHONE {}.", afm, phone);

        return new PersonPhoneResponse(phone);
    }

    private PersonResponse personToPersonResponse(Person person) {
        return new PersonResponse(
                person.getAfm(),
                person.getAmka(),
                person.getFirstName(),
                person.getLastName(),
                person.getPhone(),
                person.getType(),
                person.getServiceTypes());
    }
}
