package de.awtools.registration.jwt;

import io.jsonwebtoken.IncorrectClaimException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MissingClaimException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.security.Key;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Test JWT.
 *
 * @author Andre Winkler
 */
public class JasonWebTokenTest {

    private Date yesterday;
    private Date tomorrow;
    private Key key;

    @BeforeEach
    public void before() {
        yesterday = Date.from(LocalDate.now().minusDays(1).atStartOfDay()
                .atZone(ZoneId.systemDefault())
                .toInstant());
        tomorrow = Date.from(LocalDate.now().plusDays(1).atStartOfDay()
                .atZone(ZoneId.systemDefault())
                .toInstant());

        key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    }

    @Test
    public void generatePublicPrivateKeyPair() {
        // Erstellt einen asymmetrischen (public/private) Schluessel.
        KeyPair keyPair = Keys.keyPairFor(SignatureAlgorithm.RS256);
        PrivateKey privateKey = keyPair.getPrivate();
        PublicKey publicKey = keyPair.getPublic();

        assertThat(privateKey).isNotNull();
        assertThat(publicKey).isNotNull();
    }

    @Test
    public void createValidJsonWebToken() {
        // Mit diesem Befehl kann man den Schluessel in einen String umwandeln und abspeichern.
        // Dabei nicht vergessen: Das ist der geheime Schluessel.
        String secretString = Encoders.BASE64.encode(key.getEncoded());
        assertThat(secretString).isNotBlank();

        String jws = Jwts.builder()
                .setHeaderParam("betoffice", "1.0")
                .setSubject("Frosch")
                .setExpiration(tomorrow)
                .signWith(key)
                .compact();

        assertThat(Jwts.parser()
                .setSigningKey(key)
                .parseClaimsJws(jws)
                .getBody()
                .getSubject()).isEqualTo("Frosch");

        // IncorrectClaimException: 'Peter' statt 'Frosch'.
        assertThatThrownBy(() -> Jwts.parser()
                .requireSubject("Peter").setSigningKey(key)
                .parseClaimsJws(jws))
                .isInstanceOf(IncorrectClaimException.class);

        // MissingClaimException: Ein Issuer wurde nicht angelegt.
        assertThatThrownBy(() -> Jwts.parser()
                .requireIssuer("MissingIssuer").setSigningKey(key)
                .parseClaimsJws(jws))
                .isInstanceOf(MissingClaimException.class);
    }

    @Test
    public void createExpiredJwt() {
        String jws = Jwts.builder()
                .setHeaderParam("betoffice", "1.0")
                .setSubject("Joe")
                .setExpiration(yesterday)
                .signWith(key)
                .compact();

        assertThatThrownBy(() -> Jwts.parser()
                .setSigningKey(key)
                .parseClaimsJws(jws)
                .getBody()
                .getSubject())
                .isInstanceOf(io.jsonwebtoken.ExpiredJwtException.class);
    }

}
