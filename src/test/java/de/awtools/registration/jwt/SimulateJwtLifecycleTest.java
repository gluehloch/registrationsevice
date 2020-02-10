package de.awtools.registration.jwt;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Simulate JWT lifecycle. Ready private key. Generate JWT.
 */
public class SimulateJwtLifecycleTest {

    @Test
    public void readPrivateKey() throws IOException, NoSuchAlgorithmException,
            InvalidKeySpecException {

        InputStream privateKeyIs = SimulateJwtLifecycleTest.class
                .getResourceAsStream("register_id_rsa");
        assertThat(privateKeyIs).isNotNull();

        byte[] targetArray = new byte[privateKeyIs.available()];
        privateKeyIs.read(targetArray);

        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(
                targetArray);
        PrivateKey privateKey = keyFactory.generatePrivate(privateKeySpec);

        assertThat(privateKey).isNotNull();
    }

    @Test
    public void xxx() throws IOException, NoSuchAlgorithmException,
            InvalidKeySpecException {

        InputStream privateKeyIs = SimulateJwtLifecycleTest.class
                .getResourceAsStream("register_id_rsa");
        assertThat(privateKeyIs).isNotNull();

        byte[] targetArray = new byte[privateKeyIs.available()];
        privateKeyIs.read(targetArray);

        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(targetArray);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");

        PrivateKey privateKey1 = keyFactory.generatePrivate(spec);

//        PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(targetArray);
//        PrivateKey privateKey = keyFactory.generatePrivate(privateKeySpec);

        assertThat(privateKey1).isNotNull();
    }

}
