package gr.hua.dit.mycitygov.web.ui;

import java.time.LocalDateTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import gr.hua.dit.mycitygov.core.model.Appointment;
import gr.hua.dit.mycitygov.core.model.Request;
import gr.hua.dit.mycitygov.core.model.RequestStatus;
import gr.hua.dit.mycitygov.core.service.CitizenService;
import gr.hua.dit.mycitygov.core.service.MinioService;
import gr.hua.dit.mycitygov.core.service.PersonSessionService;
import gr.hua.dit.mycitygov.core.service.model.Person;
import gr.hua.dit.mycitygov.core.service.model.PersonType;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/citizen")
@RequiredArgsConstructor
public class CitizenController {
    private static final Logger LOGGER = LoggerFactory.getLogger(CitizenController.class);

    private final PersonSessionService personSessionService;
    private final CitizenService citizenService;
    private final MinioService minioService;

    @ModelAttribute
    public void checkLogin(Model model) {
        Person person = personSessionService.getLoggedInPerson();
        if (person == null || person.getType() != PersonType.CITIZEN) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        model.addAttribute("person", person);
    }

    @GetMapping
    public String dashboard() {
        return "citizen/dashboard";
    }

    @GetMapping("/profile")
    public String viewProfile() {
        return "citizen/profile/view";
    }

    @GetMapping("/services")
    public String viewServices(Model model) {
        model.addAttribute("services", citizenService.getAllServices());
        return "citizen/services/list";
    }

    @GetMapping("/requests/create")
    public String createRequestForm(Model model) {
        model.addAttribute("request", new Request());
        model.addAttribute("services", citizenService.getAllServices());
        return "citizen/requests/create";
    }

    @PostMapping("/requests/create")
    public String submitRequest(@ModelAttribute Request request, @RequestParam("file") MultipartFile file,
            RedirectAttributes redirectAttributes) {
        Person person = personSessionService.getLoggedInPerson();
        request.setCitizenAfm(person.getAfm());
        request.setStatus(RequestStatus.SUBMITTED);
        request.setCreatedAt(LocalDateTime.now());

        if (!file.isEmpty()) {
            String fileName = "request-" + person.getAfm() + "-" + System.currentTimeMillis() + "-"
                    + file.getOriginalFilename();
            try {
                minioService.uploadFile(fileName, file);
                request.setFileName(fileName);
            } catch (Exception e) {
                redirectAttributes.addFlashAttribute("error", e.getMessage());
                LOGGER.error("{}", e.getMessage());
                return "redirect:/citizen/requests/create";
            }
        }

        citizenService.submitRequest(request);
        redirectAttributes.addFlashAttribute("message", "Request submitted successfully!");
        LOGGER.info("Citizen with AFM {} submitted a request successfully!", person.getAfm());
        return "redirect:/citizen/requests/create";
    }

    @GetMapping("/requests")
    public String listRequests(Model model) {
        Person person = personSessionService.getLoggedInPerson();
        List<Request> requests = citizenService.getRequestsByCitizenAfm(person.getAfm());

        for (Request request : requests) {
            String fileName = request.getFileName();
            if (fileName != null && !fileName.isEmpty()) {
                String originalFileName = fileName.substring(fileName.lastIndexOf('-') + 1);
                request.setOriginalFileName(originalFileName);
            }
        }

        model.addAttribute("requests", requests);
        return "citizen/requests/list";
    }

    @GetMapping("/appointments/create")
    public String createAppointmentForm(Model model) {
        model.addAttribute("appointment", new Appointment());
        model.addAttribute("services", citizenService.getAllServices());
        return "citizen/appointments/create";
    }

    @PostMapping("/appointments/create")
    public String submitAppointment(@ModelAttribute Appointment appointment, RedirectAttributes redirectAttributes) {
        Person person = personSessionService.getLoggedInPerson();
        appointment.setCitizenAfm(person.getAfm());
        try {
            citizenService.createAppointment(appointment);
            redirectAttributes.addFlashAttribute("message", "Appointment submitted successfully!");
            LOGGER.info("Citizen with AFM {} submitted an appointment successfully!", person.getAfm());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            LOGGER.error("{}", e.getMessage());
        }

        return "redirect:/citizen/appointments/create";
    }

    @GetMapping("/appointments")
    public String listAppointments(Model model) {
        Person person = personSessionService.getLoggedInPerson();
        model.addAttribute("appointments", citizenService.getAppointmentsByCitizenAfm(person.getAfm()));
        return "citizen/appointments/list";
    }
}
