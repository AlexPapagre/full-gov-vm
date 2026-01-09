package gr.hua.dit.nocsmsverification.core.service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import gr.hua.dit.nocsmsverification.core.dto.VerifyOtpResponse;
import gr.hua.dit.nocsmsverification.core.util.CodeGenerator;

@Service
public class SmsService {
    private static final Logger LOGGER = LoggerFactory.getLogger(SmsService.class);

    private final Map<String, String> codes = new ConcurrentHashMap<>();

    public void sendMessage(String phone, String message) {
        LOGGER.warn("MOCK SMS SENT -> Phone: {} | Message: {}", phone, message);
    }

    public void sendOtp(String phone) {
        String otp = CodeGenerator.generate6DigitCode();
        codes.put(phone, otp);

        LOGGER.warn("MOCK SMS SENT -> Phone: {} | CODE: {}", phone, otp);
    }

    public VerifyOtpResponse verifyOtp(String phone, String otp) {
        String stored = codes.get(phone);

        if (stored == null) {
            LOGGER.warn("MOCK SMS VERIFICATION -> Code not found for phone {}.", phone);
            return new VerifyOtpResponse(false, "No verification code found for that phone number.");
        }

        if (!stored.equals(otp)) {
            LOGGER.warn("MOCK SMS VERIFICATION -> Invalid code for phone {}.", phone);
            return new VerifyOtpResponse(false, "Invalid verification code.");
        }

        codes.remove(phone);
        LOGGER.warn("MOCK SMS VERIFICATION -> Phone {} verified successfully.", phone);
        return new VerifyOtpResponse(true, "Phone verified successfully!");
    }
}
