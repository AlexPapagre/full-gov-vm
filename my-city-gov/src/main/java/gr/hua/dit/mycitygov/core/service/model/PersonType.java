package gr.hua.dit.mycitygov.core.service.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PersonType {
    ADMIN("Admin"),
    EMPLOYEE("Employee"),
    CITIZEN("Citizen");

    private final String label;
}
