package de.awtools.registration.user;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApplicationRepository extends CrudRepository<ApplicationEntity, Long> {

    Optional<ApplicationEntity> findByName(String name);

}
