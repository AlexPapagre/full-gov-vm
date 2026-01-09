package gr.hua.dit.nocgov.core.dto;

import java.util.Set;

import gr.hua.dit.nocgov.core.model.PersonType;
import gr.hua.dit.nocgov.core.model.ServiceType;

public record PersonResponse(
        String afm,
        String amka,
        String firstName,
        String lastName,
        String phone,
        PersonType type,
        Set<ServiceType> serviceTypes) {
}
