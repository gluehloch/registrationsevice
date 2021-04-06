package de.awtools.registration.authentication;

import de.awtools.registration.register.RegistrationEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthenticationRepository extends CrudRepository<RegistrationEntity, Long> {
}
