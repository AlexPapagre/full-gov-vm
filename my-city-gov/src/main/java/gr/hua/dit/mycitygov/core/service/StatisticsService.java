package gr.hua.dit.mycitygov.core.service;

import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import gr.hua.dit.mycitygov.core.model.Appointment;
import gr.hua.dit.mycitygov.core.model.Request;
import gr.hua.dit.mycitygov.core.repository.AppointmentRepository;
import gr.hua.dit.mycitygov.core.repository.RequestRepository;
import gr.hua.dit.mycitygov.core.service.model.ServiceType;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StatisticsService {
    private final RequestRepository requestRepository;
    private final AppointmentRepository appointmentRepository;

    public long getTotalRequests() {
        return requestRepository.count();
    }

    public Map<ServiceType, Long> getRequestCountByService() {
        return requestRepository.findAll()
                .stream()
                .collect(Collectors.groupingBy(Request::getServiceType, Collectors.counting()));
    }

    public long getTotalAppointments() {
        return appointmentRepository.count();
    }

    public Map<ServiceType, Long> getAppointmentCountByService() {
        return appointmentRepository.findAll()
                .stream()
                .collect(Collectors.groupingBy(Appointment::getServiceType, Collectors.counting()));
    }
}
