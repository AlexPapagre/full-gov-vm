package gr.hua.dit.mycitygov.core.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import gr.hua.dit.mycitygov.core.model.Appointment;
import gr.hua.dit.mycitygov.core.service.model.ServiceType;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    List<Appointment> findByServiceTypeIn(Set<ServiceType> serviceTypes);

    List<Appointment> findByCitizenAfm(String afm);

    boolean existsByCitizenAfmAndDateTime(String citizenAfm, LocalDateTime dateTime);

    boolean existsByServiceTypeAndDateTime(ServiceType serviceType, LocalDateTime dateTime);
}
