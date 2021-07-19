package de.awtools.registration.mail;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.icegreen.greenmail.spring.GreenMailBean;

@Configuration
public class MailConfig {

    @Bean
    public GreenMailBean greenMailBean() {
        return new GreenMailBean();
    }

}
