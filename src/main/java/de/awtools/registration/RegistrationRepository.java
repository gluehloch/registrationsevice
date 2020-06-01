package de.awtools.registration;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import de.awtools.registration.user.Email;

@Repository
public interface RegistrationRepository extends CrudRepository<Registration, Long> {

    Registration findByNickname(String nickname);
    
    // @Query("select r from Registration r where r.email.email = :email")
    Registration findByEmail(Email email);

    Registration findByToken(Token token);

    @Modifying
    @Query("delete from Registration r where r.email = :email")
    void deleteByEmail(@Param("email") Email email);
    
}
