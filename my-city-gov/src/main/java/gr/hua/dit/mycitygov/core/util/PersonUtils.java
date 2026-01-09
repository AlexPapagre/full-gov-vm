package gr.hua.dit.mycitygov.core.util;

import gr.hua.dit.mycitygov.core.service.model.Person;
import gr.hua.dit.mycitygov.nocgov.dto.PersonResponse;

public final class PersonUtils {

    public static String maskPhone(String phone) {
        if (phone == null || phone.length() < 5) {
            return phone;
        }

        int startCount = 3;
        int endCount = 2;

        int starCount = phone.length() - (startCount + endCount);

        return phone.substring(0, startCount) + "*".repeat(starCount) + phone.substring(phone.length() - endCount);
    }

    public static Person personResponseToPerson(PersonResponse response) {
        return new Person(
                response.afm(),
                response.amka(),
                response.firstName(),
                response.lastName(),
                response.phone(),
                response.type(),
                response.serviceTypes());
    }
}
