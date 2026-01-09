package gr.hua.dit.mycitygov.nocgov;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import gr.hua.dit.mycitygov.core.port.NocGovPort;
import gr.hua.dit.mycitygov.core.service.model.Person;
import gr.hua.dit.mycitygov.core.util.PersonUtils;
import gr.hua.dit.mycitygov.nocgov.dto.PersonPhoneResponse;
import gr.hua.dit.mycitygov.nocgov.dto.PersonResponse;

@Component
public class NocGovClient implements NocGovPort {
    private static final Logger LOGGER = LoggerFactory.getLogger(NocGovClient.class);

    private final RestTemplate restTemplate;
    private final String baseUrl;

    public NocGovClient(RestTemplate restTemplate, @Value("${nocgov.base-url}") String baseUrl) {
        this.restTemplate = restTemplate;
        this.baseUrl = baseUrl;
    }

    @Override
    public String getPhoneByAfm(String afm) {
        try {
            PersonPhoneResponse response = restTemplate.getForObject(baseUrl + "/" + afm + "/phone",
                    PersonPhoneResponse.class);
            return response.phone();
        } catch (HttpClientErrorException.NotFound e) {
            throw new RuntimeException("We couldn’t find anyone with that AFM. Please check the number and try again.");
        } catch (ResourceAccessException e) {
            LOGGER.error("NOC GOV Server is not available!");
            throw new RuntimeException(
                    "We are unable to connect to the GOV Server at this time. Please try again later.");
        } catch (Exception e) {
            LOGGER.error("Unexpected error while connecting to NOC GOV Server.");
            throw new RuntimeException("An unexpected error occurred. Please try again later.");
        }
    }

    @Override
    public Person getPersonByAfm(String afm) {
        try {
            PersonResponse response = restTemplate.getForObject(baseUrl + "/" + afm, PersonResponse.class);
            return PersonUtils.personResponseToPerson(response);
        } catch (HttpClientErrorException.NotFound e) {
            throw new RuntimeException("We couldn’t find anyone with that AFM. Please check the number and try again.");
        } catch (ResourceAccessException e) {
            LOGGER.error("NOC GOV Server is not available!");
            throw new RuntimeException(
                    "We are unable to connect to the GOV Server at this time. Please try again later.");
        } catch (Exception e) {
            LOGGER.error("Unexpected error while connecting to NOC GOV Server.");
            throw new RuntimeException("An unexpected error occurred. Please try again later.");
        }
    }
}
