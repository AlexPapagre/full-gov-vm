package gr.hua.dit.mycitygov.web.rest;

import java.util.Map;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import gr.hua.dit.mycitygov.core.service.StatisticsService;
import gr.hua.dit.mycitygov.core.service.model.ServiceType;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/statistics")
@RequiredArgsConstructor
public class StatisticsController {
    private final StatisticsService statisticsService;

    @PreAuthorize("hasRole('INTEGRATION_READ')")
    @GetMapping("/requests")
    public Map<String, Object> getRequestStatistics() {
        long totalRequests = statisticsService.getTotalRequests();
        Map<ServiceType, Long> requestsByService = statisticsService.getRequestCountByService();

        return Map.of(
                "totalRequests", totalRequests,
                "requestsByService", requestsByService);
    }

    @PreAuthorize("hasRole('INTEGRATION_READ')")
    @GetMapping("/appointments")
    public Map<String, Object> getAppointmentsStatistics() {
        long totalAppointments = statisticsService.getTotalAppointments();
        Map<ServiceType, Long> appointmentsByService = statisticsService.getAppointmentCountByService();

        return Map.of(
                "totalAppointments", totalAppointments,
                "appointmentsByService", appointmentsByService);
    }
}
