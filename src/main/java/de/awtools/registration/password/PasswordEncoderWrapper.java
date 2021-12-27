package de.awtools.registration.password;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class PasswordEncoderWrapper {

    @Autowired
    private PasswordEncoder passwordEncoder;

    public String encode(String password) {
        return passwordEncoder.encode(password);
    }

    public boolean validate(String password, String encodedPassword) {
        return passwordEncoder.matches(password, encodedPassword);
    }

    PasswordEncoder unwrap() {
        return passwordEncoder;
    }
    
}
