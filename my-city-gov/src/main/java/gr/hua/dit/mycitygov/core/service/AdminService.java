package gr.hua.dit.mycitygov.core.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import gr.hua.dit.mycitygov.core.model.Appointment;
import gr.hua.dit.mycitygov.core.model.Request;
import gr.hua.dit.mycitygov.core.port.NocGovPort;
import gr.hua.dit.mycitygov.core.port.NocSmsPort;
import gr.hua.dit.mycitygov.core.repository.AppointmentRepository;
import gr.hua.dit.mycitygov.core.repository.RequestRepository;
import gr.hua.dit.mycitygov.core.service.model.ServiceType;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminService {
    private final RequestRepository requestRepository;
    private final AppointmentRepository appointmentRepository;
    private final StatisticsService statisticsService;
    private final NocGovPort nocGovPort;
    private final NocSmsPort nocSmsPort;

    public List<Request> getAllRequests() {
        return requestRepository.findAll();
    }

    public void updateRequestServiceType(Long id, ServiceType newServiceType) {
        Request request = requestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Request not found!"));

        request.setServiceType(newServiceType);
        requestRepository.save(request);

        String phone = nocGovPort.getPhoneByAfm(request.getCitizenAfm());
        String message = "Your request with ID " + id + " got redirected to service '" + newServiceType.getLabel()
                + "'.";

        nocSmsPort.sendMessage(phone, message);
    }

    public List<Appointment> getAllAppointments() {
        return appointmentRepository.findAll();
    }

    public void updateAppointmentServiceType(Long id, ServiceType newServiceType) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Appointment not found!"));

        if (appointmentRepository.existsByServiceTypeAndDateTime(newServiceType, appointment.getDateTime())) {
            throw new RuntimeException("This service is already booked for the selected time.");
        }

        appointment.setServiceType(newServiceType);
        appointmentRepository.save(appointment);

        String phone = nocGovPort.getPhoneByAfm(appointment.getCitizenAfm());
        String message = "Your appointment with ID " + id + " got redirected to service '" + newServiceType.getLabel()
                + "'.";

        nocSmsPort.sendMessage(phone, message);
    }

    public long getTotalRequests() {
        return statisticsService.getTotalRequests();
    }

    public Map<ServiceType, Long> getRequestCountByService() {
        return statisticsService.getRequestCountByService();
    }

    public long getTotalAppointments() {
        return statisticsService.getTotalAppointments();
    }

    public Map<ServiceType, Long> getAppointmentCountByService() {
        return statisticsService.getAppointmentCountByService();
    }
}
