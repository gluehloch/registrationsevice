package de.awtools.registration.jwt;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.security.Key;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.function.Function;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.IncorrectClaimException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;

/**
 * Test JWT.
 *
 * @author Andre Winkler
 */
public class JasonWebTokenTest {

    private Date now;
    private Date yesterday;
    private Date tomorrow;
    
    /**
     * Abstraktion fuer einen Schluessel. Allen Schluesseln ist folgendes gemeinsam:
     * <ul>
     *   <li>Encoding</li>
     *   <li>Algorithmus</li>
     * </ul>
     * Fuer mich: Kann ein 'public' oder ein 'private' Key sein.
     */
    private Key key;

    @BeforeEach
    public void before() {
        now = Date.from(LocalDate.now().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
        yesterday = Date.from(LocalDate.now().minusDays(1).atStartOfDay()
                .atZone(ZoneId.systemDefault())
                .toInstant());
        tomorrow = Date.from(LocalDate.now().plusDays(1).atStartOfDay()
                .atZone(ZoneId.systemDefault())
                .toInstant());

        // Legt einen sogenannten 'Secret-Key' an.
        key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    }

    // -- TODO Ist das sinnvoll? Ueber eine Function den Wert ermitteln?
    public Date extractExpiration(String token, String key) {
        return extractClaim(token, key, Claims::getExpiration);
    }

    // -- Das ist die Alternative zu #extractExpiration
    public Date extractExpiration2(String token, String key) {
        Claims extractAllClaims = extractAllClaims(token, key);
        Date expiration = extractAllClaims.getExpiration();
        return expiration;
    }

    public <T> T extractClaim(String token, String key, Function<Claims, T> claimResolver) {
        final Claims claims = extractAllClaims(token, key);
        return claimResolver.apply(claims);
    }

    
    public static Claims extractAllClaims(String token, String key) {
        return Jwts.parser().setSigningKey(key).parseClaimsJws(token).getBody();
    }
    // -- Ende
    
    @Test
    public void createValidJsonWebToken() {
        // Mit diesem Befehl kann man den Schluessel in einen String umwandeln und
        // abspeichern. Dabei nicht vergessen: Das ist der geheime Schluessel.
        
        // TODO: Wie funktioiert das mit einem public/private key?
        
        String secretString = Encoders.BASE64.encode(key.getEncoded());
        assertThat(secretString).isNotBlank();

        String jws = Jwts.builder()
                .setHeaderParam("betoffice", "1.0")
                .setSubject("Frosch")
                .setIssuer("issuer")
                .setIssuedAt(now)
                .setExpiration(tomorrow)
                .signWith(key)
                .compact();

        Claims claims = Jwts.parser().setSigningKey(key).parseClaimsJws(jws).getBody();

        assertThat(claims.getSubject()).isEqualTo("Frosch");
        assertThat(claims.getIssuedAt()).isEqualTo(now);
        assertThat(claims.getIssuer()).isEqualTo("issuer");

        // IncorrectClaimException: 'Peter' statt 'Frosch'.
        assertThatThrownBy(() -> Jwts.parser()
                .requireSubject("Peter").setSigningKey(key)
                .parseClaimsJws(jws))
                        .isInstanceOf(IncorrectClaimException.class);

        // MissingClaimException: Ein Issuer wurde nicht angelegt.
        assertThatThrownBy(() -> Jwts.parser()
                .requireIssuer("MissingIssuer").setSigningKey(key)
                .parseClaimsJws(jws))
                        .isInstanceOf(IncorrectClaimException.class);
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
                        .isInstanceOf(ExpiredJwtException.class);
    }

}
