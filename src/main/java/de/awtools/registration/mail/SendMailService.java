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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Sends an Email
 * 
 * @author Andre Winkler
 */
@Component
public class SendMailService {

    private static final Logger LOG = LoggerFactory.getLogger(SendMailService.class);

    private final MailConfiguration mailConfiguration;

    @Autowired
    public SendMailService(MailConfiguration mailConfiguration) {
        this.mailConfiguration = mailConfiguration;
    }

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
    public void sendMail(String from, String recipient, String subject, String messageText) {
        Session session = Session.getInstance(
                mailConfiguration.properties(),
                new Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(
                                mailConfiguration.getMailAuthenticationUser(),
                                mailConfiguration.getMailAuthenticationPassword());
                    }
                });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipient));
            message.setSubject(subject);

            MimeBodyPart mimeBodyPart = new MimeBodyPart();
            mimeBodyPart.setContent(messageText, "text/html");

            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(mimeBodyPart);
            message.setContent(multipart);

            Transport.send(message);
        } catch (MessagingException ex) {
            LOG.error("Mail sending failed.", ex);
            throw new RuntimeException(ex);
        }
    }

}
