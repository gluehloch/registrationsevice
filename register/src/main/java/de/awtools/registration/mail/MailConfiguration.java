package de.awtools.registration.mail;

import java.util.Properties;

public interface MailConfiguration {

    /**
     * Creates the mail properties.
     * 
     * @return mail properties
     */
    Properties properties();

    String getMailAuthenticationUser();

    String getMailAuthenticationPassword();

}
