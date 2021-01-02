package de.awtools.registration.user;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserAccountRepository extends CrudRepository<UserAccountEntity, Long> {

    Optional<UserAccountEntity> findByNickname(String nickname);

}
