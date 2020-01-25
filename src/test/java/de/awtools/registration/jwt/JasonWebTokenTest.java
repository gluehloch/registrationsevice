package de.awtools.registration.jwt;

import static org.assertj.core.api.Assertions.assertThat;

import java.security.Key;

import org.junit.jupiter.api.Test;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

/**
 * Test JWT.
 * 
 * @author Andre Winkler
 */
public class JasonWebTokenTest {

	@Test
	public void jsonWebToken() {
		Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
		String jws = Jwts.builder().setSubject("Joe").signWith(key).compact();
		assertThat(Jwts.parser().setSigningKey(key).parseClaimsJws(jws).getBody().getSubject()).isEqualTo("Joe");
	}

}
