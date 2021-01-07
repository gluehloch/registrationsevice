package de.awtools.registration.mail;

import com.icegreen.greenmail.spring.GreenMailBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MailConfig {

    @Bean
    public GreenMailBean greenMailBean() {
        return new GreenMailBean();
    }

}
