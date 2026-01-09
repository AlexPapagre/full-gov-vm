package gr.hua.dit.mycitygov.web.ui;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import gr.hua.dit.mycitygov.core.service.AuthService;
import gr.hua.dit.mycitygov.core.service.PersonSessionService;
import gr.hua.dit.mycitygov.core.service.model.Person;
import gr.hua.dit.mycitygov.core.util.PersonUtils;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthController.class);

    private final AuthService authService;
    private final PersonSessionService personSessionService;

    @GetMapping("/login")
    public String loginPage(Model model) {
        Person person = personSessionService.getLoggedInPerson();
        if (person != null) {
            return "redirect:/";
        }

        return "auth/login";
    }

    @GetMapping("/otp")
    public String otpPage(@ModelAttribute("afm") String afm, @ModelAttribute("phone") String phone) {
        if (afm == null || afm.isBlank() || phone == null || phone.isBlank()) {
            return "redirect:/auth/login";
        }

        return "auth/otp";
    }

    @GetMapping("/send-otp")
    public String sendOtpNotFound() {
        throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/send-otp")
    public String sendOtp(@RequestParam String afm, RedirectAttributes redirectAttributes) {
        if (afm == null || afm.isBlank() || !afm.matches("\\d{9}")) {
            redirectAttributes.addFlashAttribute("error", "Please enter a valid 9-digit AFM.");

            return "redirect:/auth/login";
        }

        try {
            String phone = authService.sendOtp(afm);

            redirectAttributes.addFlashAttribute("afm", afm);
            redirectAttributes.addFlashAttribute("phone", PersonUtils.maskPhone(phone));

            return "redirect:/auth/otp";
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/auth/login";
        }
    }

    @GetMapping("/verify-otp")
    public String verifyOtpNotFound() {
        throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/verify-otp")
    public String verifyOtp(@RequestParam String afm, @RequestParam String phone, @RequestParam String otp,
            RedirectAttributes redirectAttributes) {
        if (otp == null || otp.isBlank() || !otp.matches("\\d{6}")) {
            redirectAttributes.addFlashAttribute("afm", afm);
            redirectAttributes.addFlashAttribute("phone", phone);
            redirectAttributes.addFlashAttribute("error", "Invalid code format.");

            return "redirect:/auth/otp";
        }

        LOGGER.info("Sending OTP {} to PHONE {}.", otp, phone);

        try {
            Person person = authService.verifyOtpAndGetPerson(afm, otp);
            if (person == null) {
                redirectAttributes.addFlashAttribute("afm", afm);
                redirectAttributes.addFlashAttribute("phone", phone);
                redirectAttributes.addFlashAttribute("error", "Invalid verification code.");

                LOGGER.warn("OTP {} didn't verify PHONE {}.", otp, phone);

                return "redirect:/auth/otp";
            }

            personSessionService.setLoggedInPerson(person);
            LOGGER.info("Person with AFM {} logged in successfully!", afm);

            return "redirect:/";
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("afm", afm);
            redirectAttributes.addFlashAttribute("phone", phone);
            redirectAttributes.addFlashAttribute("error", e.getMessage());

            return "redirect:/auth/otp";
        }

    }

    @GetMapping("/logout")
    public String logout(RedirectAttributes redirectAttributes) {
        if (!personSessionService.isLoggedIn()) {
            return "redirect:/";
        }

        personSessionService.logout();
        LOGGER.info("Person logged out successfully!");

        redirectAttributes.addFlashAttribute("message", "You have been logged out successfully.");

        return "redirect:/auth/login";
    }
}
