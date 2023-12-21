package de.awtools.registration.mail;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.ServerSetupTest;

@Profile("test")
@Configuration
public class GreenMailConfigurer {

    @Bean
    public GreenMail greenMail() {
        return new GreenMail(ServerSetupTest.SMTP.dynamicPort());
    }

}
