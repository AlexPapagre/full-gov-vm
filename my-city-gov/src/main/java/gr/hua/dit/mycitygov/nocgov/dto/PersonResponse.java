package gr.hua.dit.mycitygov.nocgov.dto;

import java.util.Set;

import gr.hua.dit.mycitygov.core.service.model.PersonType;
import gr.hua.dit.mycitygov.core.service.model.ServiceType;

public record PersonResponse(
        String afm,
        String amka,
        String firstName,
        String lastName,
        String phone,
        PersonType type,
        Set<ServiceType> serviceTypes) {
}
