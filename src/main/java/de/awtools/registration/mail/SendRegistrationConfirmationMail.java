package de.awtools.registration.mail;

import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import de.awtools.registration.Token;
import de.awtools.registration.user.Email;

/**
 * Sends an email with a link to confirm the registration process.
 *
 * @author Andre Winkler
 */
@Component
public class SendRegistrationConfirmationMail {

    private static final Email DONT_REPLY_SENDER = Email.of("dont-reply-to-this-mail@gluehloch.de");
    
    @Autowired
    private SendMail sendMail;

    public void send(Email recipient, Token token) {
        String subject = "Subject";
        String messageText = String.format("Dies ist ein Test. token=[%s]", token);

        // TEST TEST TEST
        // TODO ... send an email with a link with the token ...
        //

        try {
            sendMail.sendMail(DONT_REPLY_SENDER.get(), recipient.get(), subject, messageText);
        } catch (MessagingException ex) {
            ex.printStackTrace();
        }
    }

}
