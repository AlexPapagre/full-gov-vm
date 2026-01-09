package gr.hua.dit.mycitygov.core.service;

import org.springframework.stereotype.Service;

import gr.hua.dit.mycitygov.core.port.NocGovPort;
import gr.hua.dit.mycitygov.core.port.NocSmsPort;
import gr.hua.dit.mycitygov.core.service.model.Person;
import gr.hua.dit.mycitygov.nocsms.dto.VerifyOtpResponse;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final NocGovPort nocGovPort;
    private final NocSmsPort nocSmsPort;

    public String sendOtp(String afm) {
        String phone = nocGovPort.getPhoneByAfm(afm);
        nocSmsPort.sendOtp(phone);

        return phone;
    }

    public Person verifyOtpAndGetPerson(String afm, String otp) {
        String phone = nocGovPort.getPhoneByAfm(afm);
        VerifyOtpResponse response = nocSmsPort.verifyOtp(phone, otp);

        if (!response.success()) {
            return null;
        }

        return nocGovPort.getPersonByAfm(afm);
    }
}
