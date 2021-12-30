package de.awtools.registration.password;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import de.awtools.registration.user.Password;

@Component
public class PasswordEncoderWrapper {

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Password encode(Password decodedPassword) {
        if (decodedPassword.isDecoded()) {
            return Password.encoded(passwordEncoder.encode(decodedPassword.get()));
        } else {
            return decodedPassword;
        }
    }

    public boolean validate(Password decodedPassword, Password encodedPassword) {
        if (!decodedPassword.isDecoded()) {
            return false;
        }

        if (encodedPassword.isDecoded()) {
            return false;
        }

        return passwordEncoder.matches(decodedPassword.get(), encodedPassword.get());
    }

    PasswordEncoder unwrap() {
        return passwordEncoder;
    }
    
}
