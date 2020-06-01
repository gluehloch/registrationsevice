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

    @Autowired
    private SendMail sendMail;

    public void send(Email recipient, Token token) {
        Email from = new Email("dont-reply-to-this-mail@gluehloch.de");
        String subject = "Subject";
        String messageText = String.format("Dies ist ein Test. token=[%s]",
                token);

        // TEST TEST TEST
        // TODO ... send an email with a link with the token ...
        //
        try {
            sendMail.sendMail(from.get(), recipient.get(), subject, messageText);
        } catch (MessagingException ex) {
            ex.printStackTrace();
        }
    }

}
