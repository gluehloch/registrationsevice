package de.awtools.registration.password;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;

import de.awtools.registration.config.PersistenceJPAConfig;
import de.awtools.registration.user.Password;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = { PersistenceJPAConfig.class })
@ComponentScan("de.awtools.registration")
@WebAppConfiguration
public class PasswordEncodeTest {

    @Autowired
    private PasswordEncoderWrapper passwordEncoderWrapper;

    @DisplayName("Encode a password")
    @Tag("password")
    @Test
    void passwordEncoder() {
        Password password = Password.decoded("password");
        assertThat(password.isDecoded()).isTrue();
        assertThat(password.isEncoded()).isFalse();

        assertThat(passwordEncoderWrapper).isNotNull();
        Password encodedPassword = passwordEncoderWrapper.encode(password);
        assertThat(encodedPassword.isEncoded()).isTrue();
        assertThat(encodedPassword.isDecoded()).isFalse();
        assertThat(encodedPassword).isNotEqualTo(password);

        System.out.printf("Encoded password: %s", encodedPassword);
        System.out.println();
        System.out.printf("Encoder class: %s", passwordEncoderWrapper.unwrap().getClass());
    }

    @DisplayName("Validate a password")
    @Tag("password")
    @Test
    void passwordValidation() {
        Password password = Password.encoded("password");
        Password passwordToValidate = Password.decoded("password");

        assertThat(password.isEncoded()).isTrue();
        assertThat(passwordToValidate.isDecoded()).isTrue();
        assertThat(passwordToValidate).isEqualTo(password);
        assertThat(passwordEncoderWrapper.validate(password, passwordToValidate)).isTrue();
    }
    
}
