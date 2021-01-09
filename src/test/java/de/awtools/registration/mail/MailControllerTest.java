package de.awtools.registration.mail;

import static org.assertj.core.api.Assertions.assertThat;

import javax.mail.internet.MimeMessage;

import com.icegreen.greenmail.spring.GreenMailBean;
import com.icegreen.greenmail.user.GreenMailUser;
import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.GreenMailUtil;
import com.icegreen.greenmail.util.ServerSetup;
import com.icegreen.greenmail.util.ServerSetupTest;
import de.awtools.registration.config.PersistenceJPAConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;

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

    @Test
    @DisplayName("Send an email with GreenMail test.")
    void testSend() {
        GreenMail greenMail = greenMailBean.getGreenMail();

        GreenMailUtil.sendTextEmailTest("to@localhost", "from@localhost", "some subject", "some body");
        final MimeMessage[] receivedMessages = greenMail.getReceivedMessages();
        final MimeMessage receivedMessage = receivedMessages[0];
        assertThat(GreenMailUtil.getBody(receivedMessage)).isEqualTo("some body");
    }

    @Test
    @DisplayName("Send an email with GreenMail test.")
    void sendRegistrationConfirmationEmail() {
        greenMailBean.setHostname("localhost");
        greenMailBean.setPortOffset(10000);
        greenMailBean.setSmtpProtocol(true);
        greenMailBean.setImapProtocol(true);

        GreenMail greenMail = greenMailBean.getGreenMail();
        greenMail.start();

        GreenMailUser user1 = greenMail.setUser("test@localhost", "test");

        mailController.register();
        final MimeMessage[] receivedMessages = greenMail.getReceivedMessages();
        final MimeMessage receivedMessage = receivedMessages[0];
        assertThat(GreenMailUtil.getBody(receivedMessage)).isEqualTo("some body");

        greenMail.stop();
    }

}
