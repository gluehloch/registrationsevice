package de.awtools.registration.jwt;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Security;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.junit.jupiter.api.Test;

/**
 * Simulate JWT lifecycle. Ready private key. Generate JWT.
 */
public class SimulateJwtLifecycleTest {

	@Test
	public void readPrivateKey() throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
		InputStream privateKeyIs = SimulateJwtLifecycleTest.class.getResourceAsStream("register_id_rsa");
		assertThat(privateKeyIs).isNotNull();

		byte[] targetArray = new byte[privateKeyIs.available()];
		privateKeyIs.read(targetArray);

		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(targetArray);
		PrivateKey privateKey = keyFactory.generatePrivate(privateKeySpec);

		assertThat(privateKey).isNotNull();
	}
	
	@Test
	public void readPrivateKey2() throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
		InputStream privateKeyIs = SimulateJwtLifecycleTest.class.getResourceAsStream("register_id_rsa_pkcs8");
		assertThat(privateKeyIs).isNotNull();

		byte[] targetArray = new byte[privateKeyIs.available()];
		privateKeyIs.read(targetArray);

		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(targetArray);
		PrivateKey privateKey = keyFactory.generatePrivate(privateKeySpec);

		assertThat(privateKey).isNotNull();
	}

	@Test
	public void xxx() throws IOException, NoSuchAlgorithmException, InvalidKeySpecException, NoSuchProviderException {

		Security.addProvider(new BouncyCastleProvider());

//        InputStream privateKeyIs = SimulateJwtLifecycleTest.class
//                .getResourceAsStream("register_id_rsa");
//        assertThat(privateKeyIs).isNotNull();
//
//        byte[] targetArray = new byte[privateKeyIs.available()];
//        privateKeyIs.read(targetArray);

//        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(targetArray);
		KeyFactory keyFactory = KeyFactory.getInstance("RSA", "BC");

		PrivateKey privateKey = generatePrivateKey(keyFactory);

		// PrivateKey privateKey1 = keyFactory.generatePrivate(spec);

		// PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(targetArray);
		// PrivateKey privateKey = keyFactory.generatePrivate(privateKeySpec);

		assertThat(privateKey).isNotNull();
	}

	private static PrivateKey generatePrivateKey(KeyFactory factory) throws InvalidKeySpecException, IOException {

		PemFile pemFile = new PemFile();
		byte[] content = pemFile.getPemObject().getContent();
		PKCS8EncodedKeySpec privKeySpec = new PKCS8EncodedKeySpec(content);
		return factory.generatePrivate(privKeySpec);
	}

	@Test
	public void generatePrivateKey() {
		try {
			KeyPairGenerator keyGen = KeyPairGenerator.getInstance("DSA", "SUN");

			// Initialize KeyPairGenerator.
			SecureRandom random = SecureRandom.getInstance("SHA1PRNG", "SUN");
			keyGen.initialize(1024, random);

			// Generate Key Pairs, a private key and a public key.
			KeyPair keyPair = keyGen.generateKeyPair();
			PrivateKey privateKey = keyPair.getPrivate();
			PublicKey publicKey = keyPair.getPublic();

			Base64.Encoder encoder = Base64.getEncoder();
			System.out.println("privateKey: " + encoder.encodeToString(privateKey.getEncoded()));
			System.out.println("publicKey: " + encoder.encodeToString(publicKey.getEncoded()));
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchProviderException e) {
			e.printStackTrace();
		}
	}

}
