package de.awtools.registration.authentication;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import de.awtools.registration.register.RegistrationEntity;

@Repository
public interface AuthenticationRepository extends CrudRepository<RegistrationEntity, Long> {
}
