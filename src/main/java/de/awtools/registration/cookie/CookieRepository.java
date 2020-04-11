package de.awtools.registration.cookie;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CookieRepository extends CrudRepository<Cookie, Long> {
}
