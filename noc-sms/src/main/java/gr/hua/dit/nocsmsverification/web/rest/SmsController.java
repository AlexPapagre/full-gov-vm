package gr.hua.dit.nocsmsverification.web.rest;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import gr.hua.dit.nocsmsverification.core.dto.SendOtpRequest;
import gr.hua.dit.nocsmsverification.core.dto.SendMessageRequest;
import gr.hua.dit.nocsmsverification.core.dto.VerifyOtpRequest;
import gr.hua.dit.nocsmsverification.core.dto.VerifyOtpResponse;
import gr.hua.dit.nocsmsverification.core.service.SmsService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/sms")
public class SmsController {
    private final SmsService smsService;

    @PostMapping("send")
    public void sendMessage(@RequestBody SendMessageRequest request) {
        smsService.sendMessage(request.phone(), request.message());
    }

    @PostMapping("otp/send")
    public void sendOtp(@RequestBody SendOtpRequest request) {
        smsService.sendOtp(request.phone());
    }

    @PostMapping("otp/verify")
    public VerifyOtpResponse verifyOtp(@RequestBody VerifyOtpRequest request) {
        return smsService.verifyOtp(request.phone(), request.otp());
    }
}
