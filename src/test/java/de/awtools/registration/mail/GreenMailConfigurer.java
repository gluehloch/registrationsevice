package de.awtools.registration.mail;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

import com.icegreen.greenmail.spring.GreenMailBean;

@Profile("test")
public class GreenMailConfigurer {

    @Bean
    public GreenMailBean greenMailBean() {
        return new GreenMailBean();
    }

    @Bean
    public MailConfiguration mailConfiguration() {
        
    }

}
