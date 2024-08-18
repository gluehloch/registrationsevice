package de.awtools.registration.jwt;

import static org.assertj.core.api.Assertions.assertThat;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;

import org.junit.jupiter.api.Test;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

/**
 * Generate public-private key pair.
 *
 * @author Andre Winkler
 */
public class PublicPrivateKeyTest {

    @Test
    public void generatePublicPrivateKeyPair() {
        // Erstellt einen asymmetrischen (public/private) Schluessel.
        KeyPair keyPair = Keys.keyPairFor(SignatureAlgorithm.RS256);
        PrivateKey privateKey = keyPair.getPrivate();
        PublicKey publicKey = keyPair.getPublic();

        assertThat(privateKey).isNotNull();
        assertThat(publicKey).isNotNull();
    }

}
