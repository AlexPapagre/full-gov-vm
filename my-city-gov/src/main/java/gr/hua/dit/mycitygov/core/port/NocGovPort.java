package gr.hua.dit.mycitygov.core.port;

import gr.hua.dit.mycitygov.core.service.model.Person;

public interface NocGovPort {

    String getPhoneByAfm(String afm);

    Person getPersonByAfm(String afm);
}
