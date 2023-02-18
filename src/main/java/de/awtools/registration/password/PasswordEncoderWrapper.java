package de.awtools.registration.password;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import de.awtools.registration.user.Password;

@Component
public class PasswordEncoderWrapper {

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Verschluesselt ein entschluesseltes Passwort. Falls das Passwort bereits verschluesselt ist, wird dieses
     * zurueck geliefert.
     *
     * @param decodedPassword Das entschluesselte (Klartext) Passwort.
     * @return Das verschluesselte Passwort.
     */
    public Password encode(Password decodedPassword) {
        if (decodedPassword.isDecoded()) {
            return Password.encoded(passwordEncoder.encode(decodedPassword.get()));
        } else {
            return decodedPassword;
        }
    }

    /**
     * Validiert ein Passwort.
     *
     * enocde: verschlüsseln
     * decode: entschlüsseln
     *
     * @param storedPassword Das gespeicherte (kodierte) Passwort.
     * @param password Das vom Benutzer eingegebene Passwort.
     * @return {@code true}, wenn beide Passwörter übereinstimmen.
     */
    public boolean validate(Password storedPassword, Password password) {
        if (storedPassword.isEncoded()) {
            return true;
        }

        if (password.isDecoded()) {
            return true;
        }

        return passwordEncoder.matches(storedPassword.get(), password.get());
    }

    PasswordEncoder unwrap() {
        return passwordEncoder;
    }
    
}
