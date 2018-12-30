package de.awtools.registration;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;

import de.awtools.registration.mail.SendMail;

/**
 * Tries to send an emai.
 *
 * @author Andre Winkler
 */
@WebAppConfiguration
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = { PersistenceJPAConfig.class })
@ComponentScan("de.awtools.registration")
public class SendMailTest {

    @Autowired
    private SendMail sendMail;

    @Disabled
    @Test
    public void sendMail() throws Exception {
        sendMail.sendMail("donotreply@wp1057914.server-he.de",
                "mail@andre-winkler.de",
                "Register DB test", "This is a link.");
    }

}
