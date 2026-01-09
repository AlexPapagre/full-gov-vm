package gr.hua.dit.mycitygov.web.ui;

import java.util.List;

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

import gr.hua.dit.mycitygov.core.model.Appointment;
import gr.hua.dit.mycitygov.core.model.Request;
import gr.hua.dit.mycitygov.core.service.AdminService;
import gr.hua.dit.mycitygov.core.service.PersonSessionService;
import gr.hua.dit.mycitygov.core.service.model.Person;
import gr.hua.dit.mycitygov.core.service.model.PersonType;
import gr.hua.dit.mycitygov.core.service.model.ServiceType;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {
    private static final Logger LOGGER = LoggerFactory.getLogger(AdminController.class);

    private final PersonSessionService personSessionService;
    private final AdminService adminService;

    @ModelAttribute
    public void checkLogin(Model model) {
        Person person = personSessionService.getLoggedInPerson();
        if (person == null || person.getType() != PersonType.ADMIN) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        model.addAttribute("person", person);
    }

    @GetMapping
    public String dashboard() {
        return "admin/dashboard";
    }

    @GetMapping("/profile")
    public String viewProfile() {
        return "admin/profile/view";
    }

    @GetMapping("/requests")
    public String listRequests(Model model) {
        List<Request> requests = adminService.getAllRequests();
        model.addAttribute("requests", requests);
        model.addAttribute("services", ServiceType.values());
        return "admin/requests/list";
    }

    @PostMapping("/requests/{id}/update-service")
    public String updateRequestService(@PathVariable Long id, @RequestParam ServiceType serviceType,
            RedirectAttributes redirectAttributes) {
        try {
            adminService.updateRequestServiceType(id, serviceType);
            redirectAttributes.addFlashAttribute("message", "Request updated successfully!");
            LOGGER.info("Request with ID {} changed service type by admin.", id);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            LOGGER.error("{}", e.getMessage());
        }
        return "redirect:/admin/requests";
    }

    @GetMapping("/appointments")
    public String listAppointments(Model model) {
        List<Appointment> appointments = adminService.getAllAppointments();
        model.addAttribute("appointments", appointments);
        model.addAttribute("services", ServiceType.values());
        return "admin/appointments/list";
    }

    @PostMapping("/appointments/{id}/update-service")
    public String updateAppointmentService(@PathVariable Long id, @RequestParam ServiceType serviceType,
            RedirectAttributes redirectAttributes) {
        try {
            adminService.updateAppointmentServiceType(id, serviceType);
            redirectAttributes.addFlashAttribute("message", "Appointment updated successfully!");
            LOGGER.info("Appointment with ID {} changed service type by admin.", id);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            LOGGER.error("{}", e.getMessage());
        }
        return "redirect:/admin/appointments";
    }

    @GetMapping("/statistics")
    public String viewStatistics(Model model) {
        model.addAttribute("requestStats", adminService.getRequestCountByService());
        model.addAttribute("appointmentStats", adminService.getAppointmentCountByService());
        model.addAttribute("totalRequests", adminService.getTotalRequests());
        model.addAttribute("totalAppointments", adminService.getTotalAppointments());
        model.addAttribute("services", ServiceType.values());
        return "admin/statistics/view";
    }
}
