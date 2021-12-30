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
        return Password.of(passwordEncoder.encode(decodedPassword.get()));
    }

    public boolean validate(Password decodedPassword, Password encodedPassword) {
        return passwordEncoder.matches(decodedPassword.get(), encodedPassword.get());
    }

    PasswordEncoder unwrap() {
        return passwordEncoder;
    }
    
}
