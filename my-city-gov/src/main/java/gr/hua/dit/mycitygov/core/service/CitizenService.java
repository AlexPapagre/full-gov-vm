package gr.hua.dit.mycitygov.core.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import gr.hua.dit.mycitygov.core.model.Appointment;
import gr.hua.dit.mycitygov.core.model.Request;
import gr.hua.dit.mycitygov.core.repository.AppointmentRepository;
import gr.hua.dit.mycitygov.core.repository.RequestRepository;
import gr.hua.dit.mycitygov.core.service.model.ServiceType;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CitizenService {
    private final RequestRepository requestRepository;
    private final AppointmentRepository appointmentRepository;

    public List<ServiceType> getAllServices() {
        return List.of(ServiceType.values());
    }

    public void submitRequest(Request request) {
        requestRepository.save(request);
    }

    public List<Request> getRequestsByCitizenAfm(String afm) {
        return requestRepository.findByCitizenAfm(afm);
    }

    public void createAppointment(Appointment appointment) {
        if (appointment.getDateTime().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Cannot schedule an appointment in the past.");
        }

        if (appointmentRepository.existsByCitizenAfmAndDateTime(appointment.getCitizenAfm(),
                appointment.getDateTime())) {
            throw new RuntimeException("You already have an appointment at this time.");
        }

        if (appointmentRepository.existsByServiceTypeAndDateTime(appointment.getServiceType(),
                appointment.getDateTime())) {
            throw new RuntimeException("This service is already booked for the selected time.");
        }

        appointmentRepository.save(appointment);
    }

    public List<Appointment> getAppointmentsByCitizenAfm(String afm) {
        return appointmentRepository.findByCitizenAfm(afm);
    }
}
