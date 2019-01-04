package de.awtools.registration;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RegistrationRepository
        extends CrudRepository<Registration, Long> {

    Registration findByNickname(String nickname);
    
    Registration findByEmail(String email);

    Registration findByToken(Token token);
    
}
