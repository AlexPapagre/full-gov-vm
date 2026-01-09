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
@Table(name = "request")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Request {
    @Id
    @GeneratedValue
    @Column(name = "id")
    private Long id;

    @Column(name = "citizen_afm", nullable = false)
    private String citizenAfm;

    @Enumerated(EnumType.STRING)
    @Column(name = "service_type", nullable = false)
    private ServiceType serviceType;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private RequestStatus status;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "note", nullable = true)
    private String note;

    @Column(name = "response", nullable = true)
    private String response;

    @Column(name = "file_name", nullable = true)
    private String fileName;

    private String originalFileName;
}
