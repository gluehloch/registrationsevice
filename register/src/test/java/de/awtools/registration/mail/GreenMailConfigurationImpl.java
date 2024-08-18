package de.awtools.registration.mail;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.icegreen.greenmail.util.GreenMail;

@Profile("test")
@Component
public class GreenMailConfigurationImpl implements MailConfiguration {

    @Autowired
    private GreenMail greenMail;
    
    @Value("${register.mail.smtp.auth}")
    private String mailSmtpAuth;

    @Value("${register.mail.smtp.starttls.enable}")
    private String mailSmtpStarttlsEnable;

    @Value("${register.mail.smtp.host}")
    private String mailSmtpHost;

    // @Value("${register.mail.smtp.port}")
    // private String mailSmtpPort;

    @Value("${register.mail.smtp.ssl.trust}")
    private String mailSmtpSslTrust;

    @Value("${register.mail.authentication.user}")
    private String mailAuthenticationUser;

    @Value("${register.mail.authentication.password}")
    private String mailAuthenticationPassword;

    /**
     * Creates the mail properties.
     * 
     * @return mail properties
     */
    @Override
    public Properties properties() {
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", mailSmtpAuth);
        properties.put("mail.smtp.starttls.enable", mailSmtpStarttlsEnable);
        properties.put("mail.smtp.host", mailSmtpHost);
        properties.put("mail.smtp.port", greenMail.getSmtp().getPort());
        properties.put("mail.smtp.ssl.trust", mailSmtpSslTrust);
        return properties;
    }

    public String isMailSmtpAuth() {
        return mailSmtpAuth;
    }

    public String isMailSmtpStarttlsEnable() {
        return mailSmtpStarttlsEnable;
    }

    public String getMailSmtpHost() {
        return mailSmtpHost;
    }

    public String getMailSmtpPort() {
        return Integer.toString(greenMail.getSmtp().getPort());
    }

    public String getMailSmtpSslTrust() {
        return mailSmtpSslTrust;
    }

    @Override
    public String getMailAuthenticationUser() {
        return mailAuthenticationUser;
    }

    @Override
    public String getMailAuthenticationPassword() {
        return mailAuthenticationPassword;
    }

}
