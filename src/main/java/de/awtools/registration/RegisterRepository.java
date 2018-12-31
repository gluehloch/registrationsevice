package de.awtools.registration;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RegisterRepository
        extends CrudRepository<Registration, Long> {

    Registration findByNickname(String nickname);

}
