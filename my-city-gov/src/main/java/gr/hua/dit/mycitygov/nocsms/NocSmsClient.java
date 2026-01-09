package gr.hua.dit.mycitygov.nocsms;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import gr.hua.dit.mycitygov.core.port.NocSmsPort;
import gr.hua.dit.mycitygov.nocsms.dto.SendMessageRequest;
import gr.hua.dit.mycitygov.nocsms.dto.SendOtpRequest;
import gr.hua.dit.mycitygov.nocsms.dto.VerifyOtpRequest;
import gr.hua.dit.mycitygov.nocsms.dto.VerifyOtpResponse;

@Component
public class NocSmsClient implements NocSmsPort {
    private static final Logger LOGGER = LoggerFactory.getLogger(NocSmsClient.class);

    private final RestTemplate restTemplate;
    private final String baseUrl;

    public NocSmsClient(RestTemplate restTemplate, @Value("${nocsms.base-url}") String baseUrl) {
        this.restTemplate = restTemplate;
        this.baseUrl = baseUrl;
    }

    @Override
    public void sendMessage(String phone, String message) {
        try {
            SendMessageRequest request = new SendMessageRequest(phone, message);
            restTemplate.postForObject(baseUrl + "/send", request, SendMessageRequest.class);
        } catch (ResourceAccessException e) {
            LOGGER.error("NOC SMS Server is not available!");
            throw new RuntimeException(
                    "We are unable to connect to the SMS Server at this time. Please try again later.");
        } catch (Exception e) {
            LOGGER.error("Unexpected error while connecting to NOC SMS Server.");
            throw new RuntimeException("An unexpected error occurred. Please try again later.");
        }
    }

    @Override
    public void sendOtp(String phone) {
        try {
            SendOtpRequest request = new SendOtpRequest(phone);
            restTemplate.postForObject(baseUrl + "/otp/send", request, VerifyOtpResponse.class);
        } catch (ResourceAccessException e) {
            LOGGER.error("NOC SMS Server is not available!");
            throw new RuntimeException(
                    "We are unable to connect to the SMS Server at this time. Please try again later.");
        } catch (Exception e) {
            LOGGER.error("Unexpected error while connecting to NOC SMS Server.");
            throw new RuntimeException("An unexpected error occurred. Please try again later.");
        }
    }

    @Override
    public VerifyOtpResponse verifyOtp(String phone, String otp) {
        try {
            VerifyOtpRequest request = new VerifyOtpRequest(phone, otp);
            return restTemplate.postForObject(baseUrl + "/otp/verify", request, VerifyOtpResponse.class);
        } catch (ResourceAccessException e) {
            LOGGER.error("noc-sms server is not available!");
            throw new RuntimeException(
                    "We are unable to connect to the sms server at this time. Please try again later.");
        } catch (Exception e) {
            LOGGER.error("Unexpected error", e);
            throw new RuntimeException("An unexpected error occurred. Please try again later.");
        }
    }
}
