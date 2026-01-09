package gr.hua.dit.mycitygov.core.service.model;

import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Person {
    private String afm;
    private String amka;
    private String firstName;
    private String lastName;
    private String phone;
    private PersonType type;
    private Set<ServiceType> serviceTypes;
}
