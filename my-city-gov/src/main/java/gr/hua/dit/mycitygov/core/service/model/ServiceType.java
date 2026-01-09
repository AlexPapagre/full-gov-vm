package gr.hua.dit.mycitygov.core.service.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ServiceType {
    CERTIFICATE_OF_PERMANENT_RESIDENCE("Certificate of Permanent Residence"),
    SIDEWALK_OCCUPANCY_PERMIT("Sidewalk Occupancy Permit"),
    STREET_LIGHTING_ISSUE_REPORT("Street Lighting Issue Report");

    private final String label;
}
