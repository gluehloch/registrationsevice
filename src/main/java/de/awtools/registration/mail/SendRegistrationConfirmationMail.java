package de.awtools.registration.mail;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import de.awtools.registration.Email;
import de.awtools.registration.Token;

/**
 * Sends an email with a link to confirm the registration process.
 *
 * @author Andre Winkler
 */
@Component
public class SendRegistrationConfirmationMail {

    @Autowired
    private SendMail sendMail;
    
    public void send(Email recipient, Token token)  {
        // TODO ... send an email with a link with the token ...
    }

}
