package gr.hua.dit.mycitygov.core.security;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import gr.hua.dit.mycitygov.core.model.Client;
import gr.hua.dit.mycitygov.core.repository.ClientRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ClientDetailsService {
    private final ClientRepository clientRepository;

    public Optional<ClientDetails> authenticate(String id, String secret) {
        Client client = clientRepository.findByName(id).orElse(null);

        if (client == null) {
            return Optional.empty();
        }

        if (!Objects.equals(client.getSecret(), secret)) {
            return Optional.empty();
        }

        ClientDetails clientDetails = new ClientDetails(client.getName(),
                client.getSecret(),
                Arrays.stream(client.getRolesCsv().split(","))
                        .map(String::strip)
                        .map(String::toUpperCase)
                        .collect(Collectors.toSet()));

        return Optional.of(clientDetails);
    }
}
