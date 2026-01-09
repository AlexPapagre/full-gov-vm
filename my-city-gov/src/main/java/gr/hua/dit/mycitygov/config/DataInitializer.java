package gr.hua.dit.mycitygov.config;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import org.springframework.stereotype.Component;

import gr.hua.dit.mycitygov.core.model.Client;
import gr.hua.dit.mycitygov.core.repository.ClientRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DataInitializer {
    private final AtomicBoolean initialized = new AtomicBoolean(false);
    private final ClientRepository clientRepository;

    @PostConstruct
    public void init() {
        boolean alreadyInitialized = initialized.getAndSet(true);

        if (alreadyInitialized) {
            return;
        }

        List<Client> clients = List.of(
                new Client(null, "client01", "secret", "INTEGRATION_READ,INTEGRATION_WRITE"),
                new Client(null, "client02", "secret", "INTEGRATION_READ)"));
        clientRepository.saveAll(clients);
    }
}
