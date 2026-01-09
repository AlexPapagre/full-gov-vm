package gr.hua.dit.nocgov.core.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import gr.hua.dit.nocgov.core.model.Person;

public interface PersonRepository extends JpaRepository<Person, String> {

    Optional<Person> findByAfm(String afm);
}
