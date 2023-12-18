package de.awtools.registration.mail;

import java.util.Properties;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.icegreen.greenmail.spring.GreenMailBean;

@Profile("test")
@Component
public class GreenMailConfigurationImpl implements MailConfiguration {

    @Override
    public Properties properties() {
        return null;
    }

    @Override
    public String getMailAuthenticationUser() {
        return null;
    }

    @Override
    public String getMailAuthenticationPassword() {
        return null;
    }

}
