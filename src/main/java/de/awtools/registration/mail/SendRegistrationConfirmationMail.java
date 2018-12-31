package de.awtools.registration.mail;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Sends an email with a link to confirm the registration process.
 *
 * @author Andre Winkler
 */
@Component
public class SendRegistrationConfirmationMail {

    @Autowired
    private SendMail sendMail;
    
    public void send()  {
    }

}
