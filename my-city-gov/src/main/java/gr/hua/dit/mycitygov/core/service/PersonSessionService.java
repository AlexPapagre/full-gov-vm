package gr.hua.dit.mycitygov.core.service;

import org.springframework.stereotype.Service;

import gr.hua.dit.mycitygov.core.service.model.Person;
import gr.hua.dit.mycitygov.core.service.model.PersonType;
import jakarta.servlet.http.HttpSession;

@Service
public class PersonSessionService {
    private static final String PERSON_SESSION_KEY = "person";

    private final HttpSession session;

    public PersonSessionService(HttpSession session) {
        this.session = session;
    }

    public void setLoggedInPerson(Person person) {
        session.setAttribute(PERSON_SESSION_KEY, person);
    }

    public Person getLoggedInPerson() {
        Object personObj = session.getAttribute(PERSON_SESSION_KEY);
        if (personObj instanceof Person) {
            return (Person) personObj;
        }
        return null;
    }

    public boolean isLoggedIn() {
        return getLoggedInPerson() != null;
    }

    public boolean isLoggedIn(PersonType type) {
        Person person = getLoggedInPerson();
        return person != null && person.getType() == type;
    }

    public void logout() {
        session.removeAttribute(PERSON_SESSION_KEY);
    }
}
