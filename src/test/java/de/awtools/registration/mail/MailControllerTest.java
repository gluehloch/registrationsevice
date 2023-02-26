package de.awtools.registration.mail;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import com.icegreen.greenmail.util.ServerSetupTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;

import com.icegreen.greenmail.spring.GreenMailBean;
import com.icegreen.greenmail.user.GreenMailUser;
import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.GreenMailUtil;

import de.awtools.registration.config.PersistenceJPAConfig;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = { PersistenceJPAConfig.class })
@ComponentScan({ "de.awtools.registration" })
@WebAppConfiguration
public class MailControllerTest {

    // JUnit 5 Code
    //@RegisterExtension
    //static GreenMailExtension greenMail = new GreenMailExtension(ServerSetupTest.SMTP);

    @Autowired
    private GreenMailBean greenMailBean;

    @Autowired
    private MailController mailController;

    private GreenMail greenMail;

    @BeforeEach
    void setup() {
        greenMailBean.setHostname("localhost");
        // greenMailBean.setPortOffset(10000);
        greenMailBean.setSmtpProtocol(true);
        greenMailBean.setImapProtocol(true);

        greenMail = greenMailBean.getGreenMail();
        greenMail.start();
    }

    @AfterEach
    void after() {
        greenMail.stop();
    }

    @Test
    @DisplayName("Send an email with GreenMail test.")
    void sendEmailWithGreenMail() {
        GreenMailUtil.sendTextEmailTest("to@localhost", "from@localhost", "some subject", "some body");
        final MimeMessage[] receivedMessages = greenMail.getReceivedMessages();
        final MimeMessage receivedMessage = receivedMessages[0];
        assertThat(GreenMailUtil.getBody(receivedMessage)).isEqualTo("some body");
    }

    @Test
    @DisplayName("Send an email with GreenMail test.")
    void sendPingEmail() throws MessagingException, IOException {
        assertThat(greenMail.getSmtp().getPort()).isEqualTo(3025);

        GreenMailUser user1 = greenMail.setUser("test@localhost", "test");

        mailController.register();

        final MimeMessage[] receivedMessages = greenMail.getReceivedMessages();
        assertThat(receivedMessages).hasSize(1);

        assertThat(receivedMessages[0].getSubject()).isEqualTo("Subject");

        MimeMultipart mp = (MimeMultipart) receivedMessages[0].getContent();
        assertThat(GreenMailUtil.getBody(mp.getBodyPart(0)).trim()).isEqualTo("Das ist ein Test.");
    }

}
