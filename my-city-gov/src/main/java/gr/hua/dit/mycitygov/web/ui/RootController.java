package gr.hua.dit.mycitygov.web.ui;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import gr.hua.dit.mycitygov.core.service.PersonSessionService;
import gr.hua.dit.mycitygov.core.service.model.Person;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/")
@RequiredArgsConstructor
public class RootController {
    private final PersonSessionService personSessionService;

    @GetMapping
    public String root() {
        Person person = personSessionService.getLoggedInPerson();
        if (person == null) {
            return "redirect:/auth/login";
        }

        return switch (person.getType()) {
            case ADMIN -> "redirect:/admin";
            case EMPLOYEE -> "redirect:/employee";
            case CITIZEN -> "redirect:/citizen";
        };
    }

}
