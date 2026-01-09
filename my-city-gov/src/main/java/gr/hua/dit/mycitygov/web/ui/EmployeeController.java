package gr.hua.dit.mycitygov.web.ui;

import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import gr.hua.dit.mycitygov.core.model.Request;
import gr.hua.dit.mycitygov.core.model.RequestStatus;
import gr.hua.dit.mycitygov.core.service.EmployeeService;
import gr.hua.dit.mycitygov.core.service.PersonSessionService;
import gr.hua.dit.mycitygov.core.service.model.Person;
import gr.hua.dit.mycitygov.core.service.model.PersonType;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/employee")
@RequiredArgsConstructor
public class EmployeeController {
    private static final Logger LOGGER = LoggerFactory.getLogger(EmployeeController.class);

    private final PersonSessionService personSessionService;
    private final EmployeeService employeeService;

    @ModelAttribute
    public void checkLogin(Model model) {
        Person person = personSessionService.getLoggedInPerson();
        if (person == null || person.getType() != PersonType.EMPLOYEE) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        model.addAttribute("person", person);
    }

    @GetMapping
    public String dashboard() {
        return "employee/dashboard";
    }

    @GetMapping("/profile")
    public String viewProfile() {
        return "employee/profile/view";
    }

    @GetMapping("/requests")
    public String listRequests(Model model) {
        Person employee = personSessionService.getLoggedInPerson();

        model.addAttribute("requests", employeeService.getRequestsByServices(employee.getServiceTypes()));

        return "employee/requests/list";
    }

    @GetMapping("/requests/{id}")
    public String viewRequest(@PathVariable Long id, Model model) {
        Request request = employeeService.getRequest(id);

        if (request.getFileName() != null && !request.getFileName().isEmpty()) {
            String originalFileName = request.getFileName().substring(request.getFileName().lastIndexOf('-') + 1);
            request.setOriginalFileName(originalFileName);
        }

        model.addAttribute("request", request);
        model.addAttribute("statuses", RequestStatus.values());

        return "employee/requests/view";
    }

    @PostMapping("/requests/{id}/update")
    public String updateRequest(@PathVariable Long id, @RequestParam RequestStatus status,
            @RequestParam(required = false) String response, RedirectAttributes redirectAttributes) {
        employeeService.updateRequest(id, status, response);
        redirectAttributes.addFlashAttribute("message", "Request updated successfully!");
        LOGGER.info("Request with ID {} updated by employee.", id);

        return "redirect:/employee/requests/" + id;
    }

    @GetMapping("/appointments")
    public String listAppointments(Model model) {
        Person employee = personSessionService.getLoggedInPerson();

        model.addAttribute("appointments", employeeService.getAppointmentsByServices(employee.getServiceTypes()));

        return "employee/appointments/list";
    }

    @PostMapping("/appointments/{id}/update")
    public String updateAppointment(@PathVariable Long id, @RequestParam LocalDateTime dateTime,
            RedirectAttributes redirectAttributes) {
        try {
            employeeService.updateAppointment(id, dateTime);
            redirectAttributes.addFlashAttribute("message", "Appointment updated successfully!");
            LOGGER.info("Appointment with ID {} updated by employee.", id);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            LOGGER.error("{}", e.getMessage());
        }

        return "redirect:/employee/appointments";
    }

    @PostMapping("/appointments/{id}/accept")
    public String acceptAppointment(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            employeeService.acceptAppointment(id);
            redirectAttributes.addFlashAttribute("message", "Appointment accepted successfully!");
            LOGGER.info("Appointment with ID {} accepted by employee.", id);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            LOGGER.error("{}", e.getMessage());
        }

        return "redirect:/employee/appointments";
    }

    @PostMapping("/appointments/{id}/cancel")
    public String cancelAppointment(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        employeeService.cancelAppointment(id);
        redirectAttributes.addFlashAttribute("message", "Appointment cancelled successfully!");
        LOGGER.info("Appointment with ID {} cancelled by employee.", id);

        return "redirect:/employee/appointments";
    }
}
