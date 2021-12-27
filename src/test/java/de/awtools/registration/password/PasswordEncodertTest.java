package de.awtools.registration.password;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;

import de.awtools.registration.config.PersistenceJPAConfig;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = { PersistenceJPAConfig.class })
@ComponentScan("de.awtools.registration")
@WebAppConfiguration
public class PasswordEncodertTest {

    @Autowired
    private PasswordEncoderWrapper passwordEncoderWrapper;
    
    @Test
    void passwordEncoder() {
        assertThat(passwordEncoderWrapper).isNotNull();
        String encode = passwordEncoderWrapper.encode("password");
        assertThat(encode).isNotEqualTo("password");
        
        System.out.printf("Encoded password: %s", encode);
        System.out.println();
        System.out.printf("Encoder class: %s", passwordEncoderWrapper.unwrap().getClass());


    }
    
}
