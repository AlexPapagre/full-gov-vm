package gr.hua.dit.mycitygov.core.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;

import gr.hua.dit.mycitygov.core.model.Appointment;
import gr.hua.dit.mycitygov.core.model.Request;
import gr.hua.dit.mycitygov.core.model.RequestStatus;
import gr.hua.dit.mycitygov.core.port.NocGovPort;
import gr.hua.dit.mycitygov.core.port.NocSmsPort;
import gr.hua.dit.mycitygov.core.repository.AppointmentRepository;
import gr.hua.dit.mycitygov.core.repository.RequestRepository;
import gr.hua.dit.mycitygov.core.service.model.ServiceType;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmployeeService {
    private final RequestRepository requestRepository;
    private final AppointmentRepository appointmentRepository;
    private final NocGovPort nocGovPort;
    private final NocSmsPort nocSmsPort;

    public List<Request> getRequestsByServices(Set<ServiceType> serviceTypes) {
        return requestRepository.findByServiceTypeIn(serviceTypes);
    }

    public Request getRequest(Long id) {
        return requestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Request not found!"));
    }

    public void updateRequest(Long id, RequestStatus status, String response) {
        Request request = getRequest(id);

        request.setStatus(status);
        request.setResponse(response);

        requestRepository.save(request);

        String phone = nocGovPort.getPhoneByAfm(request.getCitizenAfm());
        String message = "Your request with ID " + id + " got updated to status '" + status.getLabel() + "'.";

        nocSmsPort.sendMessage(phone, message);
    }

    public List<Appointment> getAppointmentsByServices(Set<ServiceType> serviceTypes) {
        return appointmentRepository.findByServiceTypeIn(serviceTypes);
    }

    public void updateAppointment(Long id, LocalDateTime newDateTime) {
        if (newDateTime.isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Cannot schedule an appointment in the past.");
        }

        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Appointment not found!"));

        if (appointmentRepository.existsByServiceTypeAndDateTime(
                appointment.getServiceType(), newDateTime)) {
            throw new RuntimeException("This service is already booked for the selected time.");
        }

        appointment.setDateTime(newDateTime);
        appointmentRepository.save(appointment);

        String[] dateTime = newDateTime.toString().split("T");

        String phone = nocGovPort.getPhoneByAfm(appointment.getCitizenAfm());
        String message = "Your appointment with ID " + id + " got updated for " + dateTime[0] + " " + dateTime[1] + ".";

        nocSmsPort.sendMessage(phone, message);
    }

    public void acceptAppointment(Long id) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Appointment not found!"));

        appointment.setAccepted(true);
        appointmentRepository.save(appointment);

        String phone = nocGovPort.getPhoneByAfm(appointment.getCitizenAfm());
        String message = "Your appointment with ID " + id + " got accepted.";

        nocSmsPort.sendMessage(phone, message);
    }

    public void cancelAppointment(Long id) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Appointment not found!"));

        appointmentRepository.deleteById(id);

        String phone = nocGovPort.getPhoneByAfm(appointment.getCitizenAfm());
        String message = "Your appointment with ID " + id + " got cancelled.";

        nocSmsPort.sendMessage(phone, message);
    }
}
