package gr.hua.dit.nocgov.config;

import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

import org.springframework.stereotype.Component;

import gr.hua.dit.nocgov.core.model.Person;
import gr.hua.dit.nocgov.core.model.PersonType;
import gr.hua.dit.nocgov.core.model.ServiceType;
import gr.hua.dit.nocgov.core.repository.PersonRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DataInitializer {
    private final AtomicBoolean initialized = new AtomicBoolean(false);
    private final PersonRepository personRepository;

    @PostConstruct
    public void init() {
        boolean alreadyInitialized = initialized.getAndSet(true);

        if (alreadyInitialized) {
            return;
        }

        List<Person> people = List.of(
                new Person("100000001", "01010100001", "John", "Doe", "6910000001", PersonType.CITIZEN, null),
                new Person("100000002", "01010200002", "Jane", "Smith", "6910000002", PersonType.CITIZEN, null),
                new Person("100000003", "01010300003", "Mike", "Brown", "6910000003", PersonType.CITIZEN, null),

                new Person("200000001", "02020100001", "Alice", "Johnson", "6920000001", PersonType.EMPLOYEE,
                        Set.of(ServiceType.CERTIFICATE_OF_PERMANENT_RESIDENCE, ServiceType.SIDEWALK_OCCUPANCY_PERMIT)),
                new Person("200000002", "02020200002", "Bob", "Williams", "6920000002", PersonType.EMPLOYEE,
                        Set.of(ServiceType.SIDEWALK_OCCUPANCY_PERMIT)),
                new Person("200000003", "02020300003", "Charlie", "Davis", "6920000003", PersonType.EMPLOYEE,
                        Set.of(ServiceType.STREET_LIGHTING_ISSUE_REPORT)),

                new Person("300000001", "03030100001", "Eve", "Miller", "6930000001", PersonType.ADMIN, null),
                new Person("300000002", "03030200002", "Frank", "Wilson", "6930000002", PersonType.ADMIN, null),
                new Person("300000003", "03030300003", "Grace", "Moore", "6930000003", PersonType.ADMIN, null));
        personRepository.saveAll(people);
    }
}
