package de.awtools.registration.jwt;

import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemReader;

import java.io.*;

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
