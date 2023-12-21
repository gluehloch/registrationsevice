package de.awtools.registration.mail;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;

import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.GreenMailUtil;
import com.icegreen.greenmail.util.ServerSetup;

import de.awtools.registration.config.PersistenceJPAConfig;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;

@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = { PersistenceJPAConfig.class })
@ComponentScan({ "de.awtools.registration" })
@WebAppConfiguration
public class MailControllerTest {

    @Autowired
    private GreenMail greenMail;

    @Autowired
    private MailController mailController;

    private ServerSetup serverSetup;

    @BeforeEach
    void setup() {
        greenMail.start();
        serverSetup = greenMail.getSmtp().getServerSetup();
    }

    @AfterEach
    void after() {
        greenMail.stop();
    }

    @Test
    @DisplayName("Send an email with GreenMail test.")
    void sendEmailWithGreenMail() {
        GreenMailUtil.sendTextEmail("to@localhost", "from@localhost", "some subject", "some body", serverSetup);

        assertTrue(greenMail.waitForIncomingEmail(1000, 1));

        final MimeMessage[] receivedMessages = greenMail.getReceivedMessages();
        assertThat(receivedMessages).isNotNull().hasSize(1);

        final MimeMessage receivedMessage = receivedMessages[0];
        assertThat(GreenMailUtil.getBody(receivedMessage)).isEqualTo("some body");
    }

    @Test
    @DisplayName("Send an email with GreenMail test.")
    void sendPingEmail() throws MessagingException, IOException {
        assertThat(greenMail.getSmtp().getPort()).isEqualTo(greenMail.getSmtp().getServerSetup().getPort());

        // GreenMailUser user1 = greenMail.setUser("test@localhost", "test");
        mailController.register();

        final MimeMessage[] receivedMessages = greenMail.getReceivedMessages();
        assertThat(receivedMessages).hasSize(1);

        assertThat(receivedMessages[0].getSubject()).isEqualTo("Subject");

        MimeMultipart mp = (MimeMultipart) receivedMessages[0].getContent();
        assertThat(GreenMailUtil.getBody(mp.getBodyPart(0)).trim()).isEqualTo("Das ist ein Test.");
    }

}
