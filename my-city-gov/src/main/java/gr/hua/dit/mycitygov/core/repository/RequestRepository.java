package gr.hua.dit.mycitygov.core.repository;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import gr.hua.dit.mycitygov.core.model.Request;
import gr.hua.dit.mycitygov.core.service.model.ServiceType;

@Repository
public interface RequestRepository extends JpaRepository<Request, Long> {

    List<Request> findByCitizenAfm(String afm);

    List<Request> findByServiceTypeIn(Set<ServiceType> serviceTypes);
}
