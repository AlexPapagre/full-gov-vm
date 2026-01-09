package gr.hua.dit.mycitygov.core.model;

import java.time.LocalDateTime;

import gr.hua.dit.mycitygov.core.service.model.ServiceType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "appointment")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Appointment {
    @Id
    @GeneratedValue
    @Column(name = "id")
    private Long id;

    @Column(name = "citizen_afm", nullable = false)
    private String citizenAfm;

    @Enumerated(EnumType.STRING)
    @Column(name = "service_type", nullable = false)
    private ServiceType serviceType;

    @Column(name = "date_time", nullable = false)
    private LocalDateTime dateTime;

    @Column(name = "accepted", nullable = false)
    private boolean accepted = false;
}
