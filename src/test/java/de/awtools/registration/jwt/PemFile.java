package de.awtools.registration.jwt;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemReader;

public class PemFile {

    private PemObject pemObject;

    public PemFile() throws IOException {
        InputStream privateKeyIs = SimulateJwtLifecycleTest.class
                .getResourceAsStream("register_id_rsa");

        try (PemReader pemReader = new PemReader(new InputStreamReader(privateKeyIs))) {
            this.pemObject = pemReader.readPemObject();
        }
    }

    public PemObject getPemObject() {
        return pemObject;
    }

}
