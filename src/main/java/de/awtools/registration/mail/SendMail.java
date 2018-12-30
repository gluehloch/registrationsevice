package de.awtools.registration.mail;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Sends an Email
 * 
 * @author Andre Winkler
 */
@Component
public class SendMail {

    @Autowired
    private MailConfiguration mailConfiguration;

    /**
     * Sends an email.
     * 
     * @param from
     *            {@code from@domain.com}
     * @param recipient
     *            {@code recipient@domain.com}
     * @param subject
     *            Subject
     * @param messageText
     *            Mail message text
     * @throws AddressException
     *             An exception ...
     * @throws MessagingException
     *             An exception ...
     */
    public void sendMail(String from, String recipient, String subject,
            String messageText)
            throws AddressException, MessagingException {

        Session session = Session.getInstance(
                mailConfiguration.properties(),
                new Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(
                                mailConfiguration.getMailAuthenticationUser(),
                                mailConfiguration
                                        .getMailAuthenticationPassword());
                    }
                });

        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(from));
        message.setRecipients(
                Message.RecipientType.TO,
                InternetAddress.parse(recipient));
        message.setSubject(subject);

        MimeBodyPart mimeBodyPart = new MimeBodyPart();
        mimeBodyPart.setContent(messageText, "text/html");

        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(mimeBodyPart);
        message.setContent(multipart);

        Transport.send(message);
    }

}
