package gr.hua.dit.mycitygov.core.port;

import gr.hua.dit.mycitygov.nocsms.dto.VerifyOtpResponse;

public interface NocSmsPort {

    void sendMessage(String phone, String message);

    void sendOtp(String phone);

    VerifyOtpResponse verifyOtp(String phone, String otp);
}
