package de.awtools.registration.user;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserAccountRepository extends CrudRepository<UserAccountEntity, Long> {

    UserAccountEntity findByNickname(String nickname);

}
