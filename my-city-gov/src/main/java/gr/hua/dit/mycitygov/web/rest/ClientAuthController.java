package gr.hua.dit.mycitygov.web.rest;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import gr.hua.dit.mycitygov.core.security.ClientDetails;
import gr.hua.dit.mycitygov.core.security.ClientDetailsService;
import gr.hua.dit.mycitygov.core.security.JwtService;
import gr.hua.dit.mycitygov.web.rest.model.ClientTokenRequest;
import gr.hua.dit.mycitygov.web.rest.model.ClientTokenResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(value = "/api/v1/auth", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class ClientAuthController {
    private final ClientDetailsService clientDetailsService;
    private final JwtService jwtService;

    @PostMapping("/client-tokens")
    public ClientTokenResponse clientToken(@RequestBody ClientTokenRequest request) {
        ClientDetails client = clientDetailsService.authenticate(request.clientId(), request.clientSecret())
                .orElse(null);

        if (client == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid client credentials.");
        }

        String token = jwtService.issue("client:" + client.id(), client.roles());
        return new ClientTokenResponse(token, "Bearer", 60 * 60);
    }
}
